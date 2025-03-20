package RBL.mysql.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertDataIntoDB {

	// JDBC URL, username, and password of the MySQL database
	static final String dataBase = "RBL_Bank"; //3.109.189.244  172.18.172.11
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
	
	public static void insertPayloadWithUserRef(String tranID, String req_payload, String mode)
			throws SQLException, ClassNotFoundException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");
			String query = "INSERT INTO "+dataBase+".payouts_log (tranID,req_payload,mode)" + "VALUES ('" + tranID
					+ "', '" + req_payload + "', '" + mode + "')";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

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
	}

	public static void updateTxnResponse(String utr_rrn, String poNum, String txn_response, String tranID)
			throws SQLException, ClassNotFoundException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (utr,txn_response)" + "VALUES ('" + utr + "', '"
//					+ txn_response + "')";
			String query = "UPDATE "+dataBase+".payouts_log SET utr_rrn = '" + utr_rrn + "', poNum = '" + poNum + "', txn_response = '" + txn_response
					+ "' WHERE tranID='" + tranID + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

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
	}

	public static void updateTimestampsWithDelay(String initiateTS, String responseTS, long delay, String tranID)
			throws SQLException, ClassNotFoundException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (initiated_timestamp,response_timestamp,delay_in_response_secs)"
//					+ "VALUES ('" + initiateTS + "', '" + responseTS + "', '" + delay + "')";
			String query = "UPDATE "+dataBase+".payouts_log SET initiated_timestamp = '" + initiateTS
					+ "', response_timestamp = '" + responseTS + "', delay_in_response_secs = '" + delay
					+ "' WHERE tranID='" + tranID + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

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
	}

	public static void updateTxnStatus(String txnStatus, String tranID,String status) throws SQLException, ClassNotFoundException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (txn_status)" + "VALUES ('" + txnStatus + "')";
			String query = "UPDATE "+dataBase+".payouts_log SET txn_status = '" + txnStatus + "', status = '" + status +"' WHERE tranID='"
					+ tranID + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

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
	}

	public static void updateBankStmWithTimeStamp(String statement, String stmTimeStamp, String tranID)
			throws SQLException, ClassNotFoundException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (bank_statement,stm_gen_timestamp)" + "VALUES ('"
//					+ statement + "', '" + stmTimeStamp + "')";
			String query = "UPDATE "+dataBase+".payouts_log SET bank_statement = '" + statement
					+ "', stm_gen_timestamp = '" + stmTimeStamp + "' WHERE tranID='" + tranID + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

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
	}

	public static void insertStmStatus(String utr_rrn, String tranID, String reason)
			throws SQLException, ClassNotFoundException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");
			String query = "INSERT INTO "+dataBase+".stm_status_notfound (utr,tranID,reason)" + "VALUES ('" + utr_rrn
					+ "', '" + tranID + "', '" + reason + "')";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

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
	}
	
	public static void deleteStmsFoundUTR(String utr)
			throws SQLException, ClassNotFoundException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");
			String query = "Delete from "+dataBase+".stm_status_notfound where (utr = '" + utr + "')";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

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
	}
	
	public static void updateTxnId(String id,String count) throws ClassNotFoundException, SQLException {
		updateUrl();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");

//			String query = "INSERT INTO canarabank.payouts_log (bank_statement,stm_gen_timestamp)" + "VALUES ('"
//					+ statement + "', '" + stmTimeStamp + "')";
			String query = "UPDATE "+dataBase+".TxnID SET count = '" + count
					+ "' WHERE ID ='" + id + "'";
//			System.out.println("updateTxnID query--> "+query);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

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
	}

}
