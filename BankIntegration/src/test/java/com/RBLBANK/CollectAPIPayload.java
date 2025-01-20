package com.RBLBANK;

import java.io.IOException;
import java.io.StringReader;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import genricLibraries.PropertiesUtility;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CollectAPIPayload {
	
	@Test
	public void executeCollectsForDesiredNum() throws ParserConfigurationException, SAXException, IOException {
		
		int maxCollects = 10;
		
		for(int execute = 1;execute <= maxCollects;execute++) {
			upiCollectPayload();
		}
	}

	@Test
	public void upiCollectPayload() throws ParserConfigurationException, SAXException, IOException {
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
		
		
		Response response = apiCall(collectPayload);
		
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
	
	public Response apiCall(String body) {
		Response response = RestAssured.given()
				.contentType(ContentType.XML)
				.accept(ContentType.XML)
				.queryParam("client_id", "c6ecdf5ce50dab3b8489057b843c0358")
			    .queryParam("client_secret", "6697fe4dd8e974347db9099b3f0a42ec")
				.auth().basic("MYGRNDZONE", "Welcome@123")
				.body(body)
				.baseUri("https://apideveloper.rblbank.com/test/sb/rbl/v1/upi/collectons")
				.when().post()
				.then().extract().response();
		
		return response;
	}
}
