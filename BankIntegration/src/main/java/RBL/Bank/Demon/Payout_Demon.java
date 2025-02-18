package RBL.Bank.Demon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonParser;

import RBL.mysql.database.InsertDataIntoDB;
import RBL.Bank.Demon.PayoutAPI;

public class Payout_Demon {

	public void payout(int key) {

		PayoutAPI rblPayoutApi = new PayoutAPI();
		try {
			String accName = null;
			String accNum = null;
			String ifscNum = null;
			String amt = null;
			String txnNote = null;

			// Date Formats
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyy-MM-dd");

			switch (key) {
			case 1:
				// Valid bank account details for imps
				accName = "PhedoraAxisBank";
				accNum = "123456073";//"924020017800104"
				ifscNum = "NPCI0000001"; //"UTIB0004575"
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 2:
				// INValid bank account details for imps
				accName = "InvaidBank";
				accNum = "137104000182232";
				ifscNum = "IBKL0000137";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 3:
				// Closed bank account details for imps
				accName = "ClosedAccount";
				accNum = "39676197828";
				ifscNum = "SBIN0003474";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 4:
				// Frozen bank account details for imps
				accName = "FrozenAccount";
				accNum = "64701000054019";
				ifscNum = "IOBA0000647";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 5:
				// NRE bank account details for imps
				accName = "NREAccount";
				accNum = "99982107442353";
				ifscNum = "FDRL0002421";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

//			NEFT Account details
			case 6:
				// Valid bank account details for NEFT
				accName = "PhedoraAxisBank";
				accNum = "924020017800104";
				ifscNum = "UTIB0004575";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 7:
				// INValid bank account details for NEFT
				accName = "InvaidBank";
				accNum = "137104000182232";
				ifscNum = "IBKL0000137";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 8:
				// Closed bank account details for NEFT
				accName = "ClosedAccount";
				accNum = "39676197828";
				ifscNum = "SBIN0003474";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 9:
				// Frozen bank account details for NEFT
				accName = "FrozenAccount";
				accNum = "64701000054019";
				ifscNum = "IOBA0000647";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

			case 10:
				// NRE bank account details for NEFT
				accName = "NREAccount";
				accNum = "99982107442353";
				ifscNum = "FDRL0002421";
				amt = "1";
				txnNote = "OtherBankTransfer";
				break;

//				Internal Transfer
			case 11:
				// NRE bank account details for NEFT
				accName = "CanarBank";
				accNum = "120029437735";
				amt = "1";
				txnNote = "InternalBankTransfer";
				break;

			default:
				break;
			}

//			If key is 1 t0 5 imps payout will happen to other bank
			if (key >= 1 && key <= 5) {

				// Initiating the payout
				Map<String,String> payoutResponse = rblPayoutApi.imps_Payout(amt, ifscNum, accNum, accName);

				// Fetching details in payout details map
				String tranID = payoutResponse.get("tranID");
				String utr_rrn = payoutResponse.get("utr_rrn");

			    if(utr_rrn != null) {
			    	// Updating the payout status into DB
				  String status =	rblPayoutApi.imps_payout_StatusChecker(utr_rrn,tranID);
			    }else {
			    	InsertDataIntoDB.updateTxnStatus("UTR_Notfound", tranID, "Notfounds");
			    }
				
			    
			    String fromDate = LocalDate.now().format(dateFormat);
			    String toDate = LocalDate.parse(fromDate,dateFormat).plusDays(1).format(dateFormat);
	
                Map<String, String> stmtMap = rblPayoutApi.getStatements(fromDate, toDate,"B");
                if(stmtMap.containsKey(utr_rrn)) {
                	String stmtTimeStamp = JsonParser.parseString(stmtMap.get(utr_rrn)).getAsJsonObject().get("pstdDate").getAsString();
                	InsertDataIntoDB.updateBankStmWithTimeStamp(stmtMap.get(utr_rrn), stmtTimeStamp, tranID);
                }

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


////	To run the payout continuously for 24 hours with 1 hour interval
	public static void main(String[] args) throws InterruptedException {
//        final Payout_Demon testDemon = new Payout_Demon();
//
//        // Main scheduler for 24 hours
//        final ScheduledExecutorService mainScheduler = Executors.newScheduledThreadPool(1);
//
//        // Running the task every 1 hour
//        mainScheduler.scheduleAtFixedRate(() -> {
//
//            // Create scheduler for 1 minute
//            final ScheduledExecutorService taskScheduler = Executors.newScheduledThreadPool(1);
//
//            // Run the payout task every 5 seconds for 1 minute
//            taskScheduler.scheduleAtFixedRate(() -> {
//                // Run task in a separate thread
//                CompletableFuture.runAsync(() -> testDemon.payout(1));
//            }, 0, 5, TimeUnit.SECONDS);
//
//            // Stop the task scheduler after 1 minute
//            taskScheduler.schedule(() -> {
//                if (!taskScheduler.isShutdown()) {
//                    taskScheduler.shutdown();
//                    System.out.println("Task scheduler stopped after 1 minute.");
//                }
//            }, 1, TimeUnit.MINUTES); // Corrected to stop after 1 minute
//
//        }, 0, 1, TimeUnit.HOURS); // Runs every 1 hour (corrected)
//
//        // Stop the main scheduler after 24 hours
//        mainScheduler.schedule(() -> {
//            if (!mainScheduler.isShutdown()) {
//                mainScheduler.shutdown();
//                System.out.println("Main scheduler shutdown after 24 hours.");
//            }
//        }, 24, TimeUnit.HOURS); // Stop after 24 hours
		while(true) {
			Payout_Demon test = new Payout_Demon();
			System.out.println("Started");
			test.payout(1);
			System.out.println("Ended");
			TimeUnit.MINUTES.sleep(1);
			System.out.println("one minute sleep over");
		}
    }

}
