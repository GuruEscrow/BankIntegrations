package com.IDFC.uat;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import genricLibraries.PropertiesUtility;
import io.github.cdimascio.dotenv.Dotenv;
import kong.unirest.Unirest;

public class IDFC_APIs {
	
	private static String secretHex;
	private static String BaseURL;
	private static String accessToken;

	
	public void fetchBalance() {
		IDFCIV_Encrypt_Decrypt encrypt = new IDFCIV_Encrypt_Decrypt();
		
		String plainPayload = "{\n" +
				"    \"prefetchAccountReq\": {\n" +
				"        \"CBSTellerBranch\": \"\",\n" +
				"        \"CBSTellerID\": \"\",\n" +
				"        \"accountNumber\": \"21488530945\"\n" +
				"    }\n" +
				"}";
		
		//This code is used to initiate the credentials
    	Dotenv credentials = Dotenv.configure().directory("./").filename("idbl.env").load();
    	secretHex = credentials.get("secretHex");
    	BaseURL = credentials.get("BaseUrl");
    	accessToken = credentials.get("accessToken");
    	
    	String url = BaseURL+"/acctenq/v2/prefetchAccount";
    	
    	System.out.println("\nPlain Payload --> "+plainPayload);
    	
		try {
			String encryptReq = encrypt.encrypt(plainPayload, IDFC_APIs.secretHex);
			System.out.println("\nEncrypted Request --> "+encryptReq);
			
			System.out.println("\nURL --> "+url);
			
		    String response = invokeUniRequest(url, encryptReq);
		    System.out.println("\nEncrypted Response --> "+response);
		    System.out.println("\nDecrypted Response --> "+encrypt.decrypt(response, IDFC_APIs.secretHex));
		    
		    
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getStatment(String fromDate,String toDate) {
		IDFCIV_Encrypt_Decrypt encrypt = new IDFCIV_Encrypt_Decrypt();
		
		String plainPayload = "{\n" +
			    "    \"getAccountStatementReq\": {\n" +
			    "        \"CBSTellerBranch\": \"\",\n" +
			    "        \"CBSTellerID\": \"\",\n" +
			    "        \"accountNumber\": \"21488530945\",\n" +
			    "        \"fromDate\": \""+fromDate+"\",\n" +
			    "        \"toDate\": \""+toDate+"\",\n" +
			    "        \"numberOfTransactions\": \"100\",\n" +
			    "        \"prompt\": \"\"\n" +
			    "    }\n" +
			    "}";
		
		//This code is used to initiate the credentials
    	Dotenv credentials = Dotenv.configure().directory("./").filename("idbl.env").load();
    	IDFC_APIs.secretHex = credentials.get("secretHex");
        BaseURL = credentials.get("BaseUrl");
        accessToken = credentials.get("accessToken");
    	
    	String url = BaseURL+"/acctenq/v3/getAccountStatement";
    	
    	System.out.println("\nPlain Payload --> "+plainPayload);
    	
		try {
			String encryptReq = encrypt.encrypt(plainPayload, IDFC_APIs.secretHex);
			System.out.println("\nEncrypted Request --> "+encryptReq);
			
			System.out.println("\nURL --> "+url);
			
		    String response = invokeUniRequest(url, encryptReq);
		    System.out.println("\nEncrypted Response --> "+response);
		    System.out.println("\nDecrypted Response --> "+encrypt.decrypt(response, IDFC_APIs.secretHex));
		    
		    
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void payoutForAllMop(String txnID,String beneAccNo,String beneIFSC,String beneName,String beneAdd,String email,String mob,String mode,String amt) {
		IDFCIV_Encrypt_Decrypt encrypt = new IDFCIV_Encrypt_Decrypt();
		
		String plainPayload = null;
		if(mode.equals("RTGS")) {
			plainPayload = "{\n" +
				    "    \"initiateAuthGenericFundTransferAPIReq\": {\n" +
				    "         \"tellerBranch\": \"\",\n" +
				    "         \"tellerID\": \"\",\n" +
				    "         \"transactionID\": \""+txnID+"\",\n" +
				    "         \"debitAccountNumber\": \"21488530945\",\n" +
				    "         \"creditAccountNumber\": \""+beneAccNo+"\",\n" +
				    "         \"remitterName\": \"Pooja Prakash Sharma\",\n" +
				    "         \"amount\": \""+amt+"\",\n" +
				    "         \"currency\": \"INR\",\n" +
				    "         \"transactionType\": \""+mode+"\",\n" +
				    "         \"paymentDescription\": \"fund transfer\",\n" +
				    "         \"beneficiaryIFSC\": \""+beneIFSC+"\",\n" +
				    "         \"beneficiaryName\": \""+beneName+"\",\n" +
				    "         \"beneficiaryAddress\": \""+beneAdd+"\",\n" +
				    "         \"emailId\": \""+email+"\",\n" +
				    "         \"mobileNo\": \""+mob+"\",\n" +
				    "         \"messageType\": \"R41\"\n" +
				    "    }\n" +
				    "}";
		}else {
			plainPayload = "{\n" +
				    "    \"initiateAuthGenericFundTransferAPIReq\": {\n" +
				    "         \"tellerBranch\": \"\",\n" +
				    "         \"tellerID\": \"\",\n" +
				    "         \"transactionID\": \""+txnID+"\",\n" +
				    "         \"debitAccountNumber\": \"21488530945\",\n" +
				    "         \"creditAccountNumber\": \""+beneAccNo+"\",\n" +
				    "         \"remitterName\": \"Guru\",\n" +
				    "         \"amount\": \""+amt+"\",\n" +
				    "         \"currency\": \"INR\",\n" +
				    "         \"transactionType\": \""+mode+"\",\n" +
				    "         \"paymentDescription\": \"fund transfer\",\n" +
				    "         \"beneficiaryIFSC\": \""+beneIFSC+"\",\n" +
				    "         \"beneficiaryName\": \""+beneName+"\",\n" +
				    "         \"beneficiaryAddress\": \""+beneAdd+"\",\n" +
				    "         \"emailId\": \""+email+"\",\n" +
				    "         \"mobileNo\": \""+mob+"\"\n" +
//				    "         \"messageType\": \"R41\"\n" +
				    "    }\n" +
				    "}";
		}
		
		//This code is used to initiate the credentials
    	Dotenv credentials = Dotenv.configure().directory("./").filename("idbl.env").load();
    	IDFC_APIs.secretHex = credentials.get("secretHex");
        BaseURL = credentials.get("BaseUrl");
        accessToken = credentials.get("accessToken");
    	
    	String url = BaseURL+"/paymenttxns/v1/fundTransfer";
    	
    	System.out.println("\nPlain Payload --> "+plainPayload);
    	
		try {
			String encryptReq = encrypt.encrypt(plainPayload, IDFC_APIs.secretHex);
			System.out.println("\nEncrypted Request --> "+encryptReq);
			
			System.out.println("\nURL --> "+url);
			
		    String response = invokeUniRequest(url, encryptReq);
		    System.out.println("\nEncrypted Response --> "+response);
		    System.out.println("\nDecrypted Response --> "+encrypt.decrypt(response, IDFC_APIs.secretHex));
		    
		    
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void txnStatus(String mode,String txnRefNum,String payRefNum,String txnDate) {
		IDFCIV_Encrypt_Decrypt encrypt = new IDFCIV_Encrypt_Decrypt();
		
		String plainPayload = "{\n" +
			    "    \"paymentTransactionStatusReq\": {\n" +
			    "        \"tellerBranch\": \"\",\n" +
			    "        \"tellerID\": \"\",\n" +
			    "        \"transactionType\": \""+mode+"\",\n" +
			    "        \"transactionReferenceNumber\": \""+txnRefNum+"\",\n" +
			    "        \"paymentReferenceNumber\": \""+payRefNum+"\",\n" +
			    "        \"transactionDate\": \""+txnDate+"\"\n" +
			    "    }\n" +
			    "}";
		
		//This code is used to initiate the credentials
    	Dotenv credentials = Dotenv.configure().directory("./").filename("idbl.env").load();
    	IDFC_APIs.secretHex = credentials.get("secretHex");
        BaseURL = credentials.get("BaseUrl");
        accessToken = credentials.get("accessToken");
    	
    	String url = BaseURL+"/paymentenqs/v1/paymentTransactionStatus";
    	
    	System.out.println("\nPlain Payload --> "+plainPayload);
    	
		try {
			String encryptReq = encrypt.encrypt(plainPayload, IDFC_APIs.secretHex);
			System.out.println("\nEncrypted Request --> "+encryptReq);
			
			System.out.println("\nURL --> "+url);
			
		    String response = invokeUniRequest(url, encryptReq);
		    System.out.println("\nEncrypted Response --> "+response);
		    System.out.println("\nDecrypted Response --> "+encrypt.decrypt(response, IDFC_APIs.secretHex));
		    
		    
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void beneValidation(String identifier,String crAccNo,String crIfsc,String txnRef,String merchCode) {
		IDFCIV_Encrypt_Decrypt encrypt = new IDFCIV_Encrypt_Decrypt();
		
		String plainPayload = "{\n" +
			    "    \"beneValidationReq\": {\n" +
			    "        \"remitterName\": \"Vikas\",\n" +
			    "        \"remitterMobileNumber\": \"+91-9819574650\",\n" +
			    "        \"debtorAccountId\": \"21488530945\",\n" +
			    "        \"accountType\": \"10\",\n" +
			    "        \"creditorAccountId\": \""+crAccNo+"\",\n" +
			    "        \"ifscCode\": \""+crIfsc+"\",\n" +
			    "        \"paymentDescription\": \"Testing\",\n" +
			    "        \"transactionReferenceNumber\": \""+txnRef+"\",\n" +
			    "        \"merchantCode\": \""+merchCode+"\",\n" +
			    "        \"identifier\": \""+identifier+"\"\n" +
			    "    }\n" +
			    "}";
		
		//This code is used to initiate the credentials
    	Dotenv credentials = Dotenv.configure().directory("./").filename("idbl.env").load();
    	IDFC_APIs.secretHex = credentials.get("secretHex");
        BaseURL = credentials.get("BaseUrl");
        accessToken = credentials.get("accessToken");
    	
    	String url = BaseURL+"/paymentenqs/v1/beneValidation";
    	
    	System.out.println("\nPlain Payload --> "+plainPayload);
    	
		try {
			String encryptReq = encrypt.encrypt(plainPayload, IDFC_APIs.secretHex);
			System.out.println("\nEncrypted Request --> "+encryptReq);
			
			System.out.println("\nURL --> "+url);
			
		    String response = invokeUniRequest(url, encryptReq);
		    System.out.println("\nEncrypted Response --> "+response);
		    System.out.println("\nDecrypted Response --> "+encrypt.decrypt(response, IDFC_APIs.secretHex));
		    
		    
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private String invokeUniRequest(String url, String payload){
		
		PropertiesUtility property = new PropertiesUtility();
    	String path = "./src/test/resources/bankIntg.properties";
    	property.propertiesInit(path);
    	int id = Integer.parseInt(property.readData("cid"));
		
		kong.unirest.HttpResponse<String> response = Unirest.post(url)
				.header("Content-Type", "application/octet-stream")
				.header("source", "IFL")
				.header("correlationId", "ESCROW00"+id)
				.header("Authorization", "Bearer " + Access_Token.getAccessToken())
				.body(payload)
				.socketTimeout(1200000)
				.asString();
		
		property.writeToProperties("cid",String.valueOf(++id), path);
		System.out.println("Response code -->"+response.getStatus());
		return response.getBody();

	}

}
