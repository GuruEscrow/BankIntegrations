package com.Axis.uat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ChecksumUtility {

	public static Object validateInfo(String value) {
		return StringUtils.isNotEmpty(value) && "null" != value ? value : StringUtils.EMPTY;
	}

	
	public String generateCheckSum(LinkedHashMap<String, Object> requestMap) throws Exception {
		StringBuilder finalChkSum = new StringBuilder();
		StringBuilder keys = new StringBuilder();
		try {
			if (null == requestMap) {
				return null;
			}

			for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
				if (!entry.getKey().equals("checksum")) {
					if (entry.getValue() instanceof List) {
						List<Object> tempLst = ((List) entry.getValue());
						if (!CollectionUtils.isEmpty(tempLst) && (tempLst.get(0) instanceof Map)) {
							List<? extends Map<String, Object>> innerObjectMap = (List<? extends Map<String, Object>>) entry
									.getValue();

							for (Map<String, Object> innerMap : innerObjectMap) {
								for (Entry<? extends String, ? extends Object> entryInn : innerMap.entrySet()) {
									keys.append(entryInn.getKey());

									finalChkSum.append(getInnerLevel2Map(entryInn.getValue(), finalChkSum));
								}
							}
						} else if (!CollectionUtils.isEmpty(tempLst)) {
							for (Object strValues : tempLst) {
								finalChkSum.append(validateInfo(String.valueOf(strValues)));
							}
						}

					} else if (entry.getValue() instanceof Map) {
						Map<? extends String, ? extends Object> innerObjectMap2 = (Map<? extends String, ? extends Object>) entry
								.getValue();
						for (Entry<? extends String, ? extends Object> entryInn : innerObjectMap2.entrySet()) {
							keys.append(entryInn.getKey());
							finalChkSum.append(validateInfo(String.valueOf(entryInn.getValue())));
						}
					} else {
						finalChkSum.append(validateInfo(String.valueOf(entry.getValue())));
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return String.valueOf(encodeCheckSumWithSHA256(finalChkSum.toString().trim()));
	}
	
	private String getInnerLevel2Map(Object entryInnLvl2, StringBuilder finalChkSum123) {
		StringBuilder finalChkSum = new StringBuilder();
		StringBuilder keys = new StringBuilder();
		if (entryInnLvl2 instanceof List) {
			List<Object> tempLst = ((List) entryInnLvl2);
			if (!CollectionUtils.isEmpty(tempLst) && (tempLst.get(0) instanceof Map)) {

				List<? extends Map<String, Object>> innerObjectMap = (List<? extends Map<String, Object>>) entryInnLvl2;
				for (Map<String, Object> innerMap : innerObjectMap) {
					for (Entry<? extends String, ? extends Object> entryInn : innerMap.entrySet()) {
						keys.append(entryInn.getKey());
						finalChkSum.append(validateInfo(String.valueOf(entryInn.getValue())));
					}
				}
			} else if (!CollectionUtils.isEmpty(tempLst)) {
				for (Object strValues : tempLst) {
					finalChkSum.append(validateInfo(String.valueOf(strValues)));
				}
			}

		} else if (entryInnLvl2 instanceof Map) {
			Map<? extends String, ? extends Object> innerObjectMap2 = (Map<? extends String, ? extends Object>) entryInnLvl2;
			for (Entry<? extends String, ? extends Object> entryInn : innerObjectMap2.entrySet()) {
				keys.append(entryInn.getKey());

				finalChkSum.append(validateInfo(String.valueOf(entryInn.getValue())));
			}
		} else {
			finalChkSum.append(validateInfo(String.valueOf(entryInnLvl2)));
		}
		return finalChkSum.toString(); 
	}
	
	public static String encodeCheckSumWithSHA256(String data) {
		MessageDigest md;
		StringBuilder sb = new StringBuilder();
		String response = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(data.getBytes(StandardCharsets.UTF_8));
			// Get the hashbytes
			byte[] hashBytes = md.digest();
			// Convert hash bytes to hex format
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}
			response = sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Internal server error");
		}
		return response;
	}
}
