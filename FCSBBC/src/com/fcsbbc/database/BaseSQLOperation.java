/**
 * 
 */
package com.fcsbbc.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fcsbbc.cryptology.HashModule;
import com.fcsbbc.utils.JDBCUtil;
import com.fcsbbc.utils.SQLLiteBaseOperation;

/**
 * @author luo.changshu
 *
 */
public class BaseSQLOperation implements BaseSQLInterface {
	private static Logger logger = LoggerFactory.getLogger(BaseSQLOperation.class.getName());
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	private int operationResultCode = 0;
	
	public int getOperationResultCode() {
		return operationResultCode;
	}
	/**
	 * 
	 * @param DBTypeFlag	[0-MySQL;1-SQLLite]
	 */
	public BaseSQLOperation(int DBTypeFlag) {
		if (DBTypeFlag == 0) {
			connection = new JDBCUtil().getJDBCUtil().getConnection();
		}
		else if (DBTypeFlag == 1) {
			connection = new SQLLiteBaseOperation().getSQLBaseOperation().getConnection();
		}
		else {
			logger.info("Please Entry Right DataBase Type![0-MySQL;1-SQLLite]");
		}
	}

	/* (non-Javadoc)
	 * @see com.database.BaseSQLInterface#query(java.lang.String, int, java.lang.Object[])
	 */
	@Override
	public List<Object[]> query(String sql, Object[] objects) {
		// TODO Auto-generated method stub
		List<Object[]> list = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (objects != null && objects.length > 0) {
				for (int i = 0; i < objects.length; i++) {
					preparedStatement.setObject(i + 1, objects[i]);
				}
			}
			resultSet = preparedStatement.executeQuery();
			int columnCount = resultSet.getMetaData().getColumnCount();
			while (resultSet.next()) {
				Object[] array = new Object[columnCount];
				for (int i = 0; i < columnCount; i++) {
					array[i] = resultSet.getObject(i + 1);
				}
				list.add(array);
			}
			if (!list.isEmpty() && list.size() > 0) {
				operationResultCode = 0;
			}
			else {
				operationResultCode = 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			operationResultCode = 11;
			logger.error("Data Query Exception in <BaseSQLOperation:query>", e.getMessage());
			throw new RuntimeException("Exception in <BaseSQLOperation:query>", e);
		}finally {
			new JDBCUtil().releaseResource(preparedStatement, resultSet, connection);
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.database.BaseSQLInterface#insert(java.lang.String, java.lang.Object[])
	 */
	@Override
	public boolean insert(String sql, Object[] objects) {
		// TODO Auto-generated method stub
		boolean insertResult = false;
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (objects != null && objects.length > 0) {
				for (int i = 0; i < objects.length; i++) {
					preparedStatement.setObject(i + 1, objects[i]);
				}
			}
			int res = preparedStatement.executeUpdate();
			if (res != 0) {
				insertResult = true;
				operationResultCode = 0;
			}
			else {
				operationResultCode = 2;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			operationResultCode = 11;
			logger.error("Data Insert Exception in <BaseSQLOperation:insert>", e.getMessage());
			throw new RuntimeException("Exception in <BaseSQLOperation:insert>", e);
		}finally {
			new JDBCUtil().releaseResource(null, preparedStatement, connection);
		}
		return insertResult;
	}

	/* (non-Javadoc)
	 * @see com.database.BaseSQLInterface#update(java.lang.String, java.lang.Object[])
	 */
	@Override
	public boolean update(String sql, Object[] objects) {
		// TODO Auto-generated method stub
		boolean updateResult = false;
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (objects != null && objects.length > 0) {
				for (int i = 0; i < objects.length; i++) {
					preparedStatement.setObject(i + 1, objects[i]);
				}
			}
			int res = preparedStatement.executeUpdate();
			if (res > 0) {
				updateResult = true;
				operationResultCode = 0;
			}
			else {
				operationResultCode = 3;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			operationResultCode = 11;
			logger.error("Data Update Exception in <BaseSQLOperation:update>", e.getMessage());
			throw new RuntimeException("Exception in <BaseSQLOperation:update>", e);
		}finally {
			new JDBCUtil().releaseResource(null, preparedStatement, connection);
		}
		return updateResult;
	}

	/* (non-Javadoc)
	 * @see com.database.BaseSQLInterface#delete(java.lang.String, java.lang.Object[])
	 */
	@Override
	public boolean delete(String sql, Object[] objects) {
		// TODO Auto-generated method stub
		boolean deleteResult = false;
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (objects != null && objects.length > 0) {
				for (int i = 0; i < objects.length; i++) {
					preparedStatement.setObject(i + 1, objects[i]);
				}
			}
			int res = preparedStatement.executeUpdate();
			if (res > 0) {
				deleteResult = true;
				operationResultCode = 0;
			}
			else {
				operationResultCode = 4;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			operationResultCode = 11;
			logger.error("Data Delete Exception in <BaseSQLOperation:delete>", e.getMessage());
			throw new RuntimeException("Exception in <BaseSQLOperation:delete>", e);
		}finally {
			new JDBCUtil().releaseResource(null, preparedStatement, connection);
		}
		return deleteResult;
	}

	/* (non-Javadoc)
	 * @see com.fcsbbc.database.Bas	eSQLInterface#queryExists(java.lang.String, java.lang.Object[])
	 */
	@Override
	public boolean queryExists(String sql, Object[] objects) {
		// TODO Auto-generated method stub
		boolean queryExists = false;
			List<Object[]> list = query(sql, objects);
			operationResultCode = getOperationResultCode();
			if (operationResultCode == 0) {
				if (!list.isEmpty() && list.size() > 0) {
					queryExists = true;
				}
			}
		return queryExists;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.database.BaseSQLInterface#query(java.lang.String, int, java.lang.Object[])
	 */
	@Override
	public List<Object[]> query(String sql, int columnNum, Object[] objects) {
		// TODO Auto-generated method stub
		List<Object[]> list = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (objects != null) {
				for (int i = 0; i < objects.length; i++) {
					preparedStatement.setObject(i + 1, objects[i]);
				}
			}
			resultSet = preparedStatement.executeQuery();
			int columnCount = resultSet.getMetaData().getColumnCount();
			if (columnCount <= columnNum) {
				while (resultSet.next()) {
					Object[] array = new Object[columnCount];
					for (int i = 0; i < columnCount; i++) {
						array[i] = resultSet.getObject(i + 1);
					}
					list.add(array);
				}
				if (!list.isEmpty() && list.size() > 0) {
					operationResultCode = 0;
				}
				else {
					operationResultCode = 1;
				}
			}
			else {
				operationResultCode = 12;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			operationResultCode = 11;
			logger.error("Data Query Exception in <BaseSQLOperation:query>", e.getMessage());
			throw new RuntimeException("Exception in <BaseSQLOperation:query>", e);
		}finally {
			new JDBCUtil().releaseResource(preparedStatement, resultSet, connection);
		}
		return list;
	}
	
		
	/* (non-Javadoc)
	 * @see com.fcsbbc.database.BaseSQLInterface#query(java.lang.String, int, java.lang.Object[], boolean, int)
	 */
	@Override
	public List<Object[]> query(String sql, int columnNum, Object[] objects, boolean cryptologyFlag, int cryptologyNum, boolean hexStringFlag) {
		// TODO Auto-generated method stub
		List<Object[]> list = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			//	need fill
			if (objects != null && objects.length > 0) {
				int objectsLen = objects.length;
				if (cryptologyFlag && cryptologyNum <= objectsLen) {
					for (int i = 0; i < cryptologyNum; i++) {
						String initialString = (String) objects[i];
						String cryptologyString = HashModule.sha256(initialString, hexStringFlag);
						preparedStatement.setString(i + 1, cryptologyString);
					}
					for (int j = cryptologyNum; j < objectsLen; j++) {
						preparedStatement.setObject(j + 1, objects[j]);
					}
				}
				else if (!cryptologyFlag) {
					for (int i = 0; i < objectsLen; i++) {
						preparedStatement.setObject(i + 1, objects[i]);
					}
				}
				else {
					logger.info("Please input right parameters for <BaseSQLOperation:query>");
				}
			}
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Object[] array = new Object[columnNum];
				for (int i = 0; i < columnNum; i++) {
					array[i] = resultSet.getObject(i + 1);
				}
				list.add(array);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Data Query Exception in <BaseSQLOperation:query>", e.getMessage());
			throw new RuntimeException("Data Query Exception in <BaseSQLOperation:query>", e);
		}finally {
			new JDBCUtil().releaseResource(preparedStatement, resultSet, connection);
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.database.BaseSQLInterface#query(java.lang.String, java.lang.Object[], boolean, int, boolean)
	 */
	@Override
	public boolean query(String sql, Object[] objects, int columnNum, boolean cryptologyFlag, int cryptologyNum, boolean hexStringFlag) {
		// TODO Auto-generated method stub
		boolean queryResult = false;
		try {
			List<Object> list = new ArrayList<>();
			preparedStatement = connection.prepareStatement(sql);
			if (objects != null && objects.length > 0) {
				int objectsLen = objects.length;
				if (cryptologyFlag && cryptologyNum <= objectsLen) {
					for (int i = 0; i < cryptologyNum; i++) {
						String initialString = (String) objects[i];
						String cryptologyString = HashModule.sha256(initialString, hexStringFlag);
						preparedStatement.setString(i + 1, cryptologyString);
					}
					for (int j = cryptologyNum; j < objectsLen; j++) {
						preparedStatement.setObject(j + 1, objects[j]);
					}
				}
				else if (!cryptologyFlag) {
					for (int i = 0; i < objectsLen; i++) {
						preparedStatement.setObject(i + 1, objects[i]);
					}
				}
				else {
					logger.info("Please input right parameters for <BaseSQLOperation:query>");
				}
			}
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Object[] array = new Object[columnNum];
				for (int i = 0; i < columnNum; i++) {
					array[i] = resultSet.getObject(i + 1);
				}
				list.add(array);
			}
			if (!list.isEmpty() && list.size() > 0) {
				queryResult = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Data Query Exception in <BaseSQLOperation:query>", e.getMessage());
			throw new RuntimeException("Data Query Exception in <BaseSQLOperation:query>", e);
		}finally {
			new JDBCUtil().releaseResource(preparedStatement, resultSet, connection);
		}
		return queryResult;
	}

	/* (non-Javadoc)
	 * @see com.fcsbbc.database.BaseSQLInterface#update(java.lang.String, java.lang.Object[], boolean, int)
	 */
	@Override
	public boolean update(String sql, Object[] objects, boolean cryptologyFlag, int cryptologyNum, boolean hexStringFlag) {
		// TODO Auto-generated method stub
		boolean updateResult = false;
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (objects != null && objects.length > 0) {
				int objectsLen = objects.length;
				if (cryptologyFlag && cryptologyNum <= objectsLen) {
					for (int i = 0; i < cryptologyNum; i++) {
						String initialString = (String) objects[i];
						String cryptologyString = HashModule.sha256(initialString, hexStringFlag);
						preparedStatement.setString(i + 1, cryptologyString);
					}
					for (int j = cryptologyNum; j < objectsLen; j++) {
						preparedStatement.setObject(j + 1, objects[j]);
					}
				}
				else if (!cryptologyFlag) {
					for (int i = 0; i < objectsLen; i++) {
						preparedStatement.setObject(i + 1, objects[i]);
					}
				}
				else {
					
					logger.info("Please input right parameters for <BaseSQLOperation:update>");
				}
			}
			int res = preparedStatement.executeUpdate();
			if (res > 0) {
				updateResult = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error("Data Update Exception in <BaseSQLOperation:update>", e.getMessage());
			throw new RuntimeException("Data Update Exception in <BaseSQLOperation:update>", e);
		}finally {
			new JDBCUtil().releaseResource(null, preparedStatement, connection);
		}
		return updateResult;
	}

	/* (non-Javadoc)
	 * @see com.fcsbbc.database.BaseSQLInterface#insert(java.lang.String, java.lang.Object[], boolean, int, boolean)
	 */
	@Override
	public boolean insert(String sql, Object[] objects, boolean cryptologyFlag, int cryptologyNum, boolean hexStringFlag) {
		// TODO Auto-generated method stub
		boolean insertResult = false;
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (objects != null && objects.length > 0) {
				int objectsLen = objects.length;
				if (cryptologyFlag && cryptologyNum <= objectsLen) {
					for (int i = 0; i < cryptologyNum; i++) {
						String initialString = (String) objects[i];
						String cryptologyString = HashModule.sha256(initialString, hexStringFlag);
						preparedStatement.setString(i + 1, cryptologyString);
					}
					for (int j = cryptologyNum; j < objectsLen; j++) {
						preparedStatement.setObject(j + 1, objects[j]);
					}
				}
				else if (!cryptologyFlag) {
					for (int i = 0; i < objectsLen; i++) {
						preparedStatement.setObject(i + 1, objects[i]);
					}
				}
				else {
					logger.info("Please input right parameters for <BaseSQLOperation:insert>");
				}
				int res = preparedStatement.executeUpdate();
				if (res != 0) {
					insertResult = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error("Data Insert Exception in <BaseSQLOperation:insert>", e.getMessage());
			throw new RuntimeException("Data Insert Exception in <BaseSQLOperation:insert>", e);
		}finally {
			new JDBCUtil().releaseResource(null, preparedStatement, connection);
		}
		return insertResult;
	}
	
}
