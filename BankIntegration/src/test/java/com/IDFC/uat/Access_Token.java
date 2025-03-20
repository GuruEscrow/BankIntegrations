package com.IDFC.uat;

import java.io.FileReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bouncycastle.util.io.pem.PemReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import genricLibraries.PropertiesUtility;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class Access_Token {
	private static String scope;
	private static String client_id;
	
    private static String getJwtToken() {
    	
    	String jwt = null;
        try {
        	
        	PropertiesUtility property = new PropertiesUtility();
        	String path = "./src/test/resources/bankIntg.properties";
        	property.propertiesInit(path);
        	int jtiValue = Integer.parseInt(property.readData("jti"));
        	++jtiValue;
        	
            // Load the private key
            PrivateKey privateKey = loadPrivateKey("D:\\Phedora\\Keys\\IDFC\\idfc_uat.key");

            // Define the Header
            Map<String, Object> header = new LinkedHashMap<>();
            header.put("alg", "RS256");
            header.put("typ", "JWT");
            header.put("kid", "7b87f193-2e71-4aff-9379-865a2e3a8b88");

            // Define the Payload (Claims)
            Map<String, Object> claims = new LinkedHashMap<>();
            claims.put("jti", "ESCROWSTACK00"+jtiValue);
            property.writeToProperties("jti",""+ jtiValue, path);
            claims.put("sub", "dfacdeed-128d-41d8-b919-4b56d3e3e3f0");
            claims.put("iss", "dfacdeed-128d-41d8-b919-4b56d3e3e3f0");
            claims.put("aud", "https://app.uat-opt.idfcfirstbank.com/platform/oauth/oauth2/token");
            claims.put("exp", Epoch_Timstamp.getEpochTimestampInIST());

            // Generate the JWT
            jwt = Jwts.builder()
                .setHeader(header)                  
                .setClaims(claims)                  
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();                        

            // Output the JWT
//            System.out.println("Generated JWT: " + jwt);

        } catch (Exception e) {
            System.err.println("Error generating JWT: " + e.getMessage());
            e.printStackTrace();
        }
        return jwt;
    }

    // Helper method to load RSA private key from PEM file
    private static PrivateKey loadPrivateKey(String filePath) throws Exception {
        PemReader pemReader = new PemReader(new FileReader(filePath));
        byte[] keyBytes = pemReader.readPemObject().getContent();
        pemReader.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
    
    public static String getAccessToken() {
    	Dotenv credentails = Dotenv.configure().directory("./").filename("idbl.env").load();
        
    	Access_Token.scope = credentails.get("scope");
    	Access_Token.client_id = credentails.get("client_id");
    	
    	
    	String payload = "grant_type=" + URLEncoder.encode("client_credentials", StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(Access_Token.scope, StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(Access_Token.client_id, StandardCharsets.UTF_8) +
                "&client_assertion_type=" + URLEncoder.encode("urn:ietf:params:oauth:client-assertion-type:jwt-bearer", StandardCharsets.UTF_8) +
                "&client_assertion=" + URLEncoder.encode(getJwtToken(), StandardCharsets.UTF_8);
    	
    	String response = invokeUniRequest(payload);
    	
//    	System.out.println("Access_Token --> "+response);
    	JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
    	String access_token = jsonObject.get("access_token").getAsString();
    	
    	return access_token;
    	
    }
    
	private static String invokeUniRequest(String payload){
		
		HttpResponse<String> response = Unirest.post("https://app.uat-opt.idfcfirstbank.com/platform/oauth/oauth2/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .body(payload)
                .socketTimeout(1200000)
                .asString();

//        System.out.println("Response code --> " + response.getStatus());
        return response.getBody();
	}
	
	public static void main(String[] args) {
		System.out.println(getAccessToken());
	}
}