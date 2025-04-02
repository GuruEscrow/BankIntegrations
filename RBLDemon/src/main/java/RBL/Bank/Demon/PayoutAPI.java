package RBL.Bank.Demon;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import RBL.mysql.database.FetchDataFromDB;
import RBL.mysql.database.InsertDataIntoDB;
import kong.unirest.Unirest;

public class PayoutAPI {

//	Credentials
	
	private String service_api_key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiJlODdjZTAzMS0yZDZmLTQ0ZjAtYmY0Zi00ODg2Yjc2ZmIzMzIiLCJuYW1lIjpbImdldF9heGlzX2JhbmtfYWNjb3VudF9zdGF0ZW1lbnQiLCJnZXRfcGF5b3V0X2xvZ19lbnRyaWVzX2RhdGVfcmFuZ2UiLCJnZXRfdmFuX2NvbGxlY3RzX2VudHJpZXNfZGF0ZV9yYW5nZSIsImNoZWNrX2FuZF9wcm9jZXNzX3N0YXR1c193aXRoX2F4aXNfYmFuayIsImdldF9wYXlvdXRfbG9nX2VudHJpZXMiLCJjaGVja19zdGF0dXNfd2l0aF9heGlzX2JhbmsiLCJyZXZlcnRfcGF5b3V0c193aXRoX3V0ciJdLCJhdXRob3JpemVkX3BlcnNvbiI6eyJuYW1lIjoiQW51c3JlZSBWaW5vZCJ9LCJ0eXBlIjoic2VydmljZSIsImVudiI6ImxpdmUiLCJpYXQiOjE3MjY1NDgwMDd9.A6GWK1uflE2Qxx28vip5QsahM4nCsXLaueA_wL2Hc8s";	
	
	private String BASE_URL = "https://myglivetest.escrowstack.io";
	
//	Bank Account details
	
	private String TRANID;
	
	private String STATUS_TRANID;
	
	private String STMT_TRANID;
	
	private int status;
	
	private Map<String,String> stmtMap = new HashMap<String,String>();
	
    public Map<String, String> getStatements(String fromDate,String toDate,String tran_type,String amtValue,String curCode,String LpstDate,String LTxnDate,String LtxnID,String LsrlNo) {
		
		String url = BASE_URL+"/v1/service/get_live_account_statement_by_date";
		
		//Updating the transaction ID
		updateGetStatmentTxnID();
		
		String requestPayload =  "{\n" +
			    "    \"ledger_label\": \"MYGROUND11409002362954\",\n" +
			    "    \"mode\": \""+tran_type+"\",\n" +
			    "    \"from_date\": \""+fromDate+"\",\n" +
			    "    \"to_date\": \""+toDate+"\",\n" +
			    "    \"Pagination_Details\": {\n" +
			    "        \"Last_Balance\": {\n" +
			    "            \"Amount_Value\": \""+amtValue+"\",\n" +
			    "            \"Currency_Code\": \""+curCode+"\"\n" +
			    "        },\n" +
			    "        \"Last_Pstd_Date\": \""+LpstDate+"\",\n" +
			    "        \"Last_Txn_Date\": \""+LTxnDate+"\",\n" +
			    "        \"Last_Txn_Id\": \""+LtxnID+"\",\n" +
			    "        \"Last_Txn_SrlNo\": \""+LsrlNo+"\"\n" +
			    "    }\n" +
			    "}";  
		
		System.out.println("\n***************************statement fetch started**********************************");
		System.out.println("\nRequest Payload--> "+requestPayload);
		
		System.out.println("\nURL---> "+ url);

		String response = null;
		try {
			
			response = invokeUniRequest(url, requestPayload);	
			System.out.println("\nStatus -->"+status);
//			System.out.println("\nStatement response --> "+response);
			
//			JsonObject responseJsonParse = JsonParser.parseString(response).getAsJsonObject();
//			System.out.println(responseJsonParse.toString());
			if(JsonParser.parseString(response).isJsonArray()) {
				JsonArray statementArray = JsonParser.parseString(response).getAsJsonArray();
				
				 String txnDesc = null;
				 String allTxnID = null;
				    String amtsValue= null;
				    String currencyCode= null;
				    String pstdDate= null;
				    String txnDate= null;
				    String txnID= null;
				    String txnSrlNo = null;
				for (int stmts = 0; stmts < statementArray.size(); stmts++) {
					
					JsonObject jsonStmts = statementArray.get(stmts).getAsJsonObject();
					txnDesc = jsonStmts.get("transactionSummary").getAsJsonObject().get("txnDesc").getAsString();
					allTxnID = jsonStmts.get("txnId").getAsString().trim();
					
					if(stmts==statementArray.size()-1) {
						
						amtsValue = jsonStmts.get("txnBalance").getAsJsonObject().get("amountValue").getAsString().trim();
						currencyCode = jsonStmts.get("txnBalance").getAsJsonObject().get("currencyCode").getAsString().trim();
						
					    pstdDate = jsonStmts.get("pstdDate").getAsString().trim();
					    txnDate = jsonStmts.get("transactionSummary").getAsJsonObject().get("txnDate").getAsString().trim();
					    txnID = jsonStmts.get("txnId").getAsString().trim();
					    txnSrlNo = jsonStmts.get("txnSrlNo").getAsString().trim();	
					}
					
					if(txnDesc.contains("-")) {
						String utr_rrn = txnDesc.split("-")[0].trim();
						if (!(stmtMap.containsKey(utr_rrn))) {
							stmtMap.put(utr_rrn, jsonStmts.toString());
						} else {
							String oldstm = stmtMap.get(utr_rrn);
							stmtMap.put(utr_rrn, oldstm + "\n" + jsonStmts.toString());
						}
					}else if(txnDesc.contains("NEFT")) {
						String utr_rrn = txnDesc.split("/")[1].trim();
						if (!(stmtMap.containsKey(utr_rrn))) {
							stmtMap.put(utr_rrn, jsonStmts.toString());
						} else {
							String oldstm = stmtMap.get(utr_rrn);
							stmtMap.put(utr_rrn, oldstm + "\n" + jsonStmts.toString());
						}
					}else {
						if (!(stmtMap.containsKey(allTxnID))) {
							stmtMap.put(allTxnID, jsonStmts.toString());
						} else {
							String oldstm = stmtMap.get(allTxnID);
							stmtMap.put(allTxnID, oldstm + "\n" + jsonStmts.toString());
						}
					}
					
				}
				getStatements(fromDate, toDate, tran_type, amtsValue, currencyCode, pstdDate, txnDate, txnID, txnSrlNo);

			}else {
				System.out.println("Done");
			}
			
			//update the response in the data base
			
			//update the UTR in the data base
			
		}catch(Exception e) {
			//Update the error in the data bases
			System.err.println("Error: While fetching the statements "+e.getMessage());
			
		}
		
		System.out.println("\n******************************statment fetch ended********************************");
		return stmtMap;
	}
	
	public Map<String, String> imps_Payout(String amt,String beneIFSC,String beneAccNo,String beneName,String beneBankName,String mode,String txnNote) throws ClassNotFoundException, SQLException {
		
		String path = "/v1/service";
		switch (mode) {
		case "IMPS":
			path += "/make_imps_payout";
			break;
		case "NEFT":
			path += "/make_neft_payout";
			break;
		case "FT":
			path += "/make_ft_payout";
			break;
		case "RTGS":
			path += "/make_rtgs_payout";
			break;

		default:
			break;
		}
		String url = BASE_URL+path;
		Map<String,String> payoutDetails = new HashMap<String, String>();
		
		//Updating the transaction Id
		updateIMPSTxnID();
		
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmssSSS");
		String currentDate = date.format(formatter);
		
		String requestPayload = "{\n" +
                "  \"ledger_label\": \"MYGROUND11409002362954\",\n" +
                "  \"crn\": \""+currentDate+"\",\n" +
                "  \"amount\": \""+amt+"\",\n" +
                "  \"debit_TrnParticulars\": \"Testing\",\n" +
                "  \"debit_PartTrnRmks\": \"Testing\",\n" +
                "  \"payee_acc_no\": \""+beneAccNo+"\",\n" +
                "  \"payee_ifsc\": \""+beneIFSC+"\",\n" +
                "  \"payee_name\": \""+beneName+"\",\n" +
                "  \"bene_BankName\": \""+beneBankName+"\",\n" +
                "  \"bene_TrnParticulars\": \""+txnNote+"\",\n" +
                "  \"bene_PartTrnRmks\": \"bePartTrnRmks\",\n" +
                "  \"remarks\": \"Remarks\"\n" +
                "}"; 
		
		System.out.println("\n********************** Payout started***********************************");
		System.out.println("\nRequest Payload--> "+requestPayload);
		
		System.out.println("\nURL---> "+ url);
		
		String response = null;
		String initiatedTSString = null;
		String responseTSString = null;
		long delay = 0;
		String utr_rrn = null;
		String poNum = null;
		try {
			//update request body in the data base
			InsertDataIntoDB.insertPayloadWithUserRef(TRANID, requestPayload, mode);
			
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
			InsertDataIntoDB.updateTimestampsWithDelay(initiatedTSString, responseTSString, delay, TRANID);
			
			//update the response in the data base
			System.out.println("\nstatus--> "+status);
			System.out.println("\nresponse--> "+response);
			JsonObject responseJosnParse = JsonParser.parseString(response).getAsJsonObject();
			
			JsonObject responseBody = responseJosnParse.get("data").getAsJsonObject();
			if(responseBody.has("RefNo")) {
//				JsonObject responseBody =	responseJosnParse.get("Single_Payment_Corp_Resp").getAsJsonObject().get("Body").getAsJsonObject();
				
				if(mode.equals("IMPS")) {
				   utr_rrn = responseBody.get("RRN").getAsString();
				}else if(mode.equals("NEFT")) {
					utr_rrn = responseBody.get("UTRNo").getAsString();
					poNum = responseBody.get("PONum").getAsString();
				}else if(mode.equals("FT")) {
					utr_rrn = responseBody.get("RefNo").getAsString();
				}
				//Updating the txn response
				InsertDataIntoDB.updateTxnResponse(utr_rrn,poNum, response, TRANID);
				//Updating the initiated and response time with dealy
				InsertDataIntoDB.updateTimestampsWithDelay(initiatedTSString, responseTSString, delay, TRANID);
			}else {
				//Updating when we got other than expected result
				InsertDataIntoDB.updateTxnResponse("Not generated", "", response, TRANID);
				//Updating the initiated and response time with dealy
				InsertDataIntoDB.updateTimestampsWithDelay(initiatedTSString, responseTSString, delay, TRANID);
			}
	
		}catch(Exception e) {
			//Update the error in the data bases
			//Updating when we got other than expected result
			InsertDataIntoDB.updateTxnResponse("ERROR","",response+" "+ e.getMessage(), TRANID);
			System.err.println("Error: while doing payout "+e.getMessage());
			//Updating the initiated and response time with dealy
//			InsertDataIntoDB.updateTimestampsWithDelay(initiatedTSString, responseTSString, delay, TRANID);
		}
		
		payoutDetails.put("tranID", TRANID);
		payoutDetails.put("utr_rrn", utr_rrn);
		payoutDetails.put("mode", mode);
		payoutDetails.put("poNum", poNum);
		System.out.println("\n*************************** payout ended********************************");
		return payoutDetails;
	}
	
	public String imps_payout_StatusChecker(String rrn,String transID,String mode) throws ClassNotFoundException, SQLException {
		
		String url = BASE_URL+"/v1/service/check_rbl_payout_status";
		
		//Updating the transaction ID
//		updateIMPS_StatusTxnID();
		
		String requestPayload = "{\n" +
                "  \"ledger_label\": \"MYGROUND11409002362954\",\n" +
                "  \"mode\": \""+mode+"\",\n" +
                "  \"ref_no\": \""+rrn+"\"\n" +
                "}";  
		
		System.out.println("\n**************************Status Checker Started*************************************");
		System.out.println("\nRequest Payload--> "+requestPayload);
		
		System.out.println("\nURL---> "+ url);
		
		String response = null;
		String status = null;
		try {
			response = invokeUniRequest(url, requestPayload);	
			
			JsonObject responseJsonParse = JsonParser.parseString(response).getAsJsonObject();
			
			JsonObject bodyHeader = responseJsonParse.get("data").getAsJsonObject();
			if(bodyHeader.has("ORGTRANSACTIONID") && this.status == 200) {
//				JsonObject bodyHeader = responseJsonParse.get("get_Single_Payment_Status_Corp_Res").getAsJsonObject().get("Header").getAsJsonObject();
				
				if(mode.equals("NEFT")||mode.equals("RTGS")) {
					status = bodyHeader.get("TXNSTATUS").getAsString();					
				}else {
					status = "Success";	
				}
				System.out.println("Status resposne---> "+response);
				
				//updating the status in the DB
				InsertDataIntoDB.updateTxnStatus(response,transID , status);
			}else {
				//updating the status in the DB
				InsertDataIntoDB.updateTxnStatus(response,transID , "Notfound");
			}
			
		}catch(Exception e) {
			//Update the error in the data bases
			//updating the status in the DB
			InsertDataIntoDB.updateTxnStatus(e.getMessage()+" "+response, transID, "ERROR");
		}
		System.out.println("\n**********************Status check stoped******************************");
		return status;
	}
	
	
	/**
	 * Constructor to create the refID
	 */
	private void updateIMPSTxnID() {
		int num = 0;
		try {
			num = Integer.parseInt(FetchDataFromDB.getTxnID("payout"));
		} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TRANID = "ESCROW0" + num;

		// Updating the number in property file
		try {
			InsertDataIntoDB.updateTxnId("payout", String.valueOf(++num));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor to create the refID
	 */
	private void updateIMPS_StatusTxnID() {
		
		int num = 0;
		try {
			num = Integer.parseInt(FetchDataFromDB.getTxnID("status"));
		} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		STATUS_TRANID = "ESCROWSTATUS0" + num;

		// Updating the number in property file
		try {
			InsertDataIntoDB.updateTxnId("status", String.valueOf(++num));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Constructor to create the refID
	 */
	private void updateGetStatmentTxnID() {
		int num = 0;
		try {
			num = Integer.parseInt(FetchDataFromDB.getTxnID("stmt"));
		} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		STMT_TRANID = "ESCROWSMT0" + num;

		// Updating the number in property file
		try {
			InsertDataIntoDB.updateTxnId("stmt", String.valueOf(++num));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method responsible for calling the specificed API and return the API response
	 * @throws Exception 
	 */
	private String invokeUniRequest(String url, String payload) throws Exception {
		
		kong.unirest.HttpResponse<String> response = Unirest.post(url)
				.header("Content-Type", "application/json")
		        .header("apikey",service_api_key)
				.body(payload)
				.socketTimeout(1200000)
				.asString();
		status = response.getStatus();
		return response.getBody();
	}
}
