/**
 * 
 */
package com.fcsbbc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luo.changshu
 *
 */
public class SQLLiteBaseOperation {
	private static Logger logger = LoggerFactory.getLogger(SQLLiteBaseOperation.class.getName());
	public static SQLLiteBaseOperation sqlLiteBaseOperation;
	private final static String driver = ConfigMessage.SQLLITE_JDBC_DRIVER;
	private final static String url = ConfigMessage.SQLLITE_JDBC_URL;
	static {
		//	Load SQLLite JDBC Driver
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Load SQLLite JDBC Driver Failed!", e.getMessage());
		}
	}
	
	/**
	 * Get instance with singleton
	 * @return
	 */
	public SQLLiteBaseOperation getSQLBaseOperation() {
		if (sqlLiteBaseOperation == null) {
			synchronized (JDBCUtil.class) {
				if (sqlLiteBaseOperation == null) {
					sqlLiteBaseOperation = new SQLLiteBaseOperation();
				}
			}
		}
		return sqlLiteBaseOperation;
	}

	/**
	 * Create Database Connection
	 * @return
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			logger.error("Create Database Connection Failed!", e.getMessage());
		}
		return conn;
	}
	
	/**
	 * Release Database Resource
	 * @param object
	 */
	public void releaseResource(Object object) {
		if (object == null) {
			return;
		}
		if (object instanceof ResultSet) {
			try {
				((ResultSet) object).close();
			} catch (SQLException e) {
//				e.printStackTrace();
				logger.error("ResultSet Release Failed!", e.getMessage());
			}
		} else if (object instanceof PreparedStatement) {
			try {
				((PreparedStatement) object).close();
			} catch (SQLException e) {
//				e.printStackTrace();
				logger.error("PreparedStatement Release Failed!", e.getMessage());
			}
		} else if (object instanceof Connection) {
			Connection connection = (Connection) object;
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
//				e.printStackTrace();
				logger.error("Connection Release Failed!", e.getMessage());
			}
		}
	}
	
	/**
	 * Release Database Resource
	 * @param resultSet
	 * @param preparedStatement
	 * @param connection
	 */
	public void releaseResource(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
		try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
			if (preparedStatement != null && !preparedStatement.isClosed()) {
				preparedStatement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Database Resource Release Failed!", e.getMessage());
		}
	}
	
	/**
	 * Release Database Resource
	 * @param preparedStatement
	 * @param connection
	 */
	public void releaseResource(PreparedStatement preparedStatement, Connection connection) {
		try {
			if (preparedStatement != null && !preparedStatement.isClosed()) {
				preparedStatement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Database Resource Release Failed!", e.getMessage());
		}
	}
	
	/**
	 * Release Database Resource
	 * @param preparedStatement
	 * @param resultSet
	 * @param connection
	 */
	public void releaseResource(PreparedStatement preparedStatement, ResultSet resultSet, Connection connection) {
		releaseResource(resultSet);
		releaseResource(preparedStatement);
		releaseResource(connection);
	}
}
