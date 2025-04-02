package RBL.Bank.Demon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.JsonParser;

import RBL.mysql.database.InsertDataIntoDB;

public class Payout_Demon {
	private static final AtomicInteger payoutCount = new AtomicInteger(0);
	private static int impsPayoutCount = 1;
	
	public void  payout(int key,String mode) {
		
		PayoutAPI rblPayoutApi = new PayoutAPI();
		try {
			String accName = null;
			String accNum = null;
			String ifscNum = null;
			String amt = null;
			String txnNote = null;
			String bankName = null;

			// Date Formats
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyy-MM-dd");
			DateTimeFormatter hrsFormat = DateTimeFormatter.ofPattern("HH");
			
			//Fetching hours
			String hours = LocalDateTime.now().format(hrsFormat);

			switch (key) {
			case 1:
				// Valid bank account details for imps [Internal]
				accName = "MYGROUNDINT";
				accNum = "409002317855";//"924020017800104"
				ifscNum = "RATN0000317"; //"UTIB0004575"
				amt = "1."+hours;
				bankName = "Bene Bank";
				txnNote = "internal";
				break;

			case 2:
				// INValid bank account details for imps [other bank]
				accName = "MYGROUNDEXT";
				accNum = "50200067343103";
				ifscNum = "HDFC0000102";
				amt = "1."+hours;
				bankName = "Bene Bank";
				txnNote = "external";
				break;

			default:
				break;
			}

//			If key is 1 t0 5 imps payout will happen to other bank
			if (key >= 1 && key <= 5) {

				// Initiating the payout
				Map<String,String> payoutResponse = rblPayoutApi.imps_Payout(amt, ifscNum, accNum, accName,bankName,mode,txnNote);

				// Fetching details in payout details map
				String tranID = payoutResponse.get("tranID");
				String utr_rrn = payoutResponse.get("utr_rrn");
				String statusMode = payoutResponse.get("mode");
				String poNum = payoutResponse.get("poNum");
				
				String stmtUTR = null;
				if(statusMode.equals("NEFT")) {
					stmtUTR = poNum;
				}else {
					stmtUTR = utr_rrn;
				}

				if (utr_rrn != null) {
					// Updating the payout status into DB
					String status = rblPayoutApi.imps_payout_StatusChecker(utr_rrn, tranID,statusMode);

					//Checking and updating the statement accordingly
					String fromDate = LocalDate.now().format(dateFormat);
					String toDate = LocalDate.parse(fromDate, dateFormat).plusDays(1).format(dateFormat);

					Map<String, String> stmtMap = rblPayoutApi.getStatements(fromDate, toDate,"D", "", "", "", "", "", "");
					if (stmtMap.containsKey(stmtUTR)) {
						String stmtTimeStamp = JsonParser.parseString(stmtMap.get(stmtUTR)).getAsJsonObject()
								.get("pstdDate").getAsString();
						InsertDataIntoDB.updateBankStmWithTimeStamp(stmtMap.get(stmtUTR), stmtTimeStamp, tranID);
					}
				} else {
					InsertDataIntoDB.updateTxnStatus("UTR_Notfound", tranID, "Notfounds");
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


////	To run the payout continuously for 24 hours with 1 hour interval
	public static void main(String[] args) throws InterruptedException {
		
        final Payout_Demon testDemon = new Payout_Demon();

        /**
         * Code to run the program for 24 hours
         * it will do 2 payments per minute for 24 hrs 
         */
		final ScheduledExecutorService taskScheduler = Executors.newScheduledThreadPool(1);

		// Schedule the task to run every 333 milliseconds for 1 minute
		taskScheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// This line execute the task every time in separate thread
				Executors.newSingleThreadExecutor().submit(new Runnable() {
					@Override
					public void run() {
						int count = payoutCount.incrementAndGet();
						if (count % 2 == 0) {
							if(impsPayoutCount%2==0) {
								// Extrenal transfer
								testDemon.payout(2, "IMPS");
							}else {
								// Internal transfer
								testDemon.payout(1, "IMPS");
							}
							impsPayoutCount++;
						} else {
							// NEFT transfer
							testDemon.payout(2, "NEFT");
						}
					}
				});

			}
		}, 0, 30, TimeUnit.SECONDS); // Run every 333 ms

		// Stop the task scheduler after 1 minute
		taskScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				if (!taskScheduler.isShutdown()) {
					taskScheduler.shutdown();
					System.out.println("Task stopped after 1 minute.");
				}
			}
		}, 24, TimeUnit.HOURS); // Stop after 24 hrs
//        
//        /**
//         * Below code to do payouts for mentioned dealy for some given time (TPS checker)
//         */
////		final ScheduledExecutorService tpsScheduler = Executors.newScheduledThreadPool(1);
////
////		tpsScheduler.scheduleAtFixedRate(new Runnable() {
////			@Override
////			public void run() {
////				// Submit the task to another thread
////				Executors.newSingleThreadExecutor().submit(new Runnable() {
////					@Override
////					public void run() {
////						int count = payoutCount.incrementAndGet();
////						if(true) {
////							//Extrenal transfer
////							testDemon.payout(2,"NEFT");
////						}else {
////							//Internal transfer
//////							testDemon.payout(1,"NEFT");
////						}
////					}
////				});
////			}
////		}, 0,333 , TimeUnit.MILLISECONDS);
////
////		// To stop the payouts after 1 minutes and it will be there to execute all the
////		// threads
////		tpsScheduler.schedule(new Runnable() {
////			@Override
////			public void run() {
////				if (tpsScheduler != null) {
////					tpsScheduler.shutdown(); // Optionally shut down the scheduler
////				}
////			}
////		}, 15, TimeUnit.MINUTES);
//
	}
	

}
