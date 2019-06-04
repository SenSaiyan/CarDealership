package com.revature.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionUtil {
	private static ConnectionUtil cu = null;
	private static Properties prop;
	
	private ConnectionUtil() {
		prop = new Properties();

		// use the class loader to reduce reliance on the file system
		try (InputStream dbProps = ConnectionUtil.class.getClassLoader()
					.getResourceAsStream("database.properties");){
			prop.load(dbProps);
		} catch (Exception e) {
			LogUtil.logException(e, ConnectionUtil.class);
		}
	}
	public static synchronized ConnectionUtil getConnectionUtil() {
		if(cu == null)
			cu = new ConnectionUtil();
		return cu;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			// We should register our driver class
			Class.forName(prop.getProperty("drv"));
			conn = DriverManager.getConnection(prop.getProperty("bob"),
					prop.getProperty("usr"),
					prop.getProperty("pswd"));
			
		} catch(Exception e) {
			LogUtil.logException(e, ConnectionUtil.class);
		}
		
		return conn;
	}
}
