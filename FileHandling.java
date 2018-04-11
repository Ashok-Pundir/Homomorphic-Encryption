import java.io.*;
import java.sql.*;

public class FileHandling {
	static final String Jdriver = "com.mysql.jdbc.Driver";
	static final String DB = "jdbc:mysql://localhost:3306/java_project";

	static final String user = "root";
	static final String pass = "root";

	public static void main(String[] args) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		// try and catch for sql and db conn
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB, user, pass);
			stmt = conn.createStatement();

			String sql = "SELECT * FROM t_log";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("asd");
			try {
			FileWriter fileWriter = new FileWriter(new File("test1.txt"));
			fileWriter.write("Transaction Details:");
			fileWriter.append(System.lineSeparator());
			fileWriter.append("T_id       T_type      T_ammount	       T_account	     T_time");
			fileWriter.append(System.lineSeparator());
			while (rs.next()) 
			{
					fileWriter.append(""+rs.getInt(1));
					fileWriter.append("       "+rs.getString(2));
					fileWriter.append("       "+rs.getString(3));
					fileWriter.append("       "+rs.getInt(4));
					fileWriter.append("       "+rs.getTimestamp(5));
					fileWriter.append(System.lineSeparator());
			}
			fileWriter.flush();
			fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}

	}
}
