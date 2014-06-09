package com.liu.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class JDBCHelper {
	private static final Logger logger = Logger.getLogger(JDBCHelper.class);
	private static Connection conn;
	private static String url;
	private static String username;
	private static String password;
	
    private static void loadDriver(String driverName) {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static boolean init(String driver, String dbUrl, String dbUsername, String dbPassword) {
    	url = dbUrl;
    	username = dbUsername;
    	password = dbPassword;
    	loadDriver(driver);
    	getConnection();
    	try {
			return !conn.isClosed();
		} catch (SQLException e) {
			logger.error("connection failed.", e);
			return false;
		}
    }
    
	public static Connection getConnection() {
		try {
			if (conn == null || conn.isClosed())
				conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			logger.error("Failed to connect to db server.", e);
		}
		return conn;
	}

    public static void close(ResultSet res, Statement stmt) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                logger.error("Failed to close ResultSet", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("Failed to close Statement", e);
            }
        }
    }
    
    public static void closeConnection() {
    	if(conn != null) {
    		try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Failed to close connection to db server.", e);
			}
    	}
    }
}
