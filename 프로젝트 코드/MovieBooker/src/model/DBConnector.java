package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
	
	private static DBConnector dbConnector = null;
	
	private static final String className = "com.mysql.cj.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/db2";
	
	private Connection conn = null;
	private Statement statement;
	private ResultSet res;
	
	public static DBConnector getInstance() {
		
		if(dbConnector == null) {
			dbConnector = new DBConnector();
		}
		
		return dbConnector;
	}
	
	//DB접속
	public void connectDB(String id, String password) {
		try {
			
			Class.forName(className);
			conn = DriverManager.getConnection(url, id, password);
			statement = conn.createStatement();
			
		} catch (ClassNotFoundException e) {
			
			System.out.println("JDBC 드라이버 로드 오류");
			
		} catch (SQLException e) {
			
			System.out.println("SQL 실행오류");
			
		} 
	}
	
	//DB 접속 해제
	public void disconnectDB() {
		try {
			statement.close();
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//CUD
	public void update(String query) throws SQLException{
		statement.executeUpdate(query);
	}
	
	//R
	public ResultSet select(String query) throws SQLException{
		res = statement.executeQuery(query);
		return res;
	}
	
	// Connection getter
    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            connectDB("root", "1234"); // 적절한 사용자명과 비밀번호로 변경하세요.
        }
        return this.conn;
    }
}
