package com.NSDL.uat;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class NSDL_Encrypt_Decrypt {

	private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";
	private static final int GCM_TAG_LENGTH = 128;
	private static final int GCM_IV_LENGTH = 12;
	private static final int AES_KEY_SIZE = 256;

	public static String encryptstring(String value, String password) {
		try {
			SecretKey originalKey = new SecretKeySpec(password.substring(0, 32).getBytes("UTF-8"), "AES");
			IvParameterSpec iv = new IvParameterSpec(password.substring(0, 16).getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, originalKey, iv);
			byte[] encrypted = cipher.doFinal(value.getBytes());
			return new String(Base64.getEncoder().encodeToString(encrypted));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String decryptpayload(String value, String key) {
		String decryptedData = null;
		try {
			IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.substring(0, 32).getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(value));
			decryptedData = new String(original);

			decryptedData = new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
			decryptedData = "NA";
		}
		return decryptedData;
	}
	
//	Old encryption logic
//	public static String encryptpayload(String value, String key) {
//		String encryptedData = null;
//		try {
//			IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes("UTF-8"));
//			SecretKeySpec skeySpec = new SecretKeySpec(key.substring(0, 32).getBytes("UTF-8"), "AES");
//			Cipher cipher = Cipher.getInstance(ALGORITHM);
//			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//
//			byte[] encrypted = cipher.doFinal(value.getBytes());
//			encryptedData = Base64.getEncoder().encodeToString(encrypted);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			encryptedData = null;
//		}
//		return encryptedData;
//	}
	
	
	/**
	 * Encryption code for Balance fetch
	 */
    public static String PGPEncryptPayload(String PlainRequest, String strkey, String striv, String PUBLIC_KEY) throws Exception {
        byte[] key = Base64.getDecoder().decode(strkey);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        byte[] IV = Base64.getDecoder().decode(striv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] plaintextbyte = PlainRequest.getBytes();
        byte[] encryptedtext = cipher.doFinal(plaintextbyte);
        String encryptedtextstr = Base64.getEncoder().encodeToString(encryptedtext);

        byte[] keyBytes = Base64.getDecoder().decode(PUBLIC_KEY.getBytes());
        X509EncodedKeySpec keySpecrsa = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpecrsa);

        byte[] keyrsaenc = RSAencrypt(key, publicKey);
        String keyrsaencstr = Base64.getEncoder().encodeToString(keyrsaenc);

        byte[] ivrsaenc = RSAencrypt(IV, publicKey);
        String ivrsaencstr = Base64.getEncoder().encodeToString(ivrsaenc);

        return encryptedtextstr + "^" + keyrsaencstr + "^" + ivrsaencstr;
    }

    public static byte[] RSAencrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String EncryptPayload(String PlainPayloadRequest, String PUBLIC_KEY) {
    	String parsedPublicKey = PUBLIC_KEY.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace("\r\n", "").trim();
        String encryptedPayloadStr = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(AES_KEY_SIZE, SecureRandom.getInstanceStrong());
            SecretKey key = keyGenerator.generateKey();
            String strkey = Base64.getEncoder().encodeToString(key.getEncoded());

            byte[] IV = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(IV);
            String striv = Base64.getEncoder().encodeToString(IV);

            encryptedPayloadStr = PGPEncryptPayload(PlainPayloadRequest, strkey, striv, parsedPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedPayloadStr;
    }
    
    /**
     * Decryption code
     */
    public static String PGPDecryptPayload(String EncryptedPayload, String PRIVATE_KEY) throws Exception {
    	String parsedPrivateKey = PRIVATE_KEY.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replace("\r\n", "").trim();
        String[] data = EncryptedPayload.split("\\^");
        byte[] keyBytes1 = Base64.getDecoder().decode(parsedPrivateKey.getBytes());
        PKCS8EncodedKeySpec keySpec1 = new PKCS8EncodedKeySpec(keyBytes1);
        KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory1.generatePrivate(keySpec1);

        byte[] keydecoded = Base64.getDecoder().decode(data[1]);
//        System.out.println("keydecoded:" + Base64.getEncoder().encode(keydecoded));
        byte[] ivdecoded = Base64.getDecoder().decode(data[2]);
//        System.out.println("ivdecoded:" + Base64.getEncoder().encode(ivdecoded));

        byte[] keyrsadec = RSAdecrypt(keydecoded, privateKey);
        byte[] ivrsadec = RSAdecrypt(ivdecoded, privateKey);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(keyrsadec, "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, ivrsadec);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] datadecoded = Base64.getDecoder().decode(data[0]);
        byte[] decryptedText = cipher.doFinal(datadecoded);

        return new String(decryptedText);
    }

    public static byte[] RSAdecrypt(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Checksum code
     */
    public static String signatureChecksum(String secretKey, String checksum) {
        String hash = null;
        try {
            Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
            sha512_HMAC.init(secret_key);
            byte[] hashBytes = sha512_HMAC.doFinal(checksum.getBytes("UTF-8"));
            hash = Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            e.printStackTrace(); // Optional: helpful during debugging
        }
        return hash;
    }
}
