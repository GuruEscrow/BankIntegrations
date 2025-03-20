package RBL.Bank.Demon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;

import RBL.mysql.database.FetchDataFromDB;
import RBL.mysql.database.InsertDataIntoDB;

public class StatementFetch {
	
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
		

	public static void main(String [] args) throws ClassNotFoundException, SQLException {
		updateUrl();
		
        PayoutAPI api = new PayoutAPI();
      //Checking and updating the statement accordingly
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyy-MM-dd");
		String fromDate = LocalDate.now().format(dateFormat);
		String toDate = LocalDate.parse(fromDate, dateFormat).plusDays(1).format(dateFormat);
		
         Map<String, String> stmtMap = api.getStatements("2025-03-04", "2025-03-05", "D", "", "", "", "", "", "");
         
      // Define the directory path and file name
         String directoryPath = "D:\\Phedora\\Banks\\RBL bank\\Reports";
         String filePath = directoryPath + "\\statement.json";
         
      // Write data to JSON file line by line
         writeMapToJsonFile(directoryPath, filePath, stmtMap);
         
         
	
         System.out.println(stmtMap.size());

//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		// Establishing the connection
//		String payout_timeStamp = null;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			connection = DriverManager.getConnection(jdbcUrl, username, password);
//			String query = "SELECT * FROM "+dataBase+".payouts_log where mode = 'IMPS'";
//
//			preparedStatement = connection.prepareStatement(query);
//			resultSet = preparedStatement.executeQuery();
//
//			while (resultSet.next()) {
//				String tranID = resultSet.getString("TranID");
//				String utr = resultSet.getString("utr_rrn");
//				
//				if (stmtMap.containsKey(utr)) {
//					String stmtTimeStamp = JsonParser.parseString(stmtMap.get(utr)).getAsJsonObject()
//							.get("pstdDate").getAsString();
//					InsertDataIntoDB.updateBankStmWithTimeStamp(stmtMap.get(utr), stmtTimeStamp, tranID);
//				}
//			}
//		} catch (SQLException e) {
//			System.out.println("Connection failed. Error: " + e.getMessage());
//		} finally {
//			if (resultSet != null)
//				resultSet.close();
//			if (resultSet != null)
//				preparedStatement.close();
//			if (connection != null)
//				connection.close();
//		}
		
	}
	
	public static void writeMapToJsonFile(String directoryPath, String filePath, Map<String, String> stmtMap) {
        try {
            // Ensure the directory exists
            Files.createDirectories(Paths.get(directoryPath));

            File file = new File(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
         // Truncating the file before writing it
    		Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.TRUNCATE_EXISTING);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Map.Entry<String, String> entry : stmtMap.entrySet()) {
                    // Create an object with the key-value structure
                    Map<String, String> jsonEntry = new LinkedHashMap<>();
                    jsonEntry.put(entry.getKey(), entry.getValue());

                    // Convert to JSON format
                    String jsonLine = objectMapper.writeValueAsString(jsonEntry);

                    // Write to file
                    writer.write(jsonLine);
                    writer.newLine(); // Add a new line after each entry
                }
                System.out.println("JSON file successfully saved to: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }
	
//	public static void main(String[] args) {
//		FetchDataFromDB.exportPayoutLogToExcel();
//	}
}
