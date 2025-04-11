package com.NSDL.uat;

import genricLibraries.PropertiesUtility;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import kong.unirest.Unirest;

public class PayoutAPI {
	
	private String requestPublicCert = "-----BEGIN PUBLIC KEY-----\r\n"
			+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3CZiAJxaBW7iJ8BT2HF9\r\n"
			+ "FXlr0QI+2TCaAo/+XSpPFH5Srva3t2cbyp9dRzxIyFx8daIgsTfB8Mbc/sTpf5f8\r\n"
			+ "+2XKpsbmZDwS9CAaHORBy+tLkkzXWbsS6lJpKyEvQJmAaOUw9HyEK6mQh3eLfARH\r\n"
			+ "+u9Sgg3u3aXhoJqG1zSfWACNyYM79D8VvaWF6leYLz7d1WFfGsGDOsci7nV10nyy\r\n"
			+ "6l8DooPOR2aedEmeS8c11uPN+eVYbzfYlZYkbNyugrsXT1G0dtI4g6DICL8xTnKq\r\n"
			+ "791YFY2Ppq2nJotfcibKd0EWHj4B/INDEjHIKtBC4RM2uK8HeCKEykamG8vaao7m\r\n"
			+ "mwIDAQAB\r\n"
			+ "-----END PUBLIC KEY-----";

	private String responsePublicCert = "-----BEGIN CERTIFICATE-----\r\n"
			+ "MIIEKTCCAxGgAwIBAgIUDRtZcLAPpDnWUC5QGIDrxActutYwDQYJKoZIhvcNAQEL\r\n"
			+ "BQAwgaIxCzAJBgNVBAYTAklOMRQwEgYDVQQIDAtNQUhBUkFTSFRSQTEUMBIGA1UE\r\n"
			+ "BwwLTkFWSSBNVU1CQUkxITAfBgNVBAoMGFBBUlRORVIgTkVPIEJBTktJTkcgTFRE\r\n"
			+ "LjEQMA4GA1UECwwHVEVTVElORzERMA8GA1UEAwwIQkFOS0NFUlQxHzAdBgkqhkiG\r\n"
			+ "9w0BCQEWEFBBUlRORVJAVEVTVC5DT00wIBcNMjMxMjA3MDUwODExWhgPMjIyMTAx\r\n"
			+ "MjMwNTA4MTFaMIGiMQswCQYDVQQGEwJJTjEUMBIGA1UECAwLTUFIQVJBU0hUUkEx\r\n"
			+ "FDASBgNVBAcMC05BVkkgTVVNQkFJMSEwHwYDVQQKDBhQQVJUTkVSIE5FTyBCQU5L\r\n"
			+ "SU5HIExURC4xEDAOBgNVBAsMB1RFU1RJTkcxETAPBgNVBAMMCEJBTktDRVJUMR8w\r\n"
			+ "HQYJKoZIhvcNAQkBFhBQQVJUTkVSQFRFU1QuQ09NMIIBIjANBgkqhkiG9w0BAQEF\r\n"
			+ "AAOCAQ8AMIIBCgKCAQEAla0p4XsyeFkMpF2JS+Dc90OAkZCb0onLNPHmW4f2+Bgi\r\n"
			+ "J3142wZ095jePhtESFGRVAlcg2VLHkfQCN0pkYWpbzTVoAUG9RL8yxSlJ/UxJcwH\r\n"
			+ "oPOoCr/UCQJ4FZDVkjjSMdW+JPF9O5PuRilVLDEKqaIvL4XsL8rtSJCUR7vut3Bd\r\n"
			+ "irFi44HoM3QK6B/C2Wv2zCDKtjJDIddFKvLXjwa2XVxKh8RFWnLCax5O6QyfgKpG\r\n"
			+ "lhI3b+DHvhJ+pJsf342P6WdFDUsJVXiCmFBcmeAE88gtOwPYJAeLYFLfG3IZch52\r\n"
			+ "HjtYrVDP3+MUTSiYHsko+hwMNCiXCgn67/UpdQ9ySwIDAQABo1MwUTAdBgNVHQ4E\r\n"
			+ "FgQUBlHJd2w0TGQJxO4hAL+6tNGqb4swHwYDVR0jBBgwFoAUBlHJd2w0TGQJxO4h\r\n"
			+ "AL+6tNGqb4swDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAigVR\r\n"
			+ "i7kwou8IzfAa7Gma+xWZjtLJfeilrcOLyW1BAnlpwS24qK+9xebZKRaeiZIhpX+I\r\n"
			+ "wFpId/T8/YS/iLvUt5TPaJ22rDCX1DAJotPyt342+iLtiERlUfHJXPApw5ykl6xs\r\n"
			+ "ML1818cPlxhP+660DgFFide3Sy7Nm2RabizuMcBiCr6kfpteoTIZYm18ZkvaDUyf\r\n"
			+ "HlU0TQEVhAJWmbCLSObIcCNnH6vugxPLQknBZdu7HRGeVaGEz1FEbigNWz8EbaFj\r\n"
			+ "xTVADjR9fwFKKS13Qbkg1OgBX4dww2ForKC4Fwqb4gKG+7InkiaT0jdUvkeEKPWc\r\n"
			+ "eIEIKbA01XB29Fr1Tw==\r\n"
			+ "-----END CERTIFICATE-----\r\n";
	
	private String privateKey = "-----BEGIN PRIVATE KEY-----\r\n"
			+ "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCVrSnhezJ4WQykXYlL4Nz3Q4CRkJvSics08eZbh/b4GCInfXjbBnT3mN4+G0RIUZFUCVyDZUseR9AI3SmRhalvNNWgBQb1EvzLFKUn9TElzAeg86gKv9QJAngVkNWSONIx1b4k8X07k+5GKVUsMQqpoi8vhewvyu1IkJRHu+63cF2KsWLjgegzdAroH8LZa/bMIMq2MkMh10Uq8tePBrZdXEqHxEVacsJrHk7pDJ+AqkaWEjdv4Me+En6kmx/fjY/pZ0UNSwlVeIKYUFyZ4ATzyC07A9gkB4tgUt8bchlyHnYeO1itUM/f4xRNKJgeySj6HAw0KJcKCfrv9Sl1D3JLAgMBAAECggEADa49BeaITOwQFeqUv4eR5X8ufl+XgS1mEkn1R7LMQI3tepWN2TB5ush8BnTgj172kmtOoOs+a/SdZQoDStFK6ia2oJXaYRvMrlU4k/KQfPIX/76gQpHuXqpym79YE1w33iU5aCVd0HcWJXnmq+Te+B/3rHd9szqa6JyKaRTE3iWhCxYvBtQmkwa/FtyW5aljUHf6Iy+eJd4i5zMuEMdQEYZZrKOCfLnX+APH9a78P+Pkz4mo8SzL2EVTmqDBHFghjQV5iGJKkvoGV13dGUZqkgvOt6gl8HDoKIMPjoCp4JQJZ8dQJ4LYRRinkffW1Fg+m7bGP1yy3ihKNCS1qiRl9QKBgQC1K+phhhz3IL7aVs+KB+k94Bdg79fmW9ipYZ/rLlkmuG62AxUdNdnVupSlGed0B/f83yeckmYTPDWAesY1Y+qGzA6/XbxAsSPeoaOVUDaur1CI0RRTpan7j81ADfCzn2NK33AXD96RRe+JDeJwI2DyjvV8eN1bavgxVeO+ivOG3wKBgQDTfyBgj3BDkZf1wd5ueIGiMwgG3jQR/e6QRp/YXxvp8Io1c3JCPOaBNnw7GBeL+e0VxzF4a1c7mFtXlbSIXpgT/7TwInv5ahcr6BQlUu11cgtM8L9HJ88Q9D9jQRfvkUbDD6OQW5ieAae6oTr/DcYJMM5IhsQBDo5ByZSpw+DeFQKBgGwk2rN8Kq4ZjdMnHpt2PXQB3/KtOGL5UEtLSvpHoE/Mp8ld7aySCIXb6f7jh5VrbEw1qJ99hBDXT9hxcP9NtSDhjawNd+Hl21N5iPwH9ZwwahJBO7DyopTXMd2adKPD8LEwzuf1QRXWeTWGZDQr6iLf0wmJ3BhFsgFBE8EZkGRhAoGAejsahzPe+e4HGO12k/npUciUdwOsrElJuvBWKUKua0GtXr7d5hI0VbEbkzuVQDytHM0GdkXzIGopQDExKS8iTUyXUcFcTQVXZvS1z+xheGL0zP7GTWVgcrf2enKAVuBrOQisyx1k0sy6F05fPlDDh0RBtADO/p1e8f6CgKrIJxECgYBVWS5qqjfrWqpQjxpA+6NdXzoREA0bRD2zSj9g45fr3YuOBpGFk8BlGa4E9l964MrPKfWadV0Zp9U2stN/+ydxalB7vuoPgYCyxr+CI0vMf3LArGtBGTEqkb1Ot+WhRMLo4/3P0xGGKwM/ne/lulhha+bv1Flz2X5lSfbHyNCY+A==\r\n"
			+ "-----END PRIVATE KEY-----";

	private String CHANNEL_ID = "ElxvUGaBniUlBJtMWdcG";

	private String PARNTER_ID = "c377bbe133c18bab6360b3f3ed5d8f3f";

	private String appId = "com.sample.nsdlpb"; //"com.mobile.nsdlpb"
	
	private String BASE_URL = "https://jiffyuat.nsdlbank.co.in/partnerbank";
	
	private String KEY = "oIc3jwrw2P9CPCS4xWpTVpx5uRHX6FNQm5zpyvrvVhiK4Z9a5BO7DvRswDEV9VOQeIvuLA4hDK9BB8DolMVfms6idRb6dAf7tWT4mZDStC1WgqkkKCnMSWq537UpDEa7sRMFNuXHKsWoarZ468uYHIfeXkG8ROQKVFBpKk9WmQ4yJB9qsH2nHjubDvECM0sradM3IxMSW14AMhhDRUcVEpy9Q1fIan23bYiWicqfhQH6HA9DbYwmUupKHcfoza8r";
	
	private String refID;
	
	/**
	 * This method is used to fetch the balance
	 */
	public String fetchBalance(String accNo) {
		
		String url = BASE_URL+"/getBalance";
		String serviceType = "GETBALANCE";
		
		String Data_To_Encrypt =  "{\n"
			    + "  \"instrumenttype\": \"ACCOUNTNO\",\n"
			    + "  \"instrumentid\": \""+accNo+"\"\n"
			    + "}";
		
		String Req_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\",\n "
			    + "\"partnerid\": \""+PARNTER_ID+"\",\n "
			    + "\"appid\": \""+appId+"\",\n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\": \"%s\"\n"
			    + "}";
		
		String Plain_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\", \n"
			    + "\"partnerid\": \""+PARNTER_ID+"\", \n"
			    + "\"appid\": \""+appId+"\", \n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\":"+Data_To_Encrypt+"\n"
			    + "}";
		
		System.out.println("Data to Encrypt--> "+Data_To_Encrypt);
		System.out.println("\nPlain Payload--> "+Plain_Payload);
		String encrypted = NSDL_Encrypt_Decrypt.EncryptPayload(Data_To_Encrypt, requestPublicCert);
//		String encrypted = NSDL_Encrypt_Decrypt.encryptstring(Data_To_Encrypt, KEY);

		String payload = String.format(Req_Payload, encrypted);
		
		System.out.println("\nEncrypted payload--> "+payload);
		System.out.println("\nURL---> "+ url);
		String response = invokeUniRequest(url, payload,serviceType);
		
		return response;
	}

	/**
	 * This method is used to fetch the account statement
	 */
	public String getStatement(String accNo,String fromDate,String toDate) {
		String url = BASE_URL+"/getStatement";
		String serviceType = "GETSTATEMENT";
		
		String Data_To_Encrypt =  "{\n"
			    + "  \"instrumenttype\": \"ACCOUNTNO\",\n"
			    + "  \"instrumentid\": \""+accNo+"\",\n"
			    + "  \"from_date\": \""+fromDate+"\",\n"
			    + "  \"till_date\": \""+toDate+"\"\n"
			    + "}";
		
		String Req_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\",\n "
			    + "\"partnerid\": \""+PARNTER_ID+"\",\n "
			    + "\"appid\": \""+appId+"\",\n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\": \"%s\"\n"
			    + "}";
		
		String Plain_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\", \n"
			    + "\"partnerid\": \""+PARNTER_ID+"\", \n"
			    + "\"appid\": \""+appId+"\", \n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\":"+Data_To_Encrypt+"\n"
			    + "}";
		
		System.out.println("Data to Encrypt--> "+Data_To_Encrypt);
		System.out.println("\nPlain Payload--> "+Plain_Payload);
		String encrypted = NSDL_Encrypt_Decrypt.EncryptPayload(Data_To_Encrypt, requestPublicCert);
//		String encrypted = NSDL_Encrypt_Decrypt.encryptstring(Data_To_Encrypt, KEY);

		String payload = String.format(Req_Payload, encrypted);
		
		System.out.println("\nEncrypted payload--> "+payload);
		System.out.println("\nURL---> "+ url);
		String response = invokeUniRequest(url, payload,serviceType);
		
		return response;
	}
	
	/**
	 * This method is used to add the Beneficiary
	 */
	public String createBene(String accountNo,String accType,String ifsc,String bankName) {
		String url = BASE_URL+"/createBeneIdAccount";
		String serviceType = "CREATEBENEIDACCOUNT";
		
		String Data_To_Encrypt = "{\n"
			    + "  \"requestId\": \""+refID+"\",\n"
			    + "  \"partnerreferenceno\": \""+refID+"\",\n"
			    + "  \"benename\": \"Sample Beneficiary\",\n"
			    + "  \"benetype\": \"AGENT\",\n"
			    + "  \"partnerrelationdetail\": \"AGENT\",\n"
			    + "  \"benemobileno\": \"9890012345\",\n"
			    + "  \"beneemailid\": \"sample@sample.com\",\n"
			    + "  \"beneaccountid\": \""+accountNo+"\",\n"
			    + "  \"beneaccountidtype\": \""+accType+"\",\n"
			    + "  \"benebankname\": \""+bankName+"\",\n"
			    + "  \"benebankifsc\": \""+ifsc+"\",\n"
			    + "  \"requestedby\": \"Sample Requester\",\n"
			    + "  \"approvedby\": \"Sample Approver\"\n"
			    + "}";
		
		String Req_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\",\n "
			    + "\"partnerid\": \""+PARNTER_ID+"\",\n "
			    + "\"appid\": \""+appId+"\",\n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\": \"%s\"\n"
			    + "}";
		
		String Plain_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\", \n"
			    + "\"partnerid\": \""+PARNTER_ID+"\", \n"
			    + "\"appid\": \""+appId+"\", \n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\":"+Data_To_Encrypt+"\n"
			    + "}";
		
		System.out.println("Data to Encrypt--> "+Data_To_Encrypt);
		System.out.println("\nPlain Payload--> "+Plain_Payload);
//		String encrypted = NSDL_Encrypt_Decrypt.EncryptPayload(Data_To_Encrypt, requestPublicCert);
		String encrypted = NSDL_Encrypt_Decrypt.encryptstring(Data_To_Encrypt, KEY);

		String payload = String.format(Req_Payload, encrypted);
		
		System.out.println("\nEncrypted payload--> "+payload);
		System.out.println("\nURL---> "+ url);
		String response = invokeUniRequest(url, payload,serviceType);
		
		return response;
	}


	/**
	 * This method is used to add the new account number to existing Bene
	 */
	public String addNewAccount_ToExistingBene(String beneID,String accountNo,String accType,String ifsc) {
		String url = BASE_URL+"/createBeneIdAccount";
		String serviceType = "CREATEBENEIDACCOUNT";
		
		String Data_To_Encrypt = "{\n" +
			    "  \"beneid\": \""+beneID+"\",\n" +
			    "  \"beneaccountid\": \""+accountNo+"\",\n" +
			    "  \"beneaccountidtype\": \""+accType+"\",\n" +
			    "  \"benebankname\": \"Axis Bank Ltd.\",\n" +
			    "  \"benebankifsc\": \""+ifsc+"\",\n" +
			    "  \"defaultflag\": \"Y\",\n" +
			    "  \"requestedby\": \"Sample Requester\",\n" +
			    "  \"approvedby\": \"Sample Approver\"\n" +
			    "}";
		
		String Req_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\",\n "
			    + "\"partnerid\": \""+PARNTER_ID+"\",\n "
			    + "\"appid\": \""+appId+"\",\n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\": \"%s\"\n"
			    + "}";
		
		String Plain_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\", \n"
			    + "\"partnerid\": \""+PARNTER_ID+"\", \n"
			    + "\"appid\": \""+appId+"\", \n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\":"+Data_To_Encrypt+"\n"
			    + "}";
		
		System.out.println("Data to Encrypt--> "+Data_To_Encrypt);
		System.out.println("\nPlain Payload--> "+Plain_Payload);
//		String encrypted = NSDL_Encrypt_Decrypt.EncryptPayload(Data_To_Encrypt, requestPublicCert);
		String encrypted = NSDL_Encrypt_Decrypt.encryptstring(Data_To_Encrypt, KEY);

		String payload = String.format(Req_Payload, encrypted);
		System.out.println("\nEncrypted Payload--> "+payload);
		
		System.out.println("\nURL---> "+ url);
		String response = invokeUniRequest(url, payload,serviceType);
		
		return response;
	}
	
	/**
	 * This method is used to check the bene status
	 */
	public String beneStatusEnquiry(String beneID) {
		String url = BASE_URL+"/beneDetailsFetch";
		String serviceType = "FETCHBENEDETAILS";
		
		String Data_To_Encrypt = "{\n" +
			    "  \"beneid\": \""+beneID+"\"\n" +
			    "}";
		
		String Req_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\",\n "
			    + "\"partnerid\": \""+PARNTER_ID+"\",\n "
			    + "\"appid\": \""+appId+"\",\n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\": \"%s\"\n"
			    + "}";
		
		String Plain_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\", \n"
			    + "\"partnerid\": \""+PARNTER_ID+"\", \n"
			    + "\"appid\": \""+appId+"\", \n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\":"+Data_To_Encrypt+"\n"
			    + "}";
		
		System.out.println("Data to Encrypt--> "+Data_To_Encrypt);
		System.out.println("\nPlain Payload--> "+Plain_Payload);
//		String encrypted = NSDL_Encrypt_Decrypt.EncryptPayload(Data_To_Encrypt, requestPublicCert);
		String encrypted = NSDL_Encrypt_Decrypt.encryptstring(Data_To_Encrypt, KEY);

	
		String payload = String.format(Req_Payload, encrypted);
		System.out.println("\nEncrypted payload--> "+payload);
		
		System.out.println("\nURL---> "+ url);
		String response = invokeUniRequest(url, payload,serviceType);
		
		return response;
	}
	
	/**
	 * This method is used to do payout
	 */
	public String payout_ToBeneAccount(String payoutMode,String beneRefID,String accountNo,double amt) {
		String url = BASE_URL+"/benePayOut";
		String serviceType = "BENEPAYOUT";
		
		String Data_To_Encrypt = "{\n" +
			    " \"requestId\": \""+refID+"\",\n" +
			    " \"paymentdtls\": {\n" +
			    "  \"txntype\": \""+payoutMode+"\",\n" +
			    "  \"chantxnrefno\": \""+refID+"\",\n" +
			    "  \"usrtxnrefno\": \""+refID+"\",\n" +
			    "  \"payeedtls\": {\n" +
			    "   \"beneaccountrefid\": \""+beneRefID+"\",\n" +
			    "   \"beneaccountid\": \""+accountNo+"\"\n" +
			    "  },\n" +
			    "  \"txnamount\": "+amt+",\n" +
			    "  \"txncurrency\": \"INR\",\n" +
			    "  \"txnts\": \"20190508121400\",\n" +
			    "  \"narration\": \"Sample Transaction\"\n" +
			    " },\n" +
			    " \"requestedby\": \"Sample Requester\",\n" +
			    " \"approvedby\": \"Sample Approver\"\n" +
			    "}";
		
		String Req_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\",\n "
			    + "\"partnerid\": \""+PARNTER_ID+"\",\n "
			    + "\"appid\": \""+appId+"\",\n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\": \"%s\"\n"
			    + "}";
		
		String Plain_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\", \n"
			    + "\"partnerid\": \""+PARNTER_ID+"\", \n"
			    + "\"appid\": \""+appId+"\", \n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\":"+Data_To_Encrypt+"\n"
			    + "}";
		
		System.out.println("Data to Encrypt--> "+Data_To_Encrypt);
		System.out.println("\nPlain Payload--> "+Plain_Payload);
//		String encrypted = NSDL_Encrypt_Decrypt.EncryptPayload(Data_To_Encrypt, requestPublicCert);
		String encrypted = NSDL_Encrypt_Decrypt.encryptstring(Data_To_Encrypt, KEY);

	
		String payload = String.format(Req_Payload, encrypted);
		System.out.println("\nEncrypted payload--> "+payload);
		
		System.out.println("\nURL---> "+ url);
		String response = invokeUniRequest(url, payload,serviceType);
		
		return response;
	}
	
	
	/**
	 * This method is used to check the payout status
	 */
	public String payout_StatuChecker(String requestId,String txnType,String chanTxnRef,String usrTxnRef) {
		String url = BASE_URL+"/benePayOut";
		String serviceType = "STATUSENQBENEPAYOUT";
		
		String Data_To_Encrypt = "{\n" +
				"  \"requestId\": \""+requestId+"\",\n" +
				"  \"paymentdtls\": {\n" +
				"    \"txntype\": \""+txnType+"\",\n" +
				"    \"chantxnrefno\": \""+chanTxnRef+"\",\n" +
				"    \"usrtxnrefno\": \""+usrTxnRef+"\"\n" +
				"  }\n" +
				"}";
		
		String Req_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\",\n "
			    + "\"partnerid\": \""+PARNTER_ID+"\",\n "
			    + "\"appid\": \""+appId+"\",\n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\": \"%s\"\n"
			    + "}";
		
		String Plain_Payload = "{\n"
			    + "\"channelid\": \""+CHANNEL_ID+"\", \n"
			    + "\"partnerid\": \""+PARNTER_ID+"\", \n"
			    + "\"appid\": \""+appId+"\", \n"
			    + "\"servicetype\": \""+serviceType+"\",\n "
			    + "\"encdata\":"+Data_To_Encrypt+"\n"
			    + "}";
		
		System.out.println("Data to Encrypt--> "+Data_To_Encrypt);
		System.out.println("\nPlain Payload--> "+Plain_Payload);
//		String encrypted = NSDL_Encrypt_Decrypt.EncryptPayload(Data_To_Encrypt, requestPublicCert);
		String encrypted = NSDL_Encrypt_Decrypt.encryptstring(Data_To_Encrypt, KEY);

	
		String payload = String.format(Req_Payload, encrypted);
		System.out.println("\nEncrypted payload--> "+payload);
		
		System.out.println("\nURL---> "+ url);
		String response = invokeUniRequest(url, payload,serviceType);
		
		return response;
		
	}
	
		
	
	/**
	 * Constructor to create the refID
	 */
	public PayoutAPI() {
		// Unirest.setTimeouts(0, 0);
		String propertyPath = "./src/test/resources/bankIntg.properties";

		// Accessing the property file to fetch the data
		PropertiesUtility properties = new PropertiesUtility();
		properties.propertiesInit(propertyPath);
		int num = Integer.parseInt(properties.readData("requestId"));

		refID = "ESCROW0" + num;

		// Updating the number in property file
		properties.writeToProperties("requestId", String.valueOf(++num), propertyPath);
	}
	
	/**
	 * Method responsible for calling the specificed API and return the API response
	 */
	private String invokeUniRequest(String url, String payload, String serviceType) {
		
		kong.unirest.HttpResponse<String> response = null;
		
		if(serviceType.equals("GETBALANCE") || serviceType.equals("GETSTATEMENT") ) {
			response = Unirest.post(url)
					.header("chanid", CHANNEL_ID)
					.header("partnerid", PARNTER_ID)
					.header("servicetype", serviceType)
					.header("requestid", refID)
					.header("mode", "2")
					.header("Content-Type", "application/json")
					.body(payload)
					.socketTimeout(1200000)
					.asString();
		}else {
			response = Unirest.post(url)
					.header("chanid", CHANNEL_ID)
					.header("partnerid", PARNTER_ID)
					.header("servicetype", serviceType)
					.header("requestid", refID)
//					.header("mode", "2")
					.header("Content-Type", "application/json")
					.body(payload)
					.socketTimeout(1200000)
					.asString();			
		}
//		System.out.println(response.getHeaders().toString());
		return response.getBody();

	}
}
