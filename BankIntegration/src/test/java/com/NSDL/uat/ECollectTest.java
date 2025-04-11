package com.NSDL.uat;

import org.testng.annotations.Test;

public class ECollectTest {

	@Test
	public void ecollect() {
		ECollectsAPI vanCollect = new ECollectsAPI();
		
		String custfname = "Guru";
		String custmname = "Guru";
		String custlname = "Prasad";
		String custSname = "Prasad";
		String mob = "1234567890";
		String email = "guruprasad@escrowstack.io";
		String van = "ESTA0002";
	
	    String response = vanCollect.createVAN(custfname, custmname, custlname, custSname, mob, email, van);
	    
	    System.out.println(response);
		
	}
}
