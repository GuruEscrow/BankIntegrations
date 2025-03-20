package com.Axis.uat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import genricLibraries.PropertiesUtility;
import kong.unirest.Unirest;

public class VPA_Api {

    private final String BASE_URL = "https://sakshamuat.axisbank.co.in";
    
    private final String CLIENT_ID ="5675712fc414382532a47bc813f3522b"; 
    
    private final String CLIENT_SECRET ="dfb9920a52382605a4a639378154bbf8"; // "c04c917876a48481d56eabb336c28747"
    
    // Credentails
    private final String MERCH_ID = "DEMOUPI1310036556";//DEMOUPI1310036556
    
    private final String MERCH_CHANNELID = "DEMOUPI13APP1003655720603";//DEMOUPI13APP1003655720603
    
    private final String CORPCODE = "DEMOUPI13";
    
	public String vpa_Verification(String mobNum) throws Exception {
		String url = BASE_URL+"/gateway/api/txb/v1/acct-recon/verifyVPA";
		
		String propertyPath = "./src/test/resources/bankIntg.properties";
		
		//Accessing the property file to fetch the data
		PropertiesUtility properties = new PropertiesUtility();
		properties.propertiesInit(propertyPath);
		int num = Integer.parseInt(properties.readData("axis"));
		
		//Sub header values
		String requestUUID = "ESCROWUPI0"+num;
		String serviceRequestId = "OpenAPI";
		String serviceRequestVersion = "1.0";
		String channelID = "ESROWSTACK";
		
		//Updating the number in property file
		properties.writeToProperties("axis", String.valueOf(++num), propertyPath);
		
		LinkedHashMap<String, Object> requestMap = new LinkedHashMap<String, Object>();
		requestMap.put("merchId", MERCH_ID);
		requestMap.put("merchChanId", MERCH_CHANNELID);
		requestMap.put("customerVpa", mobNum);
		requestMap.put("corpCode", CORPCODE);
		requestMap.put("channelId", channelID);
//		requestMap.put("failedtest", "failedTest");
		
		System.out.println("\nchecksum json map values -->"+requestMap);
		
		ChecksumUtility checksumUtility = new ChecksumUtility();
		String checkSum =  checksumUtility.generateCheckSum(requestMap);
		
		
		String Data_To_Encrypt = "{\n" +
			    "            \"merchId\": \""+MERCH_ID+"\",\n" +
			    "            \"merchChanId\": \""+MERCH_CHANNELID+"\",\n" +
			    "            \"customerVpa\": \""+mobNum+"\",\n" +
			    "            \"corpCode\": \""+CORPCODE+"\",\n" +
			    "            \"channelId\": \""+channelID+"\",\n" +
			    "            \"checksum\": \""+checkSum+"\"\n" +
			    "        }";
		
		String Req_Payload = "{\n" +
			    "    \"VerifyVPARequest\": {\n" +
			    "        \"SubHeader\": {\n" +
			    "            \"requestUUID\": \""+requestUUID+"\",\n" +
			    "            \"serviceRequestId\": \""+serviceRequestId+"\",\n" +
			    "            \"serviceRequestVersion\": \""+serviceRequestVersion+"\",\n" +
			    "            \"channelId\": \""+channelID+"\"\n" +
			    "        },\n" +
			    "        \"VerifyVPARequestBodyEncrypted\": \"%s\"\n" +
			    "    }\n" +
			    "}";
		
		String Plain_Payload = "{\n" +
			    "    \"VerifyVPARequest\": {\n" +
			    "        \"SubHeader\": {\n" +
			    "            \"requestUUID\": \""+requestUUID+"\",\n" +
			    "            \"serviceRequestId\": \""+serviceRequestId+"\",\n" +
			    "            \"serviceRequestVersion\": \""+serviceRequestVersion+"\",\n" +
			    "            \"channelId\": \""+channelID+"\"\n" +
			    "        },\n" +
			    "        \"VerifyVPARequestBody\": {\n" +
			    "            \"merchId\": \""+MERCH_ID+"\",\n" +
			    "            \"merchChanId\": \""+MERCH_CHANNELID+"\",\n" +
			    "            \"customerVpa\": \""+mobNum+"\",\n" +
			    "            \"corpCode\": \""+CORPCODE+"\",\n" +
			    "            \"channelId\": \""+channelID+"\",\n" +
			    "            \"checksum\": \""+checkSum+"\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}";
		
		System.out.println("Data to Encrypt--> "+Data_To_Encrypt);
		System.out.println("\nPlain Payload--> "+Plain_Payload);
		Encrypt_DecryptUtility axisUtility = new Encrypt_DecryptUtility();
		String encrypted = axisUtility.aes128Encrypt(Data_To_Encrypt);
		
		String payload = String.format(Req_Payload, encrypted);
		
		System.out.println("\nEncrypted payload--> "+payload);
		
		System.out.println("\nURL---> "+ url);
		String response = invokeUniRequest(url, payload);
		
		return response;
		
}
	
	/**
	 * Method responsible for calling the specificed API and return the API response
	 * @throws KeyStoreException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 * @throws KeyManagementException 
	 */
	private String invokeUniRequest(String url, String payload) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
		
		String pfxFilePath = "./src/main/resources/AxisUAT.p12"; // Ensure correct path
		
		Security.setProperty("jdk.tls.client.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        File pfxFile = new File(pfxFilePath);
        if (!pfxFile.exists()) {
            throw new FileNotFoundException("PFX file not found at: " + pfxFile.getAbsolutePath());
        }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(pfxFile);
        keyStore.load(fis, new char[0]); // Change if password is needed
        fis.close();

        // Debugging: Check if keyStore has loaded any certificates
//        System.out.println("Keystore contains: " + keyStore.size() + " entries");

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, new char[0]); // No password

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                sslContext, NoopHostnameVerifier.INSTANCE // Use default verification if needed
        );

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();

        Unirest.config().httpClient(httpClient);
		
		kong.unirest.HttpResponse<String> response = Unirest.post(url)
				.header("Content-Type", "application/json")
				.header("X-IBM-Client-Id ", CLIENT_ID)
				.header("X-IBM-Client-Secret", CLIENT_SECRET)
				.body(payload)
				.socketTimeout(1200000)
				.asString();
		System.out.println("Response code -->"+response.getStatus());
		return response.getBody();

	}
}
