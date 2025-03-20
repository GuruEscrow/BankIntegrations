package com.IDFC.uat;

import org.testng.annotations.Test;

public class IDFC_API_Test {

	@Test
	public void getBalance() {
		IDFC_APIs api = new IDFC_APIs();
		api.fetchBalance();
	}
	
	@Test
	public void getStatement() {
		IDFC_APIs api = new IDFC_APIs();
		api.getStatment("18/03/2025","20/03/2025");
	}
	
	@Test
	public void payout() {
		IDFC_APIs api = new IDFC_APIs();
		String txnID = "PAYOUT014";
		String beneAccNo = "123458960";    //Mandatory for all MOP
		String beneIFSC = "SBIN0012451";   //Mandatory except IFT
		String beneName = "Guru";              //Mandatory except IFT, IMPS
		String beneAdd = "Bengaluru";               //Mandatory except IFT, IMPS
		String email = "guru@gmail.com";                 //Mandatory for NEFT
		String mob = "9959644725";         //Mandatory for IMPS
		String mode = "RTGS";              //Mandatory for all MOP
		String amt = "200001.00";              //Mandatory for all MOP
		api.payoutForAllMop(txnID,beneAccNo, beneIFSC, beneName, beneAdd, email, mob, mode, amt);
	}
	
	@Test
	public void txnStatus() {
		IDFC_APIs api = new IDFC_APIs();
		String statusMode = "IMPS";
		String refNum = "YESB01032565285";
		String txnDate = "20032025";
		
		api.txnStatus(statusMode, refNum, txnDate);
	}
}
