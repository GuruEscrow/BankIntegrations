package com.IDFC.uat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class Epoch_Timstamp {

	public static long getEpochTimestampInIST() {
		// Current date and time
        LocalDateTime now = LocalDateTime.now();
        // Add 22 hours to current time
        LocalDateTime nowPlus22Hours = now.plusHours(22);
		
        ZoneId istZone = ZoneId.of("Asia/Kolkata");
        ZoneOffset istOffset = istZone.getRules().getOffset(nowPlus22Hours);
        return nowPlus22Hours.toEpochSecond(istOffset);
    }

//	public static void main(String[] args) {
//		System.out.println(getEpochTimestampInIST());
//	}
}
