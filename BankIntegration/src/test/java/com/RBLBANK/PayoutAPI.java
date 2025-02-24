package com.RBLBANK;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import genricLibraries.PropertiesUtility;
import kong.unirest.Unirest;

public class PayoutAPI {

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
	
    public Map<String, String> getStatements(String fromDate,String toDate,String tran_type) {
		
		String url = BASE_URL+"/test/sb/rbl/v1/cas/statement";
		
		//Updating the transaction ID
		updateGetStatmentTxnID();
		
		String requestPayload = "{\n" +
			    "    \"Acc_Stmt_DtRng_Req\": {\n" +
			    "        \"Header\": {\n" +
			    "            \"TranID\": \""+STMT_TRANID+"\",\n" +
			    "            \"Corp_ID\": \""+CORP_ID+"\",\n" +
			    "            \"Approver_ID\": \"A001\"\n" +
			    "        },\n" +
			    "        \"Body\": {\n" +
			    "            \"Acc_No\": \""+ACC_NO+"\",\n" +
			    "            \"Tran_Type\": \""+tran_type+"\",\n" +
			    "            \"From_Dt\": \""+fromDate+"\",\n" +  // 2020-08-01
			    "            \"Pagination_Details\": {\n" +
			    "                \"Last_Balance\": {\n" +
			    "                    \"Amount_Value\": \"\",\n" +
			    "                    \"Currency_Code\": \"\"\n" +
			    "                },\n" +
			    "                \"Last_Pstd_Date\": \"\",\n" +
			    "                \"Last_Txn_Date\": \"\",\n" +
			    "                \"Last_Txn_Id\": \"\",\n" +
			    "                \"Last_Txn_SrlNo\": \"\"\n" +
			    "            },\n" +
			    "            \"To_Dt\": \""+toDate+"\"\n" +  // 2021-08-10
			    "        },\n" +
			    "        \"Signature\": {\n" +
			    "            \"Signature\": \"Signature\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}";   
		
		System.out.println("\n***************************statement fetch started**********************************");
		System.out.println("\nRequest Payload--> "+requestPayload);
		
		System.out.println("\nURL---> "+ url);
		
		Map<String,String> stmtMap = new HashMap<String,String>();
		String response = null;
		try {
			
			response = invokeUniRequest(url, requestPayload);	
			
			JsonObject responseJsonParse = JsonParser.parseString(response).getAsJsonObject();
			if(responseJsonParse.has("Acc_Stmt_DtRng_Res")) {
				JsonArray statementArray = responseJsonParse.get("Acc_Stmt_DtRng_Res").getAsJsonObject().get("transactionDetails").getAsJsonArray();
				
				for (int stmts = 0; stmts < statementArray.size(); stmts++) {
					
					JsonObject jsonStmts = statementArray.get(stmts).getAsJsonObject();
					String txnDesc = jsonStmts.get("transactionSummary").getAsJsonObject().get("txnDesc").getAsString();
					if(txnDesc.contains("IMPS")) {
						String utr_rrn = txnDesc.split("-")[0].trim();
						if(!(stmtMap.containsKey(utr_rrn))) {
							stmtMap.put(utr_rrn, jsonStmts.toString());
						}else {
							String oldstm = stmtMap.get(utr_rrn);
							stmtMap.put(utr_rrn, oldstm+"\n"+jsonStmts.toString());
						}
					}
				}
			}
			
			//update the response in the data base
			
			//update the UTR in the data base
			
		}catch(Exception e) {
			//Update the error in the data bases
			
		}
		
		System.out.println("\n******************************statment fetch ended********************************");
		return stmtMap;
	}
	
	public Map<String, String> payouts(String amt,String beneIFSC,String beneAccNo,String beneName) throws ClassNotFoundException, SQLException {
		
		String url = BASE_URL+"/test/sb/rbl/v1/payments/corp/payment";
		Map<String,String> payoutDetails = new HashMap<String, String>();
		
		//Updating the transaction Id
		updateIMPSTxnID();
		
		String requestPayload = "{\n" +
			    "    \"Single_Payment_Corp_Req\": {\n" +
			    "        \"Header\": {\n" +
			    "            \"TranID\": \""+TRANID+"\",\n" +
			    "            \"Corp_ID\": \""+CORP_ID+"\",\n" +
			    "            \"Maker_ID\": \"M005\",\n" +
			    "            \"Checker_ID\": \"C003\",\n" +
			    "            \"Approver_ID\": \"A003\"\n" +
			    "        },\n" +
			    "        \"Body\": {\n" +
			    "            \"Amount\": \""+amt+"\",\n" +
			    "            \"Debit_Acct_No\": \""+ACC_NO+"\",\n" +
			    "            \"Debit_Acct_Name\": \""+ACC_NAME+"\",\n" + // Not mandatory
			    "            \"Debit_IFSC\": \""+IFSC+"\",\n" +
			    "            \"Debit_Mobile\": \"9856234589\",\n" +
			    "            \"Debit_TrnParticulars\": \"FARIDA\",\n" + // Not mandatory
			    "            \"Debit_PartTrnRmks\": \"SURESH\",\n" + // Not mandatory
			    "            \"Ben_IFSC\": \""+beneIFSC+"\",\n" +
			    "            \"Ben_Acct_No\": \""+beneAccNo+"\",\n" +
			    "            \"Ben_Name\": \""+beneName+"\",\n" +
			    "            \"Ben_Address\": \"MUMBAI\",\n" + // Not mandatory
			    "            \"Ben_BankName\": \"Bank\",\n" +
			    "            \"Ben_BankCd\": \"176\",\n" + // Not mandatory
			    "            \"Ben_BranchCd\": \"0123\",\n" + // Not mandatory
			    "            \"Ben_Email\": \"mail@gmail.com\",\n" + // Not mandatory
			    "            \"Ben_Mobile\": \"9895527234\",\n" + // Not mandatory
			    "            \"Ben_TrnParticulars\": \"VIBEESH_@123\",\n" + // Not mandatory
			    "            \"Ben_PartTrnRmks\": \"Testing\",\n" +
			    "            \"Issue_BranchCd\": \"0112\",\n" + // Not mandatory
			    "            \"Mode_of_Pay\": \""+MODE+"\",\n" +
			    "            \"Remarks\": \"PAYEMNT QUEUES\"\n" +
			    "        },\n" +
			    "        \"Signature\": {\n" +
			    "            \"Signature\": \"Signature\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}"; 
		
		System.out.println("\n**********************IMPS Payout started***********************************");
		System.out.println("\nRequest Payload--> "+requestPayload);
		
		System.out.println("\nURL---> "+ url);
		
		String response = null;
		String initiatedTSString = null;
		String responseTSString = null;
		long delay = 0;
		String utr_rrn = null;
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
			
			//update the response in the data base
			System.out.println("\nresponse--> "+response);
			JsonObject responseJosnParse = JsonParser.parseString(response).getAsJsonObject();
			
			if(responseJosnParse.has("Single_Payment_Corp_Resp")) {
				JsonObject responseBody =	responseJosnParse.get("Single_Payment_Corp_Resp").getAsJsonObject().get("Body").getAsJsonObject();
				
				if(MODE.equals("IMPS")) {
				   utr_rrn = responseBody.get("RRN").getAsString();
				}else {
					utr_rrn = responseBody.get("UTRNo").getAsString();	
				}
			
			}else {
				System.out.println("ERROR: other response then expected (UTR not generated)");
			}
	
		}catch(Exception e) {
			System.err.println("ERROR: "+response+" "+e.getMessage());
		}
		
		payoutDetails.put("tranID", TRANID);
		payoutDetails.put("utr_rrn", utr_rrn);
		System.out.println("\n***************************IMPS payout ended********************************");
		return payoutDetails;
	}
	
	public String imps_payout_StatusChecker(String rrn,String transID) throws ClassNotFoundException, SQLException {
		
		String url = BASE_URL+"/test/sb/rbl/v1/payments/corp/payment/query";
		
		//Updating the transaction ID
		updateIMPS_StatusTxnID();
		
		String requestPayload = "{\n" +
			    "  \"get_Single_Payment_Status_Corp_Req\": {\n" +
			    "    \"Header\": {\n" +
			    "      \"TranID\": \""+STATUS_TRANID+"\",\n" +
			    "      \"Corp_ID\": \""+CORP_ID+"\",\n" +
			    "      \"Maker_ID\": \"M005\",\n" +
			    "      \"Checker_ID\": \"C003\",\n" +
			    "      \"Approver_ID\": \"A003\"\n" +
			    "    },\n" +
			    "    \"Body\": {\n" +
			    "      \"RRN\": \""+rrn+"\"\n" +
			    "    },\n" +
			    "    \"Signature\": {\n" +
			    "      \"Signature\": \"Signature\"\n" +
			    "    }\n" +
			    "  }\n" +
			    "}";  
		
		System.out.println("\n**************************Status Checker Started*************************************");
		System.out.println("\nRequest Payload--> "+requestPayload);
		
		System.out.println("\nURL---> "+ url);
		
		String response = null;
		String status = null;
		try {
			response = invokeUniRequest(url, requestPayload);	
			
			JsonObject responseJsonParse = JsonParser.parseString(response).getAsJsonObject();
			
			if(responseJsonParse.has("get_Single_Payment_Status_Corp_Res")) {
				JsonObject bodyHeader = responseJsonParse.get("get_Single_Payment_Status_Corp_Res").getAsJsonObject().get("Header").getAsJsonObject();
				
				status = bodyHeader.get("Status").getAsString();
				System.out.println("Status resposne---> "+response);
				
			}else {
				System.out.println("ERROR: got other response then excpected");
			}
			
		}catch(Exception e) {
			System.err.println("ERROR: "+e.getMessage()+" "+response);
		}
		System.out.println("\n**********************Status check stoped******************************");
		return status;
	}
	
	
	/**
	 * Constructor to create the refID
	 */
	private void updateIMPSTxnID() {
		// Unirest.setTimeouts(0, 0);
		String propertyPath = "./src/test/resources/bankIntg.properties";

		// Accessing the property file to fetch the data
		PropertiesUtility properties = new PropertiesUtility();
		properties.propertiesInit(propertyPath);
		int num = Integer.parseInt(properties.readData("rblID"));

		TRANID = "ESCROWPAY0" + num;

		// Updating the number in property file
		properties.writeToProperties("rblID", String.valueOf(++num), propertyPath);
	}
	
	/**
	 * Constructor to create the refID
	 */
	private void updateIMPS_StatusTxnID() {
		// Unirest.setTimeouts(0, 0);
		String propertyPath = "./src/test/resources/bankIntg.properties";

		// Accessing the property file to fetch the data
		PropertiesUtility properties = new PropertiesUtility();
		properties.propertiesInit(propertyPath);
		int num = Integer.parseInt(properties.readData("statusID"));

		STATUS_TRANID = "ESCROWIMPSSTATUS" + num;

		// Updating the number in property file
		properties.writeToProperties("statusID", String.valueOf(++num), propertyPath);
	}
	
	/**
	 * Constructor to create the refID
	 */
	private void updateGetStatmentTxnID() {
		// Unirest.setTimeouts(0, 0);
		String propertyPath = "./src/test/resources/bankIntg.properties";

		// Accessing the property file to fetch the data
		PropertiesUtility properties = new PropertiesUtility();
		properties.propertiesInit(propertyPath);
		int num = Integer.parseInt(properties.readData("statmentID"));

		STMT_TRANID = "ESCROWSTMT" + num;

		// Updating the number in property file
		properties.writeToProperties("statmentID", String.valueOf(++num), propertyPath);
	}
	
	/**
	 * Method responsible for calling the specificed API and return the API response
	 * @throws Exception 
	 */
	private String invokeUniRequest(String url, String payload) throws Exception {
		String pfxFilePath = ".src/main/resources/myground11.pfx";
		
		 //  Load PFX file into KeyStore (No Password)
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(new File(pfxFilePath));
        keyStore.load(fis, new char[0]); // No password
        fis.close();

        // Set up KeyManagerFactory without password
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, new char[0]); // No password

        //f Create SSLContext with KeyStore
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

        // Create HttpClient with SSL support
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                sslContext, NoopHostnameVerifier.INSTANCE // Ignore hostname verification (if needed)
        );
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();

        // Configure Unirest to use custom HttpClient
        Unirest.config().httpClient(httpClient);
		
		kong.unirest.HttpResponse<String> response = Unirest.post(url)
				.header("Content-Type", "application/json")
				.basicAuth(USER_NAME, PASS_WORD)
				.queryString("client_id",CLIENT_ID)
				.queryString("client_secret",CLIENT_SECRET)
				.body(payload).asString();
		return response.getBody();

	}
}
