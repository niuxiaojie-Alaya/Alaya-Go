package network.platon.contracts.wasm;

import com.platon.rlp.datatypes.Int8;
import com.platon.rlp.datatypes.Uint8;
import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.WasmFunctionEncoder;
import org.web3j.abi.datatypes.WasmFunction;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.WasmContract;
import org.web3j.tx.gas.GasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://github.com/PlatONnetwork/client-sdk-java/releases">platon-web3j command line tools</a>,
 * or the org.web3j.codegen.WasmFunctionWrapperGenerator in the 
 * <a href="https://github.com/PlatONnetwork/client-sdk-java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with platon-web3j version 0.13.0.6.
 */
public class InitOverloadWithString extends WasmContract {
    private static String BINARY_0 = "0x0061736d0100000001530e60027f7f0060017f0060017f017f60000060027f7f017f60037f7f7f0060027f7e0060037f7f7f017f60047f7f7f7f0060047f7f7f7f017f60087f7f7f7f7f7f7f7f006000017f60027f7e017f60017f017e02a9010703656e760c706c61746f6e5f70616e6963000303656e7617706c61746f6e5f6765745f696e7075745f6c656e677468000b03656e7610706c61746f6e5f6765745f696e707574000103656e7617706c61746f6e5f6765745f73746174655f6c656e677468000403656e7610706c61746f6e5f6765745f7374617465000903656e7610706c61746f6e5f7365745f7374617465000803656e760d706c61746f6e5f72657475726e0000033c3b03020101010000040502050a040700040302070501000d00020101020601000c0000010309020600020006000702000000010205030302020804000405017001050505030100020608017f0141908b040b073904066d656d6f72790200115f5f7761736d5f63616c6c5f63746f727300070f5f5f66756e63735f6f6e5f65786974002a06696e766f6b650017090a010041010b04090c15090ad04c3b100041d80810081a4101100a103b103c0b190020004200370200200041086a41003602002000100b20000b0300010b940101027f41e408410136020041e808280200220145044041e80841f00836020041f00821010b024041ec082802002202412046044041840210182201450d0120011039220141e80828020036020041e808200136020041ec084100360200410021020b41ec08200241016a360200200120024102746a22014184016a4100360200200141046a20003602000b41e40841003602000b2201017f03402001410c470440200020016a4100360200200141046a21010c010b0b0b0b00200041186a2001100d0bd40101047f20002001470440200128020420012d00002202410176200241017122031b2102200141016a21042001280208410a2101200420031b210320002d00002205410171220404402000280200417e71417f6a21010b200220014d0440027f2004044020002802080c010b200041016a0b210120020440200120032002103a0b200120026a41003a000020002d00004101710440200020023602040f0b200020024101743a00000f0b20002001200220016b027f2004044020002802040c010b20054101760b2200410020002002200310120b0ba10101037f20004200370200200041086a2202410036020020012d0000410171450440200020012902003702002002200141086a28020036020020000f0b20012802082103024020012802042201410a4d0440200020014101743a0000200041016a21020c010b200141106a4170712204101021022000200136020420002004410172360200200020023602080b2002200320011011200120026a41003a000020000bef0201047f20004200370200200041086a41003602002000100b200141206a280200200141196a20012d0018220341017122051b21060240200228020420022d0000220441017620044101711b22042001411c6a280200200341017620051b22016a2203410a4d0440200020014101743a0000200041016a21030c010b200341106a4170712205101021032000200136020420002005410172360200200020033602080b2003200620011011200120036a41003a00002002280208200241016a20022d00004101711b21030240027f20002d0000220141017122050440200028020421022000280200417e71417f6a0c010b20014101762102410a0b220120026b20044f04402004450d012000280208200041016a20051b220520026a200320041011200220046a2101024020002d00004101710440200020013602040c010b200020014101743a00000b200120056a41003a00000f0b20002001200220046a20016b2002200241002004200310120b0b0b002000410120001b10180b10002002044020002001200210331a0b0bc30101027f027f20002d0000410171044020002802080c010b200041016a0b2109416f2108200141e6ffffff074d0440410b20014101742208200120026a220120012008491b220141106a4170712001410b491b21080b2008101021012004044020012009200410110b20060440200120046a2007200610110b200320056b220220046b22030440200120046a20066a200420096a20056a200310110b200020013602082000200220066a220236020420002008410172360200200120026a41003a00000b810201087f4101210302402001280208200141016a20012d0000220741017122051b2000280208200041016a20002d0000220241017122061b2000280204200241017620061b22042001280204200741017620051b220820042008491b101422092008200449411f7420091b4100480d0041ff01210320002802042002410176220220061b22042001280204200741017620051b470d002001280208200141016a20051b21012006450440200041016a210003402002450440410021030c030b20002d000020012d0000470d02200141016a2101200041016a21002002417f6a21020c000b000b417f410020002802082001200410141b21030b20034118744118750b4601037f02402002450d0003402002450d0120002d0000220420012d00002205460440200141016a2101200041016a21002002417f6a21020c010b0b200420056b21030b20030b7b01017f027f20002d0018220141017104402000411c6a2802002101200041206a2802000c010b20014101762101200041196a0b210002402001450d00200020016a417f6a21010340200020014f0d0120002d00002102200020012d00003a0000200120023a00002001417f6a2101200041016a21000c000b000b0b9103010b7f027f20002d0018220341017104402000411c6a2802002102200041206a2802000c010b20034101762102200041196a0b2106027f4100200128020420012d00002200410176200041017122001b2205450d001a200220066a21030240024020022005480d00410120056b21092001280208200141016a20001b220a2d0000220741818284086c210b200621040340200320046b22002005480d01200020096a2208450d01410021000240024003402000200846220c200020046a2201410371457245044020012d00002007460d02200041016a21000c010b0b41002102200c0d010b200820006b2102200020046a22012d00002007460d00200020046a2101034020024104490d012001280200200b732200417f73200041fffdfb776a71418081828478710d012002417c6a2102200141046a21010c000b000b03402002450d02200720012d00004704402002417f6a2102200141016a21010c010b0b2001200a20051014450d02200141016a21040c000b000b200321010b417f200120066b20012003461b0b4118744118750bd90602057f017e23004190016b22002400100710012201101822021002200041e8006a200041086a20022001101922024100101a200041e8006a101b02400240200028026c450d00200041e8006a101b0240200028026822012c0000220341004e044020030d010c020b200341807f460d00200341ff0171220441b7014d0440200028026c41014d04401000200028026821010b20012d00010d010c020b200441bf014b0d01200028026c200341ff017141ca7e6a22034d04401000200028026821010b200120036a2d0000450d010b200028026c450d0020012d000041c001490d010b10000b200041386a200041e8006a101c200028023c220341094f044010000b200028023821010340200304402003417f6a210320013100002005420886842105200141016a21010c010b0b024002402005500d00418008101d200551044020024102101e0c020b418508101d2005510440200041e8006a101f200041386a20004180016a100e102010210c020b419008101d2005510440200041e8006a101f20004184016a280200210420002d0080012101200041206a10222102200041d0006a4100360200200041c8006a4200370300200041406b420037030020004200370338200041386a2004200141017620014101711b41ff0171ad2205102320002802382101200041386a4104721024200220011025200220051026220128020c200141106a28020047044010000b200128020020012802041006200128020c22020440200120023602100b10210c020b419e08101d2005510440200041d8006a100821012002200041d8006a1027200041e8006a101f200041386a200041e8006a200041206a2001100e100f200041386a102010210c020b41ac08101d2005510440200041386a10082101200041c4006a1008210320004101360220200020023602682000200041206a36026c200041e8006a20011028200041e8006a20031028200041e8006a101f2000200041206a2001100e200041d8006a2003100e10133a0067200041e7006a102910210c020b41bb08101d200551044020024103101e0c020b41ca08101d2005520d00200041206a100821012002200041206a1027200041e8006a101f2000200041e8006a200041386a2001100e10163a0058200041d8006a102910210c010b10000b102a20004190016a24000b970101047f230041106b220124002001200036020c2000047f41880b200041086a2202411076220041880b2802006a220336020041840b200241840b28020022026a41076a417871220436020002400240200341107420044d044041880b200341016a360200200041016a21000c010b2000450d010b200040000d0010000b20022001410c6a4104103341086a0541000b200141106a24000b0c00200020012002411c102b0bc90202077f017e230041106b220324002001280208220520024b0440200341086a2001104120012003280208200328020c104036020c200320011041410021052001027f410020032802002206450d001a410020032802042208200128020c2207490d001a200820072007417f461b210420060b360210200141146a2004360200200141003602080b200141106a210903402001280214210402402005200249044020040d01410021040b2000200928020020044114102b1a200341106a24000f0b20032001104141002104027f410020032802002207450d001a410020032802042208200128020c2206490d001a200820066b2104200620076a0b21052001200436021420012005360210200320094100200520041040103f20012003290300220a3702102001200128020c200a422088a76a36020c2001200128020841016a22053602080c000b000b4101017f200028020445044010000b0240200028020022012d0000418101470d00200028020441014d047f100020002802000520010b2c00014100480d0010000b0bd40101047f2001102c2204200128020422024b04401000200128020421020b200128020021052000027f02400240200204404100210120052c00002203417f4a0d01027f200341ff0171220141bf014d04404100200341ff017141b801490d011a200141c97e6a0c010b4100200341ff017141f801490d001a200141897e6a0b41016a21010c010b4101210120050d000c010b410021032002200149200120046a20024b720d00410020022004490d011a200120056a2103200220016b20042004417f461b0c010b41000b360204200020033602000b3901027e42a5c688a1c89ca7f94b210103402000300000220250450440200041016a2100200142b383808080207e20028521010c010b0b20010b4401027f230041d0006b22022400200241306a100821032000200241306a1027200241086a101f200241086a200241406b2003100e20011100001021200241d0006a24000bdc0101097f230041306b2205240020001008210720004295cf95d0e0dbf488997f370310200041186a10082106200541186a102222022000290310102d200228020c200241106a28020047044010000b02400240200228020022082002280204220910032204450d002004101021010340200120036a41003a00002004200341016a2203470d000b20082009200120031004417f460d002005200141016a200120036a2001417f736a10192006102e0c010b410021040b200228020c22010440200220013602100b200445044020062007100d0b200541306a240020000b6001027f230041306b22022400200241186a102222012000102f10252001200241086a2000100e1030200128020c200141106a28020047044010000b200128020020012802041006200128020c22000440200120003602100b200241306a24000bfb0201097f230041d0006b22022400200241186a10222103200241c8006a4100360200200241406b4200370300200241386a420037030020024200370330200241306a2000290310103120022802302101200241306a410472102420032001102520032000290310102d200328020c200341106a28020047044010000b200328020421042003280200200241306a10222101200041186a2206102f210741011010220041fe013a0000200128020c200141106a28020047044010000b2001280204220841016a220920012802084b047f20012009103220012802040520080b20012802006a2000410110331a2001200128020441016a3602042001200041016a200720006b6a10252001200241086a2006100e10300240200128020c2001280210460440200128020021000c010b100020012802002100200128020c2001280210460d0010000b2004200020012802041005200128020c22000440200120003602100b200328020c22000440200320003602100b200241d0006a24000b29002000410036020820004200370200200041001032200041146a41003602002000420037020c20000b9f0102027f017e4101210320014280015a0440034020012004845045044020044238862001420888842101200241016a2102200442088821040c010b0b200241384f047f2002103420026a0520020b41016a21030b200041186a28020022020440200041086a2802002002200041146a2802006a417f6a220041087641fcffff07716a280200200041ff07714102746a21000b2000200028020020036a3602000b860201067f200028020422032000280210220241087641fcffff07716a2101027f200320002802082204460440200041146a210541000c010b2003200028021420026a220541087641fcffff07716a280200200541ff07714102746a2106200041146a21052001280200200241ff07714102746a0b21020340024020022006460440200541003602000340200420036b41027522014103490d022000200341046a22033602040c000b000b200241046a220220012802006b418020470d0120012802042102200141046a21010c010b0b2001417f6a220141014d04402000418004418008200141016b1b3602100b03402003200447044020002004417c6a22043602080c010b0b0b1300200028020820014904402000200110320b0bba0202037f037e02402001500440200041800110350c010b20014280015a044020012107034020062007845045044020064238862007420888842107200241016a2102200642088821060c010b0b0240200241384f04402002210303402003044020034108762103200441016a21040c010b0b200441c9004f044010000b2000200441b77f6a41ff017110352000200028020420046a1036200028020420002802006a417f6a21042002210303402003450d02200420033a0000200341087621032004417f6a21040c000b000b200020024180017341ff017110350b2000200028020420026a1036200028020420002802006a417f6a210203402001200584500d02200220013c0000200542388620014208888421012002417f6a2102200542088821050c000b000b20002001a741ff017110350b2000103820000b2801017f230041206b22022400200241086a20004101101a200241086a2001102e200241206a24000b4301017f230041206b22022400200241086a20002802002000280204280200101a200241086a2001102e20002802042200200028020041016a360200200241206a24000bb60102037f017e230041406a22012400200141086a10222102200141386a4100360200200141306a4200370300200141286a420037030020014200370320200141206a200030000022044201862004423f8785103120012802202103200141206a41047210242002200310252002200030000022044201862004423f8785102d200228020c200241106a28020047044010000b200228020020022802041006200228020c22000440200220003602100b200141406b24000b880101037f41e408410136020041e8082802002100034020000440034041ec0841ec082802002201417f6a2202360200200141014845044041e4084100360200200020024102746a22004184016a280200200041046a28020011010041e408410136020041e80828020021000c010b0b41ec08412036020041e808200028020022003602000c010b0b0b730020004200370210200042ffffffff0f370208200020023602042000200136020002402003410871450d002000103d20024f0d002003410471044010000c010b200042003702000b02402003411071450d002000103d20024d0d0020034104710440100020000f0b200042003702000b20000bff0201037f200028020445044041000f0b2000101b41012102024020002802002c00002201417f4a0d00200141ff0171220341b7014d0440200341807f6a0f0b02400240200141ff0171220141bf014d0440024020002802042201200341c97e6a22024d047f100020002802040520010b4102490d0020002802002d00010d0010000b200241054f044010000b20002802002d000145044010000b4100210241b7012101034020012003460440200241384f0d030c0405200028020020016a41ca7e6a2d00002002410874722102200141016a21010c010b000b000b200141f7014d0440200341c07e6a0f0b024020002802042201200341897e6a22024d047f100020002802040520010b4102490d0020002802002d00010d0010000b200241054f044010000b20002802002d000145044010000b4100210241f701210103402001200346044020024138490d0305200028020020016a418a7e6a2d00002002410874722102200141016a21010c010b0b0b200241ff7d490d010b10000b20020b09002000200110261a0bf40201057f230041206b22022400024002402000280204044020002802002d000041c001490d010b200241086a10081a0c010b200241186a2000101c2000102c21030240024002400240200228021822000440200228021c220420034f0d010b41002100200241106a410036020020024200370308410021040c010b200241106a4100360200200242003703082000200420032003417f461b22046a21052004410a4b0d010b200220044101743a0008200241086a41017221030c010b200441106a4170712206101021032002200436020c20022006410172360208200220033602100b03402000200546450440200320002d00003a0000200341016a2103200041016a21000c010b0b200341003a00000b024020012d0000410171450440200141003b01000c010b200128020841003a00002001410036020420012d0000410171450d00200141003602000b20012002290308370200200141086a200241106a280200360200200241086a100b200241206a24000bb80101047f230041306b22012400200141286a4100360200200141206a4200370300200141186a4200370300200142003703104101210202400240024020012000100e220328020420032d00002200410176200041017122041b220041014d0440200041016b0d032003280208200341016a20041b2c0000417f4c0d010c030b200041374b0d010b200041016a21020c010b2000103420006a41016a21020b20012002360210200141106a4104721024200141306a240020020b810201047f410121022001280208200141016a20012d0000220341017122051b210402400240024002402001280204200341017620051b2203410146044020042c000022014100480d012000200141ff017110350c040b200341374b0d01200321020b200020024180017341ff017110350c010b20031034220141b7016a22024180024e044010000b2000200241ff017110352000200028020420016a1036200028020420002802006a417f6a210220032101037f2001047f200220013a0000200141087621012002417f6a21020c010520030b0b21020b200020021037200028020020002802046a2004200210331a2000200028020420026a3602040b200010380b08002000200110230b2f01017f2000280208200149044020011018200028020020002802041033210220002001360208200020023602000b0bf80801067f0340200020046a2105200120046a220341037145200220044672450440200520032d00003a0000200441016a21040c010b0b200220046b210602402005410371220845044003402006411049450440200020046a2202200120046a2203290200370200200241086a200341086a290200370200200441106a2104200641706a21060c010b0b027f2006410871450440200120046a2103200020046a0c010b200020046a2202200120046a2201290200370200200141086a2103200241086a0b21042006410471044020042003280200360200200341046a2103200441046a21040b20064102710440200420032f00003b0000200341026a2103200441026a21040b2006410171450d01200420032d00003a000020000f0b024020064120490d002008417f6a220841024b0d00024002400240024002400240200841016b0e020102000b2005200120046a220628020022033a0000200541016a200641016a2f00003b0000200041036a2108200220046b417d6a2106034020064111490d03200420086a2202200120046a220541046a2802002207410874200341187672360200200241046a200541086a2802002203410874200741187672360200200241086a2005410c6a28020022074108742003411876723602002002410c6a200541106a2802002203410874200741187672360200200441106a2104200641706a21060c000b000b2005200120046a220628020022033a0000200541016a200641016a2d00003a0000200041026a2108200220046b417e6a2106034020064112490d03200420086a2202200120046a220541046a2802002207411074200341107672360200200241046a200541086a2802002203411074200741107672360200200241086a2005410c6a28020022074110742003411076723602002002410c6a200541106a2802002203411074200741107672360200200441106a2104200641706a21060c000b000b2005200120046a28020022033a0000200041016a21082004417f7320026a2106034020064113490d03200420086a2202200120046a220541046a2802002207411874200341087672360200200241046a200541086a2802002203411874200741087672360200200241086a2005410c6a28020022074118742003410876723602002002410c6a200541106a2802002203411874200741087672360200200441106a2104200641706a21060c000b000b200120046a41036a2103200020046a41036a21050c020b200120046a41026a2103200020046a41026a21050c010b200120046a41016a2103200020046a41016a21050b20064110710440200520032d00003a00002005200328000136000120052003290005370005200520032f000d3b000d200520032d000f3a000f200541106a2105200341106a21030b2006410871044020052003290000370000200541086a2105200341086a21030b2006410471044020052003280000360000200541046a2105200341046a21030b20064102710440200520032f00003b0000200541026a2105200341026a21030b2006410171450d00200520032d00003a00000b20000b1e01017f03402000044020004108762100200141016a21010c010b0b20010b2500200041011037200028020020002802046a20013a00002000200028020441016a3602040b0f00200020011032200020013602040b1b00200028020420016a220120002802084b04402000200110320b0bf80101057f0340024020002802102201200028020c460d00200141786a28020041014904401000200028021021010b200141786a2202200228020041016b220436020020040d002000200236021020004101200028020422032001417c6a28020022026b22011034220441016a20014138491b220520036a1036200220002802006a220320056a20032001103a0240200141374d0440200028020020026a200141406a3a00000c010b200441f7016a220341ff014d0440200028020020026a20033a00002000280200200220046a6a210203402001450d02200220013a0000200141087621012002417f6a21020c000b000b10000b0c010b0b0bc90201037f200041003a000020004184026a2201417f6a41003a0000200041003a0002200041003a00012001417d6a41003a00002001417e6a41003a0000200041003a00032001417c6a41003a00002000410020006b41037122026a22014100360200200141840220026b417c7122036a2202417c6a4100360200024020034109490d002001410036020820014100360204200241786a4100360200200241746a410036020020034119490d002001410036021820014100360214200141003602102001410036020c200241706a41003602002002416c6a4100360200200241686a4100360200200241646a41003602002003200141047141187222036b2102200120036a2101034020024120490d0120014200370300200141186a4200370300200141106a4200370300200141086a4200370300200141206a2101200241606a21020c000b000b20000b8d0301037f024020002001460d00200120006b20026b410020024101746b4d044020002001200210331a0c010b20002001734103712103027f024020002001490440200020030d021a410021030340200120036a2104200020036a2205410371450440200220036b210241002103034020024104490d04200320056a200320046a280200360200200341046a21032002417c6a21020c000b000b20022003460d04200520042d00003a0000200341016a21030c000b000b024020030d002001417f6a21030340200020026a22044103714504402001417c6a21032000417c6a2104034020024104490d03200220046a200220036a2802003602002002417c6a21020c000b000b2002450d042004417f6a200220036a2d00003a00002002417f6a21020c000b000b2001417f6a210103402002450d03200020026a417f6a200120026a2d00003a00002002417f6a21020c000b000b200320046a2101200320056a0b210303402002450d01200320012d00003a00002002417f6a2102200341016a2103200141016a21010c000b000b0b3501017f230041106b220041908b0436020c41800b200028020c41076a417871220036020041840b200036020041880b3f003602000b3801017f41f40a420037020041fc0a410036020041742100034020000440200041800b6a4100360200200041046a21000c010b0b4104100a0b2e01017f200028020445044041000f0b4101210120002802002c0000417f4c047f2000103e2000102c6a0541010b0b5b00027f027f41002000280204450d001a410020002802002c0000417f4a0d011a20002802002d0000220041bf014d04404100200041b801490d011a200041c97e6a0c010b4100200041f801490d001a200041897e6a0b41016a0b0b5a01027f2000027f0240200128020022054504400c010b200220036a200128020422014b2001200249720d00410020012003490d011a200220056a2104200120026b20032003417f461b0c010b41000b360204200020043602000b2301017f230041206b22022400200241086a200020014114102b103d200241206a24000b2101017f2001102c220220012802044b044010000b200020012001103e2002103f0b0b5c01004180080b55696e6974006765745f737472696e6700737472696e675f6c656e67746800737472696e675f73706c69636500737472696e675f636f6d7061726500737472696e675f7265766572736500737472696e675f66696e64";

    public static String BINARY = BINARY_0;

    public static final String FUNC_GET_STRING = "get_string";

    public static final String FUNC_STRING_LENGTH = "string_length";

    public static final String FUNC_STRING_SPLICE = "string_splice";

    public static final String FUNC_STRING_COMPARE = "string_compare";

    public static final String FUNC_STRING_REVERSE = "string_reverse";

    public static final String FUNC_STRING_FIND = "string_find";

    protected InitOverloadWithString(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider, Long chainId) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider, chainId);
    }

    protected InitOverloadWithString(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, Long chainId) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider, chainId);
    }

    public RemoteCall<String> get_string() {
        final WasmFunction function = new WasmFunction(FUNC_GET_STRING, Arrays.asList(), String.class);
        return executeRemoteCall(function, String.class);
    }

    public static RemoteCall<InitOverloadWithString> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider, Long chainId, String initStr) {
        String encodedConstructor = WasmFunctionEncoder.encodeConstructor(BINARY, Arrays.asList(initStr));
        return deployRemoteCall(InitOverloadWithString.class, web3j, credentials, contractGasProvider, encodedConstructor, chainId);
    }

    public static RemoteCall<InitOverloadWithString> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, Long chainId, String initStr) {
        String encodedConstructor = WasmFunctionEncoder.encodeConstructor(BINARY, Arrays.asList(initStr));
        return deployRemoteCall(InitOverloadWithString.class, web3j, transactionManager, contractGasProvider, encodedConstructor, chainId);
    }

    public static RemoteCall<InitOverloadWithString> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider, BigInteger initialVonValue, Long chainId, String initStr) {
        String encodedConstructor = WasmFunctionEncoder.encodeConstructor(BINARY, Arrays.asList(initStr));
        return deployRemoteCall(InitOverloadWithString.class, web3j, credentials, contractGasProvider, encodedConstructor, initialVonValue, chainId);
    }

    public static RemoteCall<InitOverloadWithString> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, BigInteger initialVonValue, Long chainId, String initStr) {
        String encodedConstructor = WasmFunctionEncoder.encodeConstructor(BINARY, Arrays.asList(initStr));
        return deployRemoteCall(InitOverloadWithString.class, web3j, transactionManager, contractGasProvider, encodedConstructor, initialVonValue, chainId);
    }

    public RemoteCall<Uint8> string_length() {
        final WasmFunction function = new WasmFunction(FUNC_STRING_LENGTH, Arrays.asList(), Uint8.class);
        return executeRemoteCall(function, Uint8.class);
    }

    public RemoteCall<String> string_splice(String spliceStr) {
        final WasmFunction function = new WasmFunction(FUNC_STRING_SPLICE, Arrays.asList(spliceStr), String.class);
        return executeRemoteCall(function, String.class);
    }

    public RemoteCall<Int8> string_compare(String strone, String strtwo) {
        final WasmFunction function = new WasmFunction(FUNC_STRING_COMPARE, Arrays.asList(strone,strtwo), Int8.class);
        return executeRemoteCall(function, Int8.class);
    }

    public RemoteCall<TransactionReceipt> string_reverse(String reverseStr) {
        final WasmFunction function = new WasmFunction(FUNC_STRING_REVERSE, Arrays.asList(reverseStr), Void.class);
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> string_reverse(String reverseStr, BigInteger vonValue) {
        final WasmFunction function = new WasmFunction(FUNC_STRING_REVERSE, Arrays.asList(reverseStr), Void.class);
        return executeRemoteCallTransaction(function, vonValue);
    }

    public RemoteCall<Int8> string_find(String findStr) {
        final WasmFunction function = new WasmFunction(FUNC_STRING_FIND, Arrays.asList(findStr), Int8.class);
        return executeRemoteCall(function, Int8.class);
    }

    public static InitOverloadWithString load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider, Long chainId) {
        return new InitOverloadWithString(contractAddress, web3j, credentials, contractGasProvider, chainId);
    }

    public static InitOverloadWithString load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, Long chainId) {
        return new InitOverloadWithString(contractAddress, web3j, transactionManager, contractGasProvider, chainId);
    }
}