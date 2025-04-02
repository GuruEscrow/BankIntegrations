package com.IDFC.uat;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
		api.getStatment("18/03/2025","21/03/2025");
	}
	
	@Test
	public void payout() {
		IDFC_APIs api = new IDFC_APIs();
		String txnID = "PAYOUT047";
		String beneAccNo = "10000011836";    //Mandatory for all MOP
		String beneIFSC = "";   //Mandatory except IFT
		String beneName = "";              //Mandatory except IFT, IMPS
		String beneAdd = "";               //Mandatory except IFT, IMPS
		String email = "";                 //Mandatory for NEFT
		String mob = "";         //Mandatory for IMPS
		String mode = "IFT";              //Mandatory for all MOP
		String amt = "12.00";              //Mandatory for all MOP
		
		
		
//		String txnID = "PAYOUT039";
//		String beneAccNo = "6881783528";    //Mandatory for all MOP
//		String beneIFSC = "IDIB000B136";   //Mandatory except IFT
//		String beneName = "Saraswathi";              //Mandatory except IFT, IMPS
//		String beneAdd = "Bengaluru";               //Mandatory except IFT, IMPS
//		String email = "abcd@xyz.tech";                 //Mandatory for NEFT
//		String mob = "919591137559";         //Mandatory for IMPS
//		String mode = "RTGS";              //Mandatory for all MOP
//		String amt = "200001.00";              //Mandatory for all MOP
		
		
		api.payoutForAllMop(txnID,beneAccNo, beneIFSC, beneName, beneAdd, email, mob, mode, amt);
	}
	
	@Test
	public void txnStatus() {
		IDFC_APIs api = new IDFC_APIs();
		String statusMode = "IFT";   
		String txnRefNum = "ESCROW00155";   //For Imps need pass txnRefno here
		String payRefNum = "";               //For NEFT and RTGS need pass txnRefno here
		String txnDate = "25032025";
		
		api.txnStatus(statusMode,txnRefNum, payRefNum, txnDate);
	}
	
	@Test
	public void beneValidation() {
		IDFC_APIs api = new IDFC_APIs();
		
		/**
		 * Below are different identifiers 
		 * IMPS1.0: IMPS Penny Drop API (Including IFT)	
		 * IMPS2.0: IMPS A/c Validation API (without penny drop) 
		 * IFT2.0: Intra IDFC Bene Validation Without Penny Drop
		 * NACH: NACH Bene Validation API
		 * Auto: system decides best routing
		 */
		String identifier = ""; //IMPS1.0 IMPS2.0 IFT2.0 NACH Auto
		String beneAccNo = "10000619844";
		String beneIFSC = "IDFB0002233";
		String txnRef = "VALIDATION014";
		String merchCode = "";
		
	    api.beneValidation(identifier, beneAccNo, beneIFSC, txnRef,merchCode);
	}
	
	@Test
	public void decrypt() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		IDFCIV_Encrypt_Decrypt decrypt = new IDFCIV_Encrypt_Decrypt();
		
		String dec = "VlsyYTlra0pFRzJqVn5SbGLxWceGivf6TfR+OWrTn8zisdAME6LDtb6zMoW1UN8vgUFHctoZAgHVZPdlASZdYXL8gXL7zRcY0yncFYoRmr09PROsUx2VeJFgCOKBgu8x7GRqJEROba6GRmTU/O9YNzxnEOwcCkal1roH4sWWvJMbjoiXrr+TjkjYnFB7E3QTdsLGyOYshvdrH9aINzGEMGnZhIhTdiyB6QsbUNoauFZu/SEFc5beiDx5yO3gQKvseWLfZBsrJC1Zhj3D0IA5qZfPpMa5RqFwvW30/9ZmVQH1OefV+9688HuROUC0m+3HC81bla5afitCx487XRVDllU+HE+aLY9dccCh6jDF4lIcJiYAUn7mB5SFmxQl1LRDlSJESBzGEizQEp3zWGYkKu6ykXaqDT2fH8VEQ6IQjISr77paXdYCkE7BbxYXMnAlUHTdxnLVjEDrXARUnITK1smMQKROdrQxWlJO2wWaVNnLWU7IrNWt6EilFcAb+j+ABS4bB6uEXVmwiRs/ZqOrWGta7huduOXYjNMEpFUeMUZCE11IC1iU3jcutIOifo0xjX/JG/zNys2Q6r3j8gB0i9rv0j8ZcoEx5BsIOPv8fUMf7HzGvlCQlI0ugsXlNJZm613Uw9hkCUfE/DDfSEkZ74xrXh+VMvj5BI5k3ubS5fmoJtyMGT9hTJxRW2gkSIAtpj2fOnubAK4eWXsjSXXWf3mIhcdA/YT3EAzjqHt4IelU/kXv0jqCXoIrC9jLhJSfjkMl6hC/88oXjQ/DmkBq69wajjieHZcqZh+ZAH+IrlInkfsZZn8Q4zzH1hXP+JcewK2Qq4XeaFu9M9AkVP79EQ==";
		
		System.out.println(decrypt.decrypt(dec, "12316d706c65445467654143536b959123616d706c65496488621144636b7562"));
	}
}
