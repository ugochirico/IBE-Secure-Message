package hamaster.gradesign.daemon;

import hamaster.gradesgin.ibe.IBEPrivateKey;
import hamaster.gradesgin.ibs.IBSCertificate;
import hamaster.gradesgin.util.IBECapsule;
import hamaster.gradesgin.util.IBECapsuleAESImpl;
import hamaster.gradesign.IdentityDescription;
import hamaster.gradesign.client.Base64Encoder;
import hamaster.gradesign.client.Encoder;
import hamaster.gradesign.client.HexEncoder;
import hamaster.gradesign.ibe.util.Hex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class EJBClient {

	private static EJBClient instance;

	private BeanFactory factory;

	private Logger logger;

	public final static Util util;

	private Map<String, Encoder> encoders;

	private IBSCertificate serverCertificate;
	private IBEPrivateKey serverPrivateKey;

	static {
		if (instance == null)
			instance = new EJBClient();
		util = new EJBClient.Util();
	}

	private EJBClient() {
	}

	public static EJBClient getInstance() {
		return instance;
	}

	public void init() {
		logger = LoggerFactory.getLogger(getClass());
		factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		logger.info(new StringBuilder("Connected to EJB server at ").append(new Date().toString()).toString());
		encoders = new HashMap<String, Encoder>();
		registerBuiltinEncoders();
	}

	private void registerBuiltinEncoders() {
		Encoder base64 = new Base64Encoder();
		encoders.put(base64.name(), base64);
		Encoder hex = new HexEncoder();
		encoders.put(hex.name(), hex);
	}

	public Object getBean(String name) {
		return factory.getBean(name);
	}

	public <T> T getBean(String name, Class<T> clazz) {
		return factory.getBean(name, clazz);
	}

	public Encoder getEncoderByName(String name) {
		return encoders.get(name);
	}

	private synchronized void prepareServerKeys() {
		String serverIdContent = "144145532F4342432F504B43533550616464696E67075348412D353132E13EFC991A9BF44BBB4DA87CDBB725240184585CCAF270523170E008CF2A3B85F45F86C3DA647F69780FB9E971CAF5437B3D06D418355A68C9760C70A31D05C700000C4E00000C500E2838B92DA890BAE4230B7BAC5C9E3E94C562EE6FBE4F24D4778BC6095650B4E2FFE80E27A2F6F6B0777413097789A04897E34E563B14F84BCB0570170E239342DF3E22F855CA367D73040FBF4C805E545B9E3DB76EDD7BD38BFE6346DD5B2A49CF5DEAB63B76373E28BBE555C64D2CCD9F5BF369EAD05E90550C556BF53A4D654C77AB0CC8399011BECDF088B0785075C300D33EF06D3BC8AA092653D69A9783F567D0555CE7725534899F31C5B51EFF0BC587DB49CCE9C26137AE1E3C03F19EB7307E61BC05C6A701DA123E7D645531F5C5494698FE9203DF2FC77034778CB668F992E72E44433A0FF5E50F1A529F7EAF5FDF488463BE1C2BB01B4211AB8356D656274F8F0151BBA25DC160A73C0F186341BC93469183AC23260BE7C6BCABB88262FAFC8E69CF5B8A4C5936B63B3FC013F74E3DA9467CB12AE87BE01B1F6B0D308BAF7067F60B787A913DAFE0E769A7A30BBF6C2B8DD5880BE43F5A30BA62DA223201FB28451A9756E53F8C1DFBC90C80D35CBFEC26EA1FB9AD409845E265255C71B765F6348B9F25BD0F082604624AF2E08AAAF5EDD0311CFE756C7161BC01E15721B6319897C58E5EFAE66DF5E10F0F3B427DDFDD1E7F18FA02AB5BBAE071EC370731F9DAE9629DDE547B85E35374DCAFD4155911271DDBCC17C81561BE967F9D7F52567E3BF9DEB387BFE75B173E9B0F4BE3F977301E6AA7C239314B3911A793DFA05EBC42A193B86C0A388EBD8688510B880341E9E0F2C5303508B5ECC114EED2C930C7F37A97D433CC7D6F470FD4144463A56A0C2FC2F192893F8E6C497C03CDC4EB87287B864E9D6CE3204E52F4176ACFDE69F90C123B8820732423BB71782A8A2E5CC2D590C1E51B3AD6D40987FBD7BFFC9292517DEE72ECBC06A33A1E41EE9676F71A975D723782DC0E73AC7295152A823090250DDAF6541A834E050A3687F748828E3FD16A0902D18B3D1926BAB2B8A2DE7B66C7D33EBE1412542BF4D7F3A8EC39D8D16941C98533904728906A6B24D79E9F66FBD41F22B4FC67256825C374E68CAA542A23B73F1C244504FF968234D3439D5151EBCA8D8C4B0946E02785DB70BFA884CDDDE356344F10433EF4DE093E1F4C45BC37C8AE2C61C3F1E645186121CA93EA2FCAFDA5AF35E3F5AF40F208923CFFD126C72E77DC11DF240D72FE1198F9B792B21EFB042CEB678BB7636F834A0CD2FC55926DD9423916FF7AB5AF12EE96BB7168A7D6D77018356E40635073A577193D056B7FDB4A9D1573224EF62072C09D91613B2DD0D312901DE9FA23068714ED1B3867E3CA8BFC61F91F0FF39CFD503D082DF96531A3D85B8A44D3266BD12C2445C2AF6EBE8025C159974384F8D08446AB8DEA2F6E166CC95BD98D89D71F8F6AE297C69FA31134F9D53DEBACA307D985FB8B4327E52EB826B8FBCB90A0B603BEA1AA93F927C210D415747AA0259A6122A57F78CCEB3DC33E91307EF0A03E358BC1A36C7F2EC4109D62BAB43A1A1AF8725C50F3B2B2DD4C4E2D8874FFDEEC0B2322E21A6A270CFBA26124B4D630CAD2F79DCF478490FF0EF2509C899C105FB13C126E3901BC22D535754AF83B73ECE26CA205454CA677C599D6FC695696DDA52683600EDDDABC3FA34557556B9A14963EB5015B0CD8052EBBBB9657D771994AD8E96874F6518DEC2F59E82FF4F2442737F665101216C2248E1DD2A2E8D4AFA81890913927D68EB59C8896EED2C3D659144C6B653DB04EA88477A0AB4551EDE710BF94AF7E9887A6D74D61E1335E55AD75C09027AB8C91585ED3DBDB9FE1E7B1508AB69585F66192EC17AAF87CEADEC1AFA069B87C6F95F79B372400D4CE2282705BDBBF8CDCA481023DFCB84210B2E574C98EF9BB90535B4A94F5BF8F6798DF17D82E2E0FD4E59C12FA0DA3F278BFDBE41533E1EC908D5CADAC87B6CE4F8C0BF4E20D03796530BDF8C0E9A34A5AB6B471C1A9F5F08A20716EB6C4ADEE9BFF89E6F82E6EEB3971CF81677C1D5EBCC51E2C92CAF20651C4886B999F9406FC53E4A4B630C534C7B9114E1A63B138184C4D69035208312B146FF3DF63F0ACDBB61F81D2089FB7EF0373E04906FB932285068B441BB993DE06B0A895492280DF4864BE6A8F5C7C0D3EECF8FC3CB8A73B702F10387EBA1B1998D0D06F75D7DA1C409DF6CCDDE15F8D5764DEE276EE99733F25992A98216A198F90C7E7FE3C0250F5D479C21F58AEDD1E35323F1EE76B60DE58F98FAA0FAE20967B672269D20061C4EC59B6FE61C1D561045135FFB9085F79BC6770D70B4D21534ADE9B176669A72EF316D88179B2C0E07F42C0D53A7812466B8660A09E626437801152D68F4303E6C6962503A303FD913DF64E6885FA1DBE3AD5863DA1ED85183D05A5BD1F0B40F8F8792EF6248B45AFA316228FE43DCBF08DA6B4BF6A8909BDC0CD5716F316155057DF950166E6634FBA99BBDB5B42F4CB5B94B5B2285B48F1B17643F562ED09DDF73F608E00D30C94BA70FAE47CA40ECD9ABC35B68AE5AF53232BB4D082B7C9966B7CD7E932C3566B23AEF5A3D3F81AAF950483D52264B9298F89E68F12DE8126E9F2CEDD56B5EF4E57133E580610A76EF0789ECE90EC1C76BAB83E62CBC28FF7A43D0E22CB48E3FB2A5E83CE5213A5D402ABD6D8784EA9EA73C32007BA8A3F4F70B5B3472CCCAD676C9F263F67D5A3858688A4537A23EC0C7AE12E0835019259BA867C24BDF4A69B49267C8726951A034D09FE4E3CCA82653F0B255E1599F88DE7C651B0C1471D95F1B1A81543B6B6BF67440111C8B72D86EBEB4EE2FDF8D9C4CC474468FF116C1A9CAC82FE15850BB7541CFA61F7B537596AA47757BC15F0750FAA4D5069AD63B94E9CD41CA73F54C6F37BE9CDB8BE2992BB101405B795E19254DAC9FAEB955810320CA45E6393698C269DD29B1ACDB69B9383E9A6E440935A78C12F13CE6D22B9289B22D1D8D803DE532A004C062DF6B87E4D314D2C9AE0BBD52C80FFEFBAB6B1C3241D1F04A53F948EDB7F239BEC4DFEEAED5333898C5F9FD275B54FDCB1BBDCF7F00E79215D56942F50C1127E94083E47DB5AA64A4E02978745A24E191B1FA0C152C29072F6825A39F872EF8F891DDD9661D5610CF921885EC079067D43D0D6E32D1B285B6AFE0222B36AD2FFFDE25C647F9BC6E6992A82C2057BC483AF5FE9811622AE1F9DD0CD10DC193CDA6F2023735D40E979D6AAB2147630C10079DA8CE5D2778AAF088ABFC326C0B44EF939DA750AC83AE0E118FC1D5164BF82B9A2F19F22403102BA49E6DCB7472F3747CAAE98B8D7114F4DCA359AB8359064A7AEAE3F035AD4BD9C7BBEDD544EE1500F70D051895F2317D36EDF452287E4DD96037FFFC51320F7334140D759B470A4302928187E943EC286F285C48C5E051D7F63531518C2D6476B893AC53A52C04B0504C2D543EAD7ADC8E2A1C1C0A8681D9226D83AD2AE70315A12CE20D46202CE6B286593A3D97754E94FA25E9D4D738DFDA82CC9EA655E51DD101A89D15706CDFC02533861B711971A77DAF79E2C9903E84E62F1B32302D8D21512399EF8854CE9E8659712DFE70A80CFFC4174E85D0EF37EF81BC9F099C79C2413054B25C96A55F4FA9F92BBCD126151F851F028D671B7A550A73887D4F5EF408A4E17A057E6A5F8BD7A0404ADA262F36078E31B3E8C1B110F23684BD4C4E6A24AB21EAEE955430D1031FB6AB70CDE74E638F752DD615DA4D97C52A07D67FE893AE769B50B4C7F2B56F5381EC291833B9D3C09E12AFDC815D6D0A59032D406A8EBCB75493E8B5A909D6E8A19DD5DB8C2B2B93A84B7A6438B92F7B6158E30FD23102DDBF1303869C0FF036815E9C0D09FC5FEB97D3CAD3AE623FFF8B01752C0500F356439B9C28EBD04C4FAB5F3490609DF2526B6DB6A2E7A45C28DA91AB1B997D3904829418326055D774CA8BE7C1E8AE065534B0A9747FB6D46540CA5731B1CADA30E5E2C6356D35948DC79708461B170227C2D7A67662B0C1EFE84543068E763FC9D0714C25387E46E1EECDC1C49573492102E4D722C91BAA05EA5AA905F260AA26A72FF636FFC814138FE68FAAB3903C76716402357637F8502B193ACF42B283E54560C293154AACDFEEB00348E7F16DC5A875875FF2B7E294BFA633F61ACED571DC695935C667E1321D19E9760D7C48300004A46891743B25D761705ABBC67D826716424BF6AA375F458AB6A49973F17283923B05DD1F6319FCA109911138B5A39DC287887C05159536E4B6DFEB07C7E670228CADAFB6A34122EC9F45F4184D26C192C5F814E280472DF92BB31D1CDE81A0518C15A194496AC57BE31C7AB6B09AFE9C5E3A0CC89908E570A6E96234BD3F9A5C1B1D72251F3E0B778D84C86857FF74D153D36F7000C13A2DCA020EBE8BAFDDCD6E00678C6E4346A5EE917BA9EA99CFA4E5B0C17E1F7D40EE18EE7D614A90B596FF9783A250C293D78E7A9F22505C7E93D309C629C48A0".toLowerCase();
		String keyContent = "1234567";
		byte[] raw = Hex.unhex(serverIdContent);
		ByteArrayInputStream in = new ByteArrayInputStream(raw);
		IBECapsule capsule = new IBECapsuleAESImpl();
		capsule.setKey(keyContent.getBytes());
		IdentityDescription id = null;
		try {
			capsule.readExternal(in);
			id = (IdentityDescription) capsule.getDataAsObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		serverPrivateKey = id == null ? null : id.getPrivateKey();
		serverCertificate = id == null ? null : id.getCertificate();
	}

	public IBEPrivateKey serverPrivateKey() {
		if (serverPrivateKey != null)
			return serverPrivateKey;
		prepareServerKeys();
		return serverPrivateKey;
	}

	public IBSCertificate serverCertificate() {
		if (serverCertificate != null)
			return serverCertificate;
		prepareServerKeys();
		return serverCertificate;
	}

//	public static void main(String[] args) {
//		// export database
//		org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.AnnotationConfiguration().configure();
//		org.hibernate.tool.hbm2ddl.SchemaExport export = new org.hibernate.tool.hbm2ddl.SchemaExport(cfg);
//		export.create(true, true);
//	}

	/**
	 * 包含数组编码 信息摘要的工具类
	 * @author <a href="mailto:wangyeee@gmail.com">Wang Ye</a>
	 */
	public static class Util {
		private Util() {
		}

		public final String format(Date date) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd#HH:mm:ss");
			return format.format(date);
		}
	}
}
