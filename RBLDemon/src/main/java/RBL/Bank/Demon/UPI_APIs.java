package RBL.Bank.Demon;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import genricLibraries.PropertiesUtility;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UPI_APIs {
	
//	Credentials
	
	private String CLIENT_ID = "c6ecdf5ce50dab3b8489057b843c0358";
	
	private String CLIENT_SECRET = "6697fe4dd8e974347db9099b3f0a42ec";
	
	private String USER_NAME = "MYGRNDZONE";
	
	private String PASS_WORD = "Welcome@123";
	
	private String CORP_ID = "MYGRNDZONE";
	
	private String BASE_URL = "https://apideveloper.rblbank.com";
	
//	Bank Account details
	
	private String ACC_NO = "409000145322";
	
	private String ACC_NAME = "TEJU MAHTO";
	
	private String IFSC = "RATN0000001";
	
	private String MODE = "IMPS";
	
	private String TRANID;
	
	private String STATUS_TRANID;
	
	private String STMT_TRANID;
	
//	public void executeCollectsForDesiredNum() throws ParserConfigurationException, SAXException, IOException {
//		
//		int maxCollects = 10;
//		
//		for(int execute = 1;execute <= maxCollects;execute++) {
//			upiCollectPayload();
//		}
//	}
	
	public void sessionTokenGenerator(String txnType) {
		
		String path = null;
		if(txnType.equals("D")) {
			path = "/test/sb/rbl/api/v1/upi/payment";
		}else if(txnType.equals("C")) {
			path = "/test/sb/rbl/v1/upi/collectons";
		}else {
			System.out.println("Un-Know txn type");
			return;
		}

		String url = BASE_URL + path;
		
		String requestPayload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
	             "<channelpartnerloginreq>\n" +
	             "  <username>MYGROUND</username>\n" +
	             "  <password>7DA433772D8272D169386823D4387CCDBE1234D7</password>\n" +
	             "  <bcagent>MYG2171126</bcagent>\n" +
	             "</channelpartnerloginreq>";

		System.out.println("\nRequest Payload--> " + requestPayload);

		System.out.println("\nURL---> " + url);

		Response response = null;
		String initiatedTSString = null;
		String responseTSString = null;
		long delay = 0;
		try {
			response = invokeUniRequest(url, requestPayload);

			// update the response in the data base
			System.out.println("\nresponse--> " + response);

			// Parse the XML string into a DOM Document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(response.body().asPrettyString()));
			Document xmlBody = builder.parse(inputSource);

			if (response.statusCode() == 200) {
				Element rootNode = xmlBody.getDocumentElement();

				String status = rootNode.getElementsByTagName("status").item(0).getTextContent();
				String descroption = rootNode.getElementsByTagName("description").item(0).getTextContent();
				String txnId = rootNode.getElementsByTagName("txnid").item(0).getTextContent();

			} else {
				System.out.println("Error: " + response.statusCode() + "\n" + response.body().asPrettyString());
			}

		} catch (Exception e) {
			System.err.println("ERROR: " + response + " " + e.getMessage());
		}

	}
	
	public void generateTxnID(String txnType) {
		String path = null;
		if(txnType.equals("D")) {
			path = "/test/sb/rbl/api/v1/upi/payment";
		}else if(txnType.equals("C")) {
			path = "/test/sb/rbl/v1/upi/collectons";
		}else {
			System.out.println("Un-Know txn type");
			return;
		}

		String url = BASE_URL + path;
		
		String requestPayload = "<gettxnid>\n" +
	             "  <header>\n" +
	             "    <sessiontoken>7DA433772D8272D169386823D4387CCDBE1234D7C1D8DD109D5749075D1BB6D385AC31E449909E51ACC664187C95CEE6868100CA6C3AC40E0630097D15EC6C99F41593B8</sessiontoken>\n" +
	             "    <bcagent>MYG2171126</bcagent>\n" +
	             "  </header>\n" +
	             "  <mrchOrgId>MYGROUND11</mrchOrgId>\n" +
	             "  <aggrOrgId>MYGROUND11</aggrOrgId>\n" +
	             "  <mobile/>\n" +
	             "  <geocode/>\n" +
	             "  <location/>\n" +
	             "  <ip/>\n" +
	             "  <type> </type>\n" +
	             "  <id/>\n" +
	             "  <os> </os>\n" +
	             "  <app> </app>\n" +
	             "  <capability/>\n" +
	             "  <hmac>7698298E-3831-4E6B-890E76B8B7968A</hmac>\n" +
	             "</gettxnid>";

		System.out.println("\nRequest Payload--> " + requestPayload);

		System.out.println("\nURL---> " + url);

		Response response = null;
		String initiatedTSString = null;
		String responseTSString = null;
		long delay = 0;
		try {
			response = invokeUniRequest(url, requestPayload);

			// update the response in the data base
			System.out.println("\nresponse--> " + response);

			// Parse the XML string into a DOM Document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(response.body().asPrettyString()));
			Document xmlBody = builder.parse(inputSource);

			if (response.statusCode() == 200) {
				Element rootNode = xmlBody.getDocumentElement();

				String status = rootNode.getElementsByTagName("status").item(0).getTextContent();
				String descroption = rootNode.getElementsByTagName("description").item(0).getTextContent();
				String txnId = rootNode.getElementsByTagName("txnid").item(0).getTextContent();

			} else {
				System.out.println("Error: " + response.statusCode() + "\n" + response.body().asPrettyString());
			}

		} catch (Exception e) {
			System.err.println("ERROR: " + response + " " + e.getMessage());
		}
	}
	
	public void validateVpa(String txnType) {
		String path = null;
		if(txnType.equals("D")) {
			path = "/test/sb/rbl/api/v1/upi/payment";
		}else if(txnType.equals("C")) {
			path = "/test/sb/rbl/v1/upi/collectons";
		}else {
			System.out.println("Un-Know txn type");
			return;
		}

		String url = BASE_URL + path;
		
		String requestPayload = "<validatevpa>\n" +
	             "  <header>\n" +
	             "    <sessiontoken>7DA433772D8272D169386823D4387CCDBE1234D7C1D8DD109D5749075D1BB6D385AC31E43C712D06E8086726E13E11EF6116BB546C3AC40E0630097D15EC6C99F41593B8</sessiontoken>\n" +
	             "    <bcagent>MYG2171126</bcagent>\n" +
	             "  </header>\n" +
	             "  <mrchOrgId>MYGROUND11</mrchOrgId>\n" +
	             "  <aggrOrgId>MYGROUND11</aggrOrgId>\n" +
	             "  <note>Testing</note>\n" +
	             "  <refId>1</refId>\n" +
	             "  <orgTxnId>GURU0010</orgTxnId>\n" +
	             "  <refUrl></refUrl>\n" +
	             "  <mobile>9945451343</mobile>\n" +
	             "  <geocode>28.3594007,77.0339989</geocode>\n" +
	             "  <location>Gurgaon</location>\n" +
	             "  <ip>192.168.1.2</ip>\n" +
	             "  <type></type>\n" +
	             "  <id>1</id>\n" +
	             "  <os>Android</os>\n" +
	             "  <app>com.rblbank.mobank</app>\n" +
	             "  <capability></capability>\n" +
	             "  <hmac>E8136957-9981-424F-8FF4-CBC3B3A4A0</hmac>\n" +
	             "  <addr>PAYMENT.MYGROUND11@rbl</addr>\n" +
	             "</validatevpa>";

		System.out.println("\nRequest Payload--> " + requestPayload);

		System.out.println("\nURL---> " + url);

		Response response = null;
		String initiatedTSString = null;
		String responseTSString = null;
		long delay = 0;
		try {
			response = invokeUniRequest(url, requestPayload);

			// update the response in the data base
			System.out.println("\nresponse--> " + response);

			// Parse the XML string into a DOM Document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(response.body().asPrettyString()));
			Document xmlBody = builder.parse(inputSource);

			if (response.statusCode() == 200) {
				Element rootNode = xmlBody.getDocumentElement();

				String status = rootNode.getElementsByTagName("status").item(0).getTextContent();
				String descroption = rootNode.getElementsByTagName("description").item(0).getTextContent();
				String txnId = rootNode.getElementsByTagName("txnid").item(0).getTextContent();

			} else {
				System.out.println("Error: " + response.statusCode() + "\n" + response.body().asPrettyString());
			}

		} catch (Exception e) {
			System.err.println("ERROR: " + response + " " + e.getMessage());
		}
	}

	public void txnInquiry(String txnType) {
		String path = null;
		if(txnType.equals("D")) {
			path = "/test/sb/rbl/api/v1/upi/payment";
		}else if(txnType.equals("C")) {
			path = "/test/sb/rbl/v1/upi/collectons";
		}else {
			System.out.println("Un-Know txn type");
			return;
		}

		String url = BASE_URL + path;
		
		String requestPayload = "<searchrequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
	             "  <header>\n" +
	             "    <sessiontoken>7DA433772D8272D169386823D4387CCDBE1234D7C1D8DD109D5749072B89D6F7669B9ACF5802C9999A086EBA793D6763B2E6B3FC6C3AC40E0630097D15EC6C99F41593B8</sessiontoken>\n" +
	             "    <bcagent>MYG2171126</bcagent>\n" +
	             "  </header>\n" +
	             "  <mrchOrgId>MYGROUND11</mrchOrgId>\n" +
	             "  <aggrOrgId>MYGROUND11</aggrOrgId>\n" +
	             "  <mobile>8623059774</mobile>\n" +
	             "  <geocode>27.8260346,77.8784359</geocode>\n" +
	             "  <location>Gonda, Iglas, Aligarh, Uttar Pradesh, 202123, India</location>\n" +
	             "  <ip>10.80.93.26</ip>\n" +
	             "  <type>mob</type>\n" +
	             "  <id>17235258527459952</id>\n" +
	             "  <os>android</os>\n" +
	             "  <app>Collection</app>\n" +
	             "  <capability>100</capability>\n" +
	             "  <hmac>15432BE1-3D7F-425D-810A-5E8ADFFDCE</hmac>\n" +
	             "  <orgTxnIdorrefId>RPA0010025027143949449B502701005021</orgTxnIdorrefId>\n" +
	             "  <flag>0</flag>\n" +
	             "</searchrequest>";

		System.out.println("\nRequest Payload--> " + requestPayload);

		System.out.println("\nURL---> " + url);

		Response response = null;
		String initiatedTSString = null;
		String responseTSString = null;
		long delay = 0;
		try {
			response = invokeUniRequest(url, requestPayload);

			// update the response in the data base
			System.out.println("\nresponse--> " + response);

			// Parse the XML string into a DOM Document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(response.body().asPrettyString()));
			Document xmlBody = builder.parse(inputSource);

			if (response.statusCode() == 200) {
				Element rootNode = xmlBody.getDocumentElement();

				String status = rootNode.getElementsByTagName("status").item(0).getTextContent();
				String descroption = rootNode.getElementsByTagName("description").item(0).getTextContent();
				String txnId = rootNode.getElementsByTagName("txnid").item(0).getTextContent();

			} else {
				System.out.println("Error: " + response.statusCode() + "\n" + response.body().asPrettyString());
			}

		} catch (Exception e) {
			System.err.println("ERROR: " + response + " " + e.getMessage());
		}
	}
	
	/**
	 * This method is use to do upi collects
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void upiCollectPayload() throws ParserConfigurationException, SAXException, IOException {
		String url = BASE_URL+"/test/sb/rbl/v1/upi/collectons";
		String propertyPath = "./src/test/resources/bankIntg.properties";
		
		//Accessing the property file to fetch the data
		PropertiesUtility properties = new PropertiesUtility();
		properties.propertiesInit(propertyPath);
		int num = Integer.parseInt(properties.readData("num"));
		
		//Updated txnID with unique number
		String reqTxnId = "MYGROUND11017164350447B50170100"+num;
		
		//Updating the number in property file
		properties.writeToProperties("num", String.valueOf(++num), propertyPath);
		
		//Generating the random number for amoutn field
		Random random = new Random();
		int amt = random.nextInt(10)+1;
		
		String collectPayload = "<collectrequest  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\r\n"
				+ "   <header>\r\n"
				+ "        <sessiontoken>7DA433772D8272D169386823D4387CCDBE1234D7C1D8DD109D5749072B89D6F7669B9ACF5802C9999A086EBA793D6763B2E6B3FC6C3AC40E0630097D15EC6C99F41593B8</sessiontoken>\r\n"
				+ "        <bcagent>MYG2171126</bcagent>\r\n"
				+ "    </header>\r\n"
				+ "    <mrchOrgId>MYGROUND11</mrchOrgId>\r\n"
				+ "    <aggrOrgId>MYGROUND11</aggrOrgId>\r\n"
				+ "    <note>CC Collect</note>\r\n"
				+ "    <validupto/>\r\n"
				+ "    <refId>"+reqTxnId+"</refId>\r\n"
				+ "    <refUrl/>\r\n"
				+ "    <orgTxnId>05012024000005</orgTxnId>\r\n"
				+ "    <txnId>"+reqTxnId+"</txnId>\r\n"
				+ "    <mobile>8623059774</mobile>\r\n"
				+ "    <geocode>27.8260346,77.8784359</geocode>\r\n"
				+ "    <location>Gonda, Iglas, Aligarh, Uttar Pradesh, 202123, India</location>\r\n"
				+ "    <ip>10.80.93.26</ip>\r\n"
				+ "    <type>mob</type>\r\n"
				+ "    <id>17235258527459952</id>\r\n"
				+ "    <os>android</os>\r\n"
				+ "    <app>Collection</app>\r\n"
				+ "    <capability>100</capability>\r\n"
				+ "    <hmac>15432BE1-3D7F-425D-810A-5E8ADFFDCE</hmac>\r\n"
				+ "    <payeraddress>kuldeep@rbl</payeraddress>\r\n"
				+ "    <payername>Kuldeep</payername>\r\n"
				+ "    <payeeaddress>PAYMENT.MYGROUND11@rbl</payeeaddress>\r\n"
				+ "    <payeename>MYGROUND11</payeename>\r\n"
				+ "    <amount>"+amt+"</amount>\r\n"
				+ "</collectrequest>";
		
		
		Response response = invokeUniRequest(url,collectPayload);
		
//		System.out.println(response.body().asPrettyString());
		// Parse the XML string into a DOM Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(response.body().asPrettyString()));
        Document xmlBody = builder.parse(inputSource);
        
        if(response.statusCode()==200) {
        	Element rootNode = xmlBody.getDocumentElement();
        	
        	String status = rootNode.getElementsByTagName("status").item(0).getTextContent();
        	String descroption = rootNode.getElementsByTagName("description").item(0).getTextContent();
        	String txnId = rootNode.getElementsByTagName("txnid").item(0).getTextContent();
        	
        	System.out.println("TxnId: "+txnId+" amt: "+amt);
        }else {
        	System.out.println("Error: "+response.statusCode()+"\n"+response.body().asPrettyString());
        }
		
	}
	
	/**
	 * This method is used to do upi payout
	 * @return 
	 */
	public Map<String, String> upiPayout() {
		String url = BASE_URL + "/test/sb/rbl/api/v1/upi/payment";
		Map<String,String> payoutDetails = new HashMap<String, String>();
		
		String requestPayload = "<upidisbursement>\n" +
	             "  <header>\n" +
	             "    <sessiontoken>7DA433772D8272D169386823D4387CCDBE1234D7C1D8DD109D5749070B38C7F2A4F54EAC1F695214CD745FDEEBB432A3B19380C56C3AC40E0630097D15EC6C99F41593B8</sessiontoken>\n" +
	             "    <bcagent>MYG2171126</bcagent>\n" +
	             "  </header>\n" +
	             "  <mrchOrgId>MYGROUND11</mrchOrgId>\n" +
	             "  <aggrOrgId>MYGROUND11</aggrOrgId>\n" +
	             "  <note>Upipayment</note>\n" +
	             "  <refId>MYGROUND130271527054B00502701005030</refId>\n" +
	             "  <refUrl>http://rblbank.com</refUrl>\n" +
	             "  <orgTxnId>05012024000005</orgTxnId>\n" +
	             "  <txnId>MYGROUND120271527054B00502701005030</txnId>\n" +
	             "  <mobile>9876251297</mobile>\n" +
	             "  <geocode>28.644800,77.216721</geocode>\n" +
	             "  <location>delhi</location>\n" +
	             "  <ip>192.168.0.183</ip>\n" +
	             "  <type>MOB</type>\n" +
	             "  <id></id>\n" +
	             "  <os>IOS</os>\n" +
	             "  <app></app>\n" +
	             "  <capability>100</capability>\n" +
	             "  <hmac>79996CB2-584E-4694-93E7-BEFDED3DE5</hmac>\n" +
	             "  <payeraddress>PAYMENT.MYGROUND11@rbl</payeraddress>\n" +
	             "  <payername>MYGROUND11</payername>\n" +
	             "  <payeeaddress>kuldeep@rbls</payeeaddress>\n" +
	             "  <payeename>Kuldeep</payeename>\n" +
	             "  <amount>1.00</amount>\n" +
	             "</upidisbursement>";
		
		System.out.println("\nRequest Payload--> " + requestPayload);

		System.out.println("\nURL---> " + url);

		Response response = null;
		String initiatedTSString = null;
		String responseTSString = null;
		long delay = 0;
		try {
			// Date Formats
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			// Initiated time
			initiatedTSString = LocalDateTime.now().format(format);
			LocalDateTime initiatedTS = LocalDateTime.parse(initiatedTSString, format);
			System.out.println("\nInitiated_timestamp: " + initiatedTSString + "\n");

			response = invokeUniRequest(url, requestPayload);

			// Response Time
			responseTSString = LocalDateTime.now().format(format);
			LocalDateTime responseTS = LocalDateTime.parse(responseTSString, format);
			System.out.println("\nResponse_timestamp: " + responseTSString);

			// Updated the initiated time and response time with delay into DB
			delay = ChronoUnit.SECONDS.between(initiatedTS, responseTS);

			// update the response in the data base
			System.out.println("\nresponse--> " + response);
			
			// Parse the XML string into a DOM Document
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        InputSource inputSource = new InputSource(new StringReader(response.body().asPrettyString()));
	        Document xmlBody = builder.parse(inputSource);

	        if(response.statusCode()==200) {
	        	Element rootNode = xmlBody.getDocumentElement();
	        	
	        	String status = rootNode.getElementsByTagName("status").item(0).getTextContent();
	        	String descroption = rootNode.getElementsByTagName("description").item(0).getTextContent();
	        	String txnId = rootNode.getElementsByTagName("txnid").item(0).getTextContent();
	        	
	        }else {
	        	System.out.println("Error: "+response.statusCode()+"\n"+response.body().asPrettyString());
	        }

		} catch (Exception e) {
			System.err.println("ERROR: " + response + " " + e.getMessage());
		}

		System.out.println("\n***************************IMPS payout ended********************************");
		return payoutDetails;
	}
	
	public Response invokeUniRequest(String url,String body) {
		Response response = RestAssured.given()
				.contentType(ContentType.XML)
				.accept(ContentType.XML)
				.queryParam("client_id", CLIENT_ID)
			    .queryParam("client_secret", CLIENT_SECRET)
				.auth().basic(USER_NAME, PASS_WORD)
				.body(body)
				.baseUri(url)
				.when().post()
				.then().extract().response();
		
		return response;
	}
}
