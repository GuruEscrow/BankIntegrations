package RBL.mysql.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchDataFromDB {

	// JDBC URL, username, and password of the MySQL database
	static final String dataBase = "RBL_Bank";
	private static String jdbcUrl;
	static final String ec2url = "jdbc:mysql://localhost:3306/"+dataBase+"?useSSL=false";
	static final String eclipseurl = "jdbc:mysql://3.109.189.244:3306/"+dataBase+"?useSSL=false";
	static final String username = "Guruprasad";
	static final String password = "MySql@#123";

	public static void updateUrl() {
		String os = System.getProperty("os.name").toLowerCase();
		boolean isEC2 = os.contains("linux");
		jdbcUrl = isEC2 ? ec2url : eclipseurl;
	}
	
	public static Map<String, List> getStmtNotFoundUTR() throws ClassNotFoundException, SQLException {

		updateUrl();
		Map<String,List> txnWoStm_utr_map = new HashMap<String,List>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		String payout_timeStamp = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			String query = "SELECT * FROM "+dataBase+".stm_status_notfound";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String utr = resultSet.getString("utr");
				String user_ref = resultSet.getString("user_ref");
				String reason = resultSet.getString("reason");
				
				List <String> userref_reason_list = new ArrayList<String>();
				userref_reason_list.add(user_ref);
				userref_reason_list.add(reason);
				
				txnWoStm_utr_map.put(utr, userref_reason_list);
			}
		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
		
		return txnWoStm_utr_map;
	}
	
	public static Map<String, String> getUserRefWithTxnMode() throws ClassNotFoundException, SQLException {

		updateUrl();
		Map<String, String> txnWoStm_utr_map = new HashMap<String, String>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		String payout_timeStamp = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			
//			SELECT * FROM tpslog.payouts_log where txn_status is null
			String query = "SELECT * FROM "+dataBase+".payouts_log where utr/rrn != 'null' and status is null";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String user_ref = resultSet.getString("tranID");
				String utr = resultSet.getString("utr/rrn");
				String mode = resultSet.getString("mode");

				List<String> userref_reason_list = new ArrayList<String>();
				userref_reason_list.add(utr);
				userref_reason_list.add(mode);

				txnWoStm_utr_map.put(user_ref, mode);
			}
		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}

		return txnWoStm_utr_map;
	}
	
	public static String getTxnID(String id) throws SQLException, ClassNotFoundException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		String txnID = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			
//			SELECT * FROM tpslog.payouts_log where txn_status is null
			String query = "SELECT * FROM "+dataBase+".TxnID where ID = '"+id+"'";
			System.out.println("getTxnID query--> "+query);

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				
				txnID = resultSet.getString("count");
			}
		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}

		return txnID;
		
	}
}
