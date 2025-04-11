package com.NSDL.uat;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PayoutsTest {

    private String KEY = "oIc3jwrw2P9CPCS4xWpTVpx5uRHX6FNQm5zpyvrvVhiK4Z9a5BO7DvRswDEV9VOQeIvuLA4hDK9BB8DolMVfms6idRb6dAf7tWT4mZDStC1WgqkkKCnMSWq537UpDEa7sRMFNuXHKsWoarZ468uYHIfeXkG8ROQKVFBpKk9WmQ4yJB9qsH2nHjubDvECM0sradM3IxMSW14AMhhDRUcVEpy9Q1fIan23bYiWicqfhQH6HA9DbYwmUupKHcfoza8r";
    
    private String privateKey = "-----BEGIN PRIVATE KEY-----\r\n"
			+ "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCVrSnhezJ4WQykXYlL4Nz3Q4CRkJvSics08eZbh/b4GCInfXjbBnT3mN4+G0RIUZFUCVyDZUseR9AI3SmRhalvNNWgBQb1EvzLFKUn9TElzAeg86gKv9QJAngVkNWSONIx1b4k8X07k+5GKVUsMQqpoi8vhewvyu1IkJRHu+63cF2KsWLjgegzdAroH8LZa/bMIMq2MkMh10Uq8tePBrZdXEqHxEVacsJrHk7pDJ+AqkaWEjdv4Me+En6kmx/fjY/pZ0UNSwlVeIKYUFyZ4ATzyC07A9gkB4tgUt8bchlyHnYeO1itUM/f4xRNKJgeySj6HAw0KJcKCfrv9Sl1D3JLAgMBAAECggEADa49BeaITOwQFeqUv4eR5X8ufl+XgS1mEkn1R7LMQI3tepWN2TB5ush8BnTgj172kmtOoOs+a/SdZQoDStFK6ia2oJXaYRvMrlU4k/KQfPIX/76gQpHuXqpym79YE1w33iU5aCVd0HcWJXnmq+Te+B/3rHd9szqa6JyKaRTE3iWhCxYvBtQmkwa/FtyW5aljUHf6Iy+eJd4i5zMuEMdQEYZZrKOCfLnX+APH9a78P+Pkz4mo8SzL2EVTmqDBHFghjQV5iGJKkvoGV13dGUZqkgvOt6gl8HDoKIMPjoCp4JQJZ8dQJ4LYRRinkffW1Fg+m7bGP1yy3ihKNCS1qiRl9QKBgQC1K+phhhz3IL7aVs+KB+k94Bdg79fmW9ipYZ/rLlkmuG62AxUdNdnVupSlGed0B/f83yeckmYTPDWAesY1Y+qGzA6/XbxAsSPeoaOVUDaur1CI0RRTpan7j81ADfCzn2NK33AXD96RRe+JDeJwI2DyjvV8eN1bavgxVeO+ivOG3wKBgQDTfyBgj3BDkZf1wd5ueIGiMwgG3jQR/e6QRp/YXxvp8Io1c3JCPOaBNnw7GBeL+e0VxzF4a1c7mFtXlbSIXpgT/7TwInv5ahcr6BQlUu11cgtM8L9HJ88Q9D9jQRfvkUbDD6OQW5ieAae6oTr/DcYJMM5IhsQBDo5ByZSpw+DeFQKBgGwk2rN8Kq4ZjdMnHpt2PXQB3/KtOGL5UEtLSvpHoE/Mp8ld7aySCIXb6f7jh5VrbEw1qJ99hBDXT9hxcP9NtSDhjawNd+Hl21N5iPwH9ZwwahJBO7DyopTXMd2adKPD8LEwzuf1QRXWeTWGZDQr6iLf0wmJ3BhFsgFBE8EZkGRhAoGAejsahzPe+e4HGO12k/npUciUdwOsrElJuvBWKUKua0GtXr7d5hI0VbEbkzuVQDytHM0GdkXzIGopQDExKS8iTUyXUcFcTQVXZvS1z+xheGL0zP7GTWVgcrf2enKAVuBrOQisyx1k0sy6F05fPlDDh0RBtADO/p1e8f6CgKrIJxECgYBVWS5qqjfrWqpQjxpA+6NdXzoREA0bRD2zSj9g45fr3YuOBpGFk8BlGa4E9l964MrPKfWadV0Zp9U2stN/+ydxalB7vuoPgYCyxr+CI0vMf3LArGtBGTEqkb1Ot+WhRMLo4/3P0xGGKwM/ne/lulhha+bv1Flz2X5lSfbHyNCY+A==\r\n"
			+ "-----END PRIVATE KEY-----";

    
    @Test
    public void fetchBalance() throws Exception {
    	PayoutAPI NSDL_API = new PayoutAPI();
    	String accNo = "501040754916";
    	
    	String response = NSDL_API.fetchBalance(accNo);
    	System.out.println("\nResponse--> "+response);
    	
    	JsonObject parseResponse = JsonParser.parseString(response).getAsJsonObject();
    	String encryptedResponse = parseResponse.get("responsedata").getAsString();
    	
//    	System.out.println("\nDecrypted Response--> "+NSDL_Encrypt_Decrypt.decryptpayload(encryptedResponse, KEY));
    	System.out.println("\nDecrypted Response Data--> "+NSDL_Encrypt_Decrypt.PGPDecryptPayload(encryptedResponse, privateKey));
    }
    
    @Test
    public void getStatement() throws Exception {
    	PayoutAPI NSDL_API = new PayoutAPI();
    	String accNo = "501040754916";
    	String fromDate = "2025-04-02";
    	String toDate = "2025-04-03";
    	
    	String response = NSDL_API.getStatement(accNo,fromDate,toDate);
    	System.out.println("\nResponse--> "+response);
    	
    	JsonObject parseResponse = JsonParser.parseString(response).getAsJsonObject();
    	String encryptedResponse = parseResponse.get("responsedata").getAsString();
    	
//    	System.out.println("\nDecrypted Response--> "+NSDL_Encrypt_Decrypt.decryptpayload(encryptedResponse, KEY));
    	System.out.println("\nDecrypted Response Data--> "+NSDL_Encrypt_Decrypt.PGPDecryptPayload(encryptedResponse, privateKey));
    	
    }
    
    
	@Test
	public void addBene() {
		PayoutAPI NSDL_API = new PayoutAPI();
		
		String accountNo = "5467891234";
		String accType = "SAVINGS_ACCOUNT"; //VPA
		String ifsc = "HDFC0000001";
		String bankName = "SBI Bank Ltd.";
		
		String response = NSDL_API.createBene(accountNo,accType,ifsc,bankName);
		
		System.out.println("\nResponse--> "+response);	
	}

	
	@Test
	public void addNew_AccNo_ToExistingBene() {
		PayoutAPI NSDL_API = new PayoutAPI();

		String beneID = "fd272fe04b7d4e68effd01bddcc6bb34";
		String accountNo = "testuat@unitype";
		String accType = "VPA";
		String ifsc = "SBIN0000021";

		String response = NSDL_API.addNewAccount_ToExistingBene(beneID, accountNo, accType, ifsc);

		System.out.println("\nResponse--> " + response);
	}

	
	@Test
	public void beneStatusEnq() {
		PayoutAPI NSDL_API = new PayoutAPI();
		
		String beneID = "eca89c0554ce99eaf250504971789ede";
		
		String response = NSDL_API.beneStatusEnquiry(beneID);
		
		System.out.println("\nResponse--> "+response);	
	}

	
    @Test
	public void payout() {
		PayoutAPI NSDL_API = new PayoutAPI();
		
		String payoutMode = "IMPSP2A";//IMPSP2A NEFT RTGS
		String beneRefID = "68897f19b106926ed889fe3f7e3d01c9";
		String accountNo = "eca89c0554ce99eaf250504971789ede";
		double amt = 10.0;
		
		String response = NSDL_API.payout_ToBeneAccount(payoutMode, beneRefID, accountNo, amt);
		
		System.out.println("\nResponse--> "+response);	
	}

    
    @Test
	public void upi_payout() throws Exception {
		UPI_Payout NSDL_API = new UPI_Payout();

		String beneAccRefID = "fcac695db02687ffb7955b66a43fe6e6";
		double amt = 2.0;
		
		String response = NSDL_API.upiPreApprovedPayout(beneAccRefID, amt);

		System.out.println("\nResponse--> " + response);
		
//        JsonObject jsonResponseBody = JsonParser.parseString(response).getAsJsonObject();
//        
//        String encryptedResponseData = jsonResponseBody.get("responsedata").getAsString();
        
//        System.out.println("\nDecrypted Response Data--> "+NSDL_Encrypt_Decrypt.decryptpayload(encryptedResponseData, KEY));
//        System.out.println("\nDecrypted Response Data--> "+NSDL_Encrypt_Decrypt.PGPDecryptPayload(encryptedResponseData, privateKey.replace("\r\n","").trim()));
        
	}
    
    
    @Test
	public void payout_status() {
		PayoutAPI NSDL_API = new PayoutAPI();

		String id = "ESCROW0339";

		String requestID = id;
		String txnType = "IMPSP2A";
		String chanTxnRef = id;
		String usrTxnRef = id;

		String response = NSDL_API.payout_StatuChecker(requestID, txnType, chanTxnRef, usrTxnRef);

		System.out.println("\nResponse--> " + response);
	}

}
