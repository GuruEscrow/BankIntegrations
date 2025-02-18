package com.NSDL.uat;

import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import kong.unirest.Unirest;

public class NSDL_Account {

	private String requestPublicCert = "MIID8zCCAtsCFArb7AyTjShG7TM6TmsurH8b8wbFMA0GCSqGSIb3DQEBCwUAMIG1MQswCQYDVQQGEwJJTjEUMBIGA1UECAwLTWFoYXJhc2h0cmExDzANBgNVBAcMBk11bWJhaTEgMB4GA1UECgwXTlNETCBQYXltZW50cyBCYW5rIEx0ZC4xHzAdBgNVBAsMFkluZm9ybWF0aW9uIFRlY2hub2xvZ3kxFDASBgNVBAMMC1BBUlRORVJDRVJUMSYwJAYJKoZIhvcNAQkBFhduc2RscGJpdEBuc2RsYmFuay5jby5pbjAeFw0yMzEyMDcwNDU4MTBaFw0yNTExMjYwNDU4MTBaMIG1MQswCQYDVQQGEwJJTjEUMBIGA1UECAwLTWFoYXJhc2h0cmExDzANBgNVBAcMBk11bWJhaTEgMB4GA1UECgwXTlNETCBQYXltZW50cyBCYW5rIEx0ZC4xHzAdBgNVBAsMFkluZm9ybWF0aW9uIFRlY2hub2xvZ3kxFDASBgNVBAMMC1BBUlRORVJDRVJUMSYwJAYJKoZIhvcNAQkBFhduc2RscGJpdEBuc2RsYmFuay5jby5pbjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANwmYgCcWgVu4ifAU9hxfRV5a9ECPtkwmgKP/l0qTxR+Uq72t7dnG8qfXUc8SMhcfHWiILE3wfDG3P7E6X+X/PtlyqbG5mQ8EvQgGhzkQcvrS5JM11m7EupSaSshL0CZgGjlMPR8hCupkId3i3wER/rvUoIN7t2l4aCahtc0n1gAjcmDO/Q/Fb2lhepXmC8+3dVhXxrBgzrHIu51ddJ8supfA6KDzkdmnnRJnkvHNdbjzfnlWG832JWWJGzcroK7F09RtHbSOIOgyAi/MU5yqu/dWBWNj6atpyaLX3ImyndBFh4+AfyDQxIxyCrQQuETNrivB3gihMpGphvL2mqO5psCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAeHDTFbpnT2EInpdWw0cRpjia65u/O15wH+IPz0HG6U3ArrnBF+D9ZXW6LYqKMcRpe7YWZ6aTWtRG2diKKZNQvUkNs25AUMIQVDpRUraAz9KoIObkUPYEcMlowroUjBRHc4xGto25UUbYeYvpgyzpb2TBB3VNP74blGktMtRWBANoSEbNqgj1h8ePHyIDfnFf0AdncCjdkQ3EPXh3L4w5+GVETmytpNwIq63CSGmBI1IxEoUkQcFQkJgdp+GhI3V+3vaNic5QkmzLypqD7FAZFpQ5DfYqkcozhdKYw5AwpXH7eB+zM4Pmjfzx0MEGvvV72Qy5iKuSDaJEITFimmAhgA==";

	private String responsePublicCert = "MIIEKTCCAxGgAwIBAgIUDRtZcLAPpDnWUC5QGIDrxActutYwDQYJKoZIhvcNAQELBQAwgaIxCzAJBgNVBAYTAklOMRQwEgYDVQQIDAtNQUhBUkFTSFRSQTEUMBIGA1UEBwwLTkFWSSBNVU1CQUkxITAfBgNVBAoMGFBBUlRORVIgTkVPIEJBTktJTkcgTFRELjEQMA4GA1UECwwHVEVTVElORzERMA8GA1UEAwwIQkFOS0NFUlQxHzAdBgkqhkiG9w0BCQEWEFBBUlRORVJAVEVTVC5DT00wIBcNMjMxMjA3MDUwODExWhgPMjIyMTAxMjMwNTA4MTFaMIGiMQswCQYDVQQGEwJJTjEUMBIGA1UECAwLTUFIQVJBU0hUUkExFDASBgNVBAcMC05BVkkgTVVNQkFJMSEwHwYDVQQKDBhQQVJUTkVSIE5FTyBCQU5LSU5HIExURC4xEDAOBgNVBAsMB1RFU1RJTkcxETAPBgNVBAMMCEJBTktDRVJUMR8wHQYJKoZIhvcNAQkBFhBQQVJUTkVSQFRFU1QuQ09NMIIBIjANBgkqhkiG9w0BAQEFAAOC AQ8AMIIBCgKCAQEAla0p4XsyeFkMpF2JS+Dc90OAkZCb0onLNPHmW4f2+BgiJ3142wZ095jePhtESFGRVAlcg2VLHkfQCN0pkYWpbzTVoAUG9RL8yxSlJ/UxJcwHoPOoCr/UCQJ4FZDVkjjSMdW+JPF9O5PuRilVLDEKqaIvL4XsL8rtSJCUR7vut3BdirFi44HoM3QK6B/C2Wv2zCDKtjJDIddFKvLXjwa2XVxKh8RFWnLCax5O6QyfgKpGlhI3b+DHvhJ+pJsf342P6WdFDUsJVXiCmFBcmeAE88gtOwPYJAeLYFLfG3IZch52HjtYrVDP3+MUTSiYHsko+hwMNCiXCgn67/UpdQ9ySwIDAQABo1MwUTAdBgNVHQ4EFgQUBlHJd2w0TGQJxO4hAL+6tNGqb4swHwYDVR0jBBgwFoAUBlHJd2w0TGQJxO4hAL+6tNGqb4swDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAigVRi7kwou8IzfAa7Gma+xWZjtLJfeilrcOLyW1BAnlpwS24qK+9xebZKRaeiZIhpX+IwFpId/T8/YS/iLvUt5TPaJ22rDCX1DAJotPyt342+iLtiERlUfHJXPApw5ykl6xsML1818cPlxhP+660DgFFide3Sy7Nm2RabizuMcBiCr6kfpteoTIZYm18ZkvaDUyfHlU0TQEVhAJWmbCLSObIcCNnH6vugxPLQknBZdu7HRGeVaGEz1FEbigNWz8EbaFjxTVADjR9fwFKKS13Qbkg1OgBX4dww2ForKC4Fwqb4gKG+7InkiaT0jdUvkeEKPWceIEIKbA01XB29Fr1Tw==";
	
	private String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCVrSnhezJ4WQykXYlL4Nz3Q4CRkJvSics08eZbh/b4GCInfXjbBnT3mN4+G0RIUZFUCVyDZUseR9AI3SmRhalvNNWgBQb1EvzLFKUn9TElzAeg86gKv9QJAngVkNWSONIx1b4k8X07k+5GKVUsMQqpoi8vhewvyu1IkJRHu+63cF2KsWLjgegzdAroH8LZa/bMIMq2MkMh10Uq8tePBrZdXEqHxEVacsJrHk7pDJ+AqkaWEjdv4Me+En6kmx/fjY/pZ0UNSwlVeIKYUFyZ4ATzyC07A9gkB4tgUt8bchlyHnYeO1itUM/f4xRNKJgeySj6HAw0KJcKCfrv9Sl1D3JLAgMBAAECggEADa49BeaITOwQFeqUv4eR5X8ufl+XgS1mEkn1R7LMQI3tepWN2TB5ush8BnTgj172kmtOoOs+a/SdZQoDStFK6ia2oJXaYRvMrlU4k/KQfPIX/76gQpHuXqpym79YE1w33iU5aCVd0HcWJXnmq+Te+B/3rHd9szqa6JyKaRTE3iWhCxYvBtQmkwa/FtyW5aljUHf6Iy+eJd4i5zMuEMdQEYZZrKOCfLnX+APH9a78P+Pkz4mo8SzL2EVTmqDBHFghjQV5iGJKkvoGV13dGUZqkgvOt6gl8HDoKIMPjoCp4JQJZ8dQJ4LYRRinkffW1Fg+m7bGP1yy3ihKNCS1qiRl9QKBgQC1K+phhhz3IL7aVs+KB+k94Bdg79fmW9ipYZ/rLlkmuG62AxUdNdnVupSlGed0B/f83yeckmYTPDWAesY1Y+qGzA6/XbxAsSPeoaOVUDaur1CI0RRTpan7j81ADfCzn2NK33AXD96RRe+JDeJwI2DyjvV8eN1bavgxVeO+ivOG3wKBgQDTfyBgj3BDkZf1wd5ueIGiMwgG3jQR/e6QRp/YXxvp8Io1c3JCPOaBNnw7GBeL+e0VxzF4a1c7mFtXlbSIXpgT/7TwInv5ahcr6BQlUu11cgtM8L9HJ88Q9D9jQRfvkUbDD6OQW5ieAae6oTr/DcYJMM5IhsQBDo5ByZSpw+DeFQKBgGwk2rN8Kq4ZjdMnHpt2PXQB3/KtOGL5UEtLSvpHoE/Mp8ld7aySCIXb6f7jh5VrbEw1qJ99hBDXT9hxcP9NtSDhjawNd+Hl21N5iPwH9ZwwahJBO7DyopTXMd2adKPD8LEwzuf1QRXWeTWGZDQr6iLf0wmJ3BhFsgFBE8EZkGRhAoGAejsahzPe+e4HGO12k/npUciUdwOsrElJuvBWKUKua0GtXr7d5hI0VbEbkzuVQDytHM0GdkXzIGopQDExKS8iTUyXUcFcTQVXZvS1z+xheGL0zP7GTWVgcrf2enKAVuBrOQisyx1k0sy6F05fPlDDh0RBtADO/p1e8f6CgKrIJxECgYBVWS5qqjfrWqpQjxpA+6NdXzoREA0bRD2zSj9g45fr3YuOBpGFk8BlGa4E9l964MrPKfWadV0Zp9U2stN/+ydxalB7vuoPgYCyxr+CI0vMf3LArGtBGTEqkb1Ot+WhRMLo4/3P0xGGKwM/ne/lulhha+bv1Flz2X5lSfbHyNCY+A==";

	private String CHANNEL_ID = "SePufKBkoStBoDPxFHSV";

	private String PARNTER_ID = "c377bbe133c18bab6360b3f3ed5d8f3f";

	private String appId = "com.mobile.nsdlpb";
	
	private String BASE_URL = "https://jiffyuat.nsdlbank.co.in/jarvisgwy/";
	
	public void create_VAN() {
		String url = BASE_URL+"/crePrtnrCustandAccnt";
		
		String req_payload = "{\n" +
			    "  \"channelid\": \""+CHANNEL_ID+"\",\n" +
			    "  \"partnerid\": \""+PARNTER_ID+"\",\n" +
			    "  \"reqdtls\": {\n" +
			    "    \"reqtype\": \"CRECUSTANDACCOUNT\",\n" +
			    "    \"chantxnrefno\": \"703021133920001020211\",\n" +
			    "    \"usrtxnrefno\": \"703021133920001020211\"\n" +
			    "  },\n" +
			    "  \"custdetails\": {\n" +
			    "    \"custtype\": \"USERS\",\n" +
			    "    \"custprodcode\": \"ISSUER\",\n" +
			    "    \"custfname\": \"Sharad\",\n" +
			    "    \"custmname\": \"Sharad\",\n" +
			    "    \"custlname\": \"Dubey\",\n" +
			    "    \"custshortname\": \"Dubey\",\n" +
			    "    \"mobile\": \"9594432548\",\n" +
			    "    \"email\": \"shradh.dubey@gmail.com\",\n" +
			    "    \"uniqueIDtype\": \"ECOLLECT\",\n" +
			    "    \"uniqueID\": \"ECXX100101000311\"\n" +
			    "  },\n" +
			    "  \"acntdetails\": {\n" +
			    "    \"acnttype\": \"ALLOCATION\",\n" +
			    "    \"acntprodcode\": \"DEFAULT\"\n" +
			    "  },\n" +
			    "  \"reqUserId\": \"13898\",\n" +
			    "  \"signcs\": \"%s\",\n" +
			    "  \"token\": \"NA\"\n" +
			    "}";
		
		String payload = String.format(req_payload,"" );//signaturechecksum(req_payload, req_payload)
		System.out.println("Checksum payload--> "+payload);
		
		
		String response = invokeUniRequest(url, req_payload, payload);
		
		System.out.println(response);
	}
	
	
//	public String signaturechecksum(String secretkey, String checksum) {  
//		String hash = null;  
//		try {  
//		Mac SHA512_HMAC = Mac.getInstance("HmacSHA512");  
//		SecretKeySpec secret_key = new SecretKeySpec(secretkey.getBytes(), 
//		"HmacSHA512");  
//		SHA512_HMAC.init(secret_key);  
//		//hash = 
//		Base64.encodeBase64String(SHA512_HMAC.doFinal(checksum.getBytes()));  
//		Encoder encoder =Base64.getEncoder();  
//		byte[] bytesEncoded = encoder.encode(SHA512_HMAC.doFinal(checksum.getB
//		 ytes("UTF-8")));  
//		hash = new String(bytesEncoded);  
//		} catch (Exception e) {  
//		}  
//		return hash;  
//		} 
	
	/**
	 * Method responsible for calling the specificed API and return the API response
	 */
	private String invokeUniRequest(String url, String payload, String serviceType) {
		// Unirest.setTimeouts(0, 0);
		String propertyPath = "./src/test/resources/bankIntg.properties";

		// Accessing the property file to fetch the data
//		PropertiesUtility properties = new PropertiesUtility();
//		properties.propertiesInit(propertyPath);
//		int num = Integer.parseInt(properties.readData("requestId"));
//
//		// Updating the number in property file
//		properties.writeToProperties("requestId", String.valueOf(++num), propertyPath);
		
		kong.unirest.HttpResponse<String> response = Unirest.post(url)
//				.header("chanid", CHANNEL_ID)
//				.header("partnerid", PARNTER_ID)
//				.header("servicetype", serviceType)
//				.header("requestid", "ESCROW" + num)
				.header("Content-Type", "application/json")
//				.header("mode", "2")
				.body(payload).asString();
		return response.getBody();

	}
}
