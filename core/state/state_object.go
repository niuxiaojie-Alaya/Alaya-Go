// Copyright 2014 The go-ethereum Authors
// This file is part of the go-ethereum library.

//
// The go-ethereum library is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// The go-ethereum library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with the go-ethereum library. If not, see <http://www.gnu.org/licenses/>.

package state

import (
	"bytes"
	"fmt"
	"io"
	"math/big"
	"time"

	"golang.org/x/crypto/sha3"

	"github.com/AlayaNetwork/Alaya-Go/metrics"

	"github.com/AlayaNetwork/Alaya-Go/common"
	cvm "github.com/AlayaNetwork/Alaya-Go/common/vm"
	"github.com/AlayaNetwork/Alaya-Go/crypto"
	"github.com/AlayaNetwork/Alaya-Go/rlp"
)

var emptyCodeHash = crypto.Keccak256(nil)

type Code []byte

func (c Code) String() string {
	return string(c) //strings.Join(Disassemble(c), " ")
}

type ValueStorage map[string][]byte

func (self ValueStorage) String() (str string) {
	for key, value := range self {
		str += fmt.Sprintf("%X : %X\n", key, value)
	}
	return
}

func (self ValueStorage) Copy() ValueStorage {
	cpy := make(ValueStorage, len(self))
	for key, value := range self {
		cpy[key] = value
	}
	return cpy
}

// stateObject represents an Ethereum account which is being modified.
//
// The usage pattern is as follows:
// First you need to obtain a state object.
// Account values can be accessed and modified through the object.
// Finally, call CommitTrie to write the modified storage trie into a database.
type stateObject struct {
	address  common.Address
	addrHash common.Hash // hash of ethereum address of the account
	data     Account
	db       *StateDB

	// DB error.
	// State objects are used by the consensus core and VM which are
	// unable to deal with database-level errors. Any error that occurs
	// during a database read is memoized here and will eventually be returned
	// by StateDB.Commit.
	dbErr error

	// Write caches.
	trie Trie // storage trie, which becomes non-nil on first access
	code Code // contract bytecode, which gets set when code is loaded

	originStorage  ValueStorage // Storage cache of original entries to dedup rewrites, reset for every transaction
	pendingStorage ValueStorage // Storage entries that need to be flushed to disk, at the end of an entire block
	dirtyStorage   ValueStorage // Storage entries that have been modified in the current transaction execution
	fakeStorage    ValueStorage // Fake storage which constructed by caller for debugging purpose.

	// Cache flags.
	// When an object is marked suicided it will be delete from the trie
	// during the "update" phase of the state transition.
	dirtyCode bool // true if the code was updated
	suicided  bool
	deleted   bool
}

// empty returns whether the account is considered empty.
func (s *stateObject) empty() bool {
	if cvm.PrecompiledContractCheckInstance.IsPlatONPrecompiledContract(s.address) {
		return false
	}
	return s.data.Nonce == 0 && s.data.Balance.Sign() == 0 && bytes.Equal(s.data.CodeHash, emptyCodeHash)
}

// Account is the Ethereum consensus representation of accounts.
// These objects are stored in the main account trie.
type Account struct {
	Nonce            uint64
	Balance          *big.Int
	Root             common.Hash // merkle root of the storage trie
	CodeHash         []byte
	StorageKeyPrefix []byte // A prefix added to the `key` to ensure that data between different accounts are not shared
}

func (self *Account) empty() bool {
	if self.Nonce != 0 {
		return false
	}
	if self.Balance.Cmp(common.Big0) != 0 {
		return false
	}
	if self.Root != common.ZeroHash {
		return false
	}
	if len(self.CodeHash) != 0 {
		return false
	}
	if len(self.StorageKeyPrefix) != 0 {
		return false
	}
	return true
}

// newObject creates a state object.
func newObject(db *StateDB, address common.Address, data Account) *stateObject {
	if data.Balance == nil {
		data.Balance = new(big.Int)
	}
	if data.CodeHash == nil {
		data.CodeHash = emptyCodeHash
	}
	if data.Root == (common.Hash{}) {
		data.Root = emptyRoot
	}
	return &stateObject{
		db:             db,
		address:        address,
		addrHash:       crypto.Keccak256Hash(address[:]),
		data:           data,
		originStorage:  make(ValueStorage),
		pendingStorage: make(ValueStorage),
		dirtyStorage:   make(ValueStorage),
	}
}

// EncodeRLP implements rlp.Encoder.
func (s *stateObject) EncodeRLP(w io.Writer) error {
	return rlp.Encode(w, s.data)
}

// setError remembers the first non-nil error it is called with.
func (s *stateObject) setError(err error) {
	if s.dbErr == nil {
		s.dbErr = err
	}
}

func (s *stateObject) markSuicided() {
	s.suicided = true
}

func (s *stateObject) touch() {
	s.db.journal.append(touchChange{
		account: &s.address,
	})
	if s.address == ripemd {
		// Explicitly put it in the dirty-cache, which is otherwise generated from
		// flattened journals.
		s.db.journal.dirty(s.address)
	}
}

func (s *stateObject) getTrie(db Database) Trie {
	if s.trie == nil {
		var err error
		s.trie, err = db.OpenStorageTrie(s.addrHash, s.data.Root)
		if err != nil {
			s.trie, _ = db.OpenStorageTrie(s.addrHash, common.Hash{})
			s.setError(fmt.Errorf("can't create storage trie: %v", err))
		}
	}
	return s.trie
}

// GetState retrieves a value from the account storage trie.
func (s *stateObject) GetState(db Database, key []byte) []byte {
	// If the fake storage is set, only lookup the state here(in the debugging mode)
	if s.fakeStorage != nil {
		return s.fakeStorage[string(key)]
	}
	// If we have a dirty value for this state entry, return it
	value, dirty := s.dirtyStorage[string(key)]
	if dirty {
		return value
	}
	// Otherwise return the entry's original value
	return s.GetCommittedState(db, key)
}

func (s *stateObject) getCommittedStateCache(key []byte) []byte {
	value, cached := s.originStorage[string(key)]
	if cached {
		return value
	}

	s.db.refLock.Lock()
	parentDB := s.db.parent
	parentCommitted := s.db.parentCommitted
	refLock := &s.db.refLock

	for parentDB != nil {
		value := parentDB.getStateObjectSnapshot(s.address, key)
		if value != nil {
			s.originStorage[string(key)] = value
			refLock.Unlock()
			return value
		} else if parentCommitted {
			refLock.Unlock()
			return nil
		}
		refLock.Unlock()
		parentDB.refLock.Lock()
		refLock = &parentDB.refLock
		if parentDB.parent == nil {
			break
		}
		parentCommitted = parentDB.parentCommitted
		parentDB = parentDB.parent
	}
	refLock.Unlock()

	return nil
}

// GetCommittedState retrieves a value from the committed account storage trie.
func (s *stateObject) GetCommittedState(db Database, key []byte) []byte {
	// If the fake storage is set, only lookup the state here(in the debugging mode)
	if s.fakeStorage != nil {
		return s.fakeStorage[string(key)]
	}
	// If we have a pending write or clean cached, return that
	if value, pending := s.pendingStorage[string(key)]; pending {
		return value
	}
	// If we have the original value cached, return that
	if value := s.getCommittedStateCache(key); len(value) != 0 {
		return value
	}

	// Track the amount of time wasted on reading the storage trie
	if metrics.EnabledExpensive {
		defer func(start time.Time) { s.db.StorageReads += time.Since(start) }(time.Now())
	}
	// Otherwise load the valueKey from trie
	enc, err := s.getTrie(db).TryGet(key[:])
	if err != nil {
		s.setError(err)
		return []byte{}
	}
	value := make([]byte, 0)
	if len(enc) > 0 {
		_, content, _, err := rlp.Split(enc)
		if err != nil {
			s.setError(err)
		}
		value = content
	}

	//log.Trace("GetCommittedState trie", "key", hex.EncodeToString(key), "value", len(value))
	s.originStorage[string(key)] = value
	return value
}

// SetState updates a value in account storage.
// set [prefixKey,value] to storage
func (s *stateObject) SetState(db Database, key, value []byte) {
	// If the fake storage is set, put the temporary state update here.
	if s.fakeStorage != nil {
		s.fakeStorage[string(key)] = value
		return
	}
	//if the new value is the same as old,don't set
	preValue := s.GetState(db, key)
	if bytes.Equal(preValue, value) {
		return
	}

	//New value is different, update and journal the change
	s.db.journal.append(storageChange{
		account:  &s.address,
		key:      key,
		preValue: preValue,
	})

	s.setState(key, value)
}

// SetStorage replaces the entire state storage with the given one.
//
// After this function is called, all original state will be ignored and state
// lookup only happens in the fake state storage.
//
// Note this function should only be used for debugging purpose.
func (s *stateObject) SetStorage(storage map[common.Hash]common.Hash) {
	// Allocate fake storage if it's nil.
	if s.fakeStorage == nil {
		s.fakeStorage = make(ValueStorage)
	}
	for key, value := range storage {
		s.fakeStorage[key.Hex()] = value.Bytes()
	}
	// Don't bother journal since this function should only be used for
	// debugging and the `fake` storage won't be committed to database.
}

func (s *stateObject) setState(key []byte, value []byte) {
	cpy := make([]byte, len(value))
	copy(cpy, value)
	s.dirtyStorage[string(key)] = cpy
}

func (s *stateObject) getPrefixValue(pack, key, value []byte) []byte {
	// Empty value deleted on updateTrie
	if len(value) == 0 {
		return []byte{}
	}
	// Ensure the same Value, unique in the same trie and different trie values
	//prefix := append(s.data.StorageKeyPrefix, key...)
	prefix := append(s.data.StorageKeyPrefix, pack...)
	prefix = append(prefix, key...)
	prefixHash := common.Hash{}
	keccak := sha3.NewLegacyKeccak256()
	keccak.Write(prefix)
	keccak.Sum(prefixHash[:0])
	return append(prefixHash[:], value...)
}

func (s *stateObject) removePrefixValue(value []byte) []byte {
	if len(value) > common.HashLength {
		return value[common.HashLength:]
	}
	return []byte{}
}

// finalise moves all dirty storage slots into the pending area to be hashed or
// committed later. It is invoked at the end of every transaction.
func (s *stateObject) finalise() {
	for key, value := range s.dirtyStorage {
		s.pendingStorage[key] = value
	}
	if len(s.dirtyStorage) > 0 {
		s.dirtyStorage = make(ValueStorage)
	}
}

// updateTrie writes cached storage modifications into the object's storage trie.
func (s *stateObject) updateTrie(db Database) Trie {
	// Make sure all dirty slots are finalized into the pending storage area
	s.finalise()
	if len(s.pendingStorage) == 0 {
		return s.trie
	}

	// Insert all the pending updates into the trie
	tr := s.getTrie(db)
	// Track the amount of time wasted on updating the storage trie
	if metrics.EnabledExpensive {
		defer func(start time.Time) { s.db.StorageUpdates += time.Since(start) }(time.Now())
	}
	for key, value := range s.pendingStorage {

		// Skip noop changes, persist actual changes
		oldValue := s.originStorage[key]
		if bytes.Equal(value, oldValue) {
			continue
		}

		s.originStorage[key] = value

		if len(value) == 0 {
			s.setError(tr.TryDelete([]byte(key)))
			continue
		}

		// Encoding []byte cannot fail, ok to ignore the error.
		v, _ := rlp.EncodeToBytes(value)
		s.setError(tr.TryUpdate([]byte(key), v))
	}

	if len(s.pendingStorage) > 0 {
		s.pendingStorage = make(ValueStorage)
	}

	return tr
}

// UpdateRoot sets the trie root to the current root hash of
func (s *stateObject) updateRoot(db Database) {
	// If nothing changed, don't bother with hashing anything
	if s.updateTrie(db) == nil {
		return
	}

	// Track the amount of time wasted on hashing the storage trie
	if metrics.EnabledExpensive {
		defer func(start time.Time) { s.db.StorageHashes += time.Since(start) }(time.Now())
	}
	//s.data.Root = s.trie.Hash()
	s.data.Root = s.trie.Hash()
}

// CommitTrie the storage trie of the object to db.
// This updates the trie root.
func (s *stateObject) CommitTrie(db Database) error {
	// If nothing changed, don't bother with hashing anything
	if s.updateTrie(db) == nil {
		return nil
	}
	if s.dbErr != nil {
		return s.dbErr
	}

	// Track the amount of time wasted on committing the storage trie
	if metrics.EnabledExpensive {
		defer func(start time.Time) { s.db.StorageCommits += time.Since(start) }(time.Now())
	}
	root, _, err := s.trie.Commit(nil)

	if err == nil {
		s.data.Root = root
	}
	return err
}

// AddBalance removes amount from c's balance.
// It is used to add funds to the destination account of a transfer.
func (s *stateObject) AddBalance(amount *big.Int) {
	// EIP158: We must check emptiness for the objects such that the account
	// clearing (0,0,0 objects) can take effect.
	if amount.Sign() == 0 {
		if s.empty() {
			s.touch()
		}

		return
	}
	s.SetBalance(new(big.Int).Add(s.Balance(), amount))
}

// SubBalance removes amount from c's balance.
// It is used to remove funds from the origin account of a transfer.
func (s *stateObject) SubBalance(amount *big.Int) {
	if amount.Sign() == 0 {
		return
	}
	s.SetBalance(new(big.Int).Sub(s.Balance(), amount))
}

func (s *stateObject) SetBalance(amount *big.Int) {
	s.db.journal.append(balanceChange{
		account: &s.address,
		prev:    new(big.Int).Set(s.data.Balance),
	})
	s.setBalance(amount)
}

func (s *stateObject) setBalance(amount *big.Int) {
	s.data.Balance = amount
}

// Return the gas back to the origin. Used by the Virtual machine or Closures
func (s *stateObject) ReturnGas(gas *big.Int) {}

func (s *stateObject) deepCopy(db *StateDB) *stateObject {
	stateObject := newObject(db, s.address, s.data)
	if s.trie != nil {
		stateObject.trie = db.db.CopyTrie(s.trie)
	}
	stateObject.code = s.code
	stateObject.dirtyStorage = s.dirtyStorage.Copy()
	stateObject.originStorage = s.originStorage.Copy()
	stateObject.pendingStorage = s.pendingStorage.Copy()
	stateObject.suicided = s.suicided
	stateObject.dirtyCode = s.dirtyCode
	stateObject.deleted = s.deleted
	return stateObject
}

// Copy account status, recreate trie
func (s *stateObject) copy(db *StateDB) *stateObject {
	stateObject := newObject(db, s.address, s.data)
	if s.trie != nil {
		stateObject.trie = db.db.NewTrie(s.trie)
	}
	stateObject.code = s.code
	stateObject.suicided = s.suicided
	stateObject.dirtyCode = s.dirtyCode
	stateObject.deleted = s.deleted
	return stateObject
}

//
// Attribute accessors
//

// Returns the address of the contract/account
func (s *stateObject) Address() common.Address {
	return s.address
}

// Code returns the contract code associated with this object, if any.
func (s *stateObject) Code(db Database) []byte {
	if s.code != nil {
		return s.code
	}
	if bytes.Equal(s.CodeHash(), emptyCodeHash) {
		return nil
	}
	code, err := db.ContractCode(s.addrHash, common.BytesToHash(s.CodeHash()))
	if err != nil {
		s.setError(fmt.Errorf("can't load code hash %x: %v", s.CodeHash(), err))
	}
	s.code = code
	return code
}

func (s *stateObject) SetCode(codeHash common.Hash, code []byte) {
	prevcode := s.Code(s.db.db)
	s.db.journal.append(codeChange{
		account:  &s.address,
		prevhash: s.CodeHash(),
		prevcode: prevcode,
	})
	s.setCode(codeHash, code)
}

func (s *stateObject) setCode(codeHash common.Hash, code []byte) {
	s.code = code
	s.data.CodeHash = codeHash[:]
	s.dirtyCode = true
}

func (s *stateObject) SetNonce(nonce uint64) {
	s.db.journal.append(nonceChange{
		account: &s.address,
		prev:    s.data.Nonce,
	})
	s.setNonce(nonce)
}

func (s *stateObject) setNonce(nonce uint64) {
	s.data.Nonce = nonce
}

func (s *stateObject) CodeHash() []byte {
	return s.data.CodeHash
}

func (s *stateObject) Balance() *big.Int {
	return s.data.Balance
}

func (s *stateObject) Nonce() uint64 {
	return s.data.Nonce
}

// Never called, but must be present to allow stateObject to be used
// as a vm.Account interface that also satisfies the vm.ContractRef
// interface. Interfaces are awesome.
func (s *stateObject) Value() *big.Int {
	panic("Value on stateObject should never be called")
}
