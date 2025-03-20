package com.Axis.uat;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AxisAPI_Test {

	@Test
	public void verifyVPA() throws Exception {
		VPA_Api vpa = new VPA_Api();
		String response = vpa.vpa_Verification("1234567890");//9820570270
		
		System.out.println("Eencrypted response -->"+response);
		
		JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
		if(jsonResponse.has("VerifyVPAResponse")) {
			
			String encryptedData = jsonResponse.get("VerifyVPAResponse").getAsJsonObject().get("VerifyVPAResponseBodyEncrypted").getAsString();
			
			Encrypt_DecryptUtility decrypt = new Encrypt_DecryptUtility();
			
			System.out.println("Decrypted data -->"+decrypt.aes128Decrypt(encryptedData));
		}
		
	}
}
