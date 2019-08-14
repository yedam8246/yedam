package common;

import java.io.FileReader;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DAO {
	
	//DB 접속용
	
		public static Connection getConnect() {
			

			
			
			
			
			
			String user="demo";
			String pw="demo";
			String url="jdbc:oracle:thin:@192.168.0.21:1521:xe";
			Connection conn=null;
			try {
				Class.forName("oracle.jdbc.OracleDriver");
				conn = DriverManager.getConnection(url, user, pw);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			return conn;
		}
		
		public static void close(Connection conn) {
			
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	
	

}
