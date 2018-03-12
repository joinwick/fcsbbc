/**
 * 
 */
package com.fcsbbc.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fcsbbc.common.interfaces.UserRegisterInterface;
import com.fcsbbc.database.BaseSQLOperation;

/**
 * @author luo.changshu
 *
 */
public class UserRegisterOperation implements UserRegisterInterface{
	private static Logger logger = LoggerFactory.getLogger(UserRegisterOperation.class.getName());
//	private static final int dataBaseType = 1;	//0-mysql 1-sqllite
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserRegisterInterface#newAccount(java.lang.Object[], int)
	 */
	@Override
	public boolean newAccount(Object[] objects, int processID, int dataBaseTypeFlag) {
		// TODO Auto-generated method stub
		boolean insertResult = false;
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseTypeFlag);
		String sql = "";
		if (objects != null && objects.length > 0) {
			int objectsLen = objects.length;
			//	insert user general info
			if (processID == 0 && objectsLen == 6) {
				sql = "insert into identity_info values (?, ?, ?, ?, ?, ?)";
				insertResult = baseSQLOperation.insert(sql, objects);
			}
			else if (processID == 0 && objectsLen == 7) {
				sql = "insert into user_general_info values (?, ?, ?, ?, ?, ?, ?)";
				insertResult = baseSQLOperation.insert(sql, objects, true, 1, true);
			}
			
		}
		return insertResult;
	}

	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserRegisterInterface#newIdentityInfo(java.lang.Object[], int)
	 */
	@Override
	public boolean newIdentityInfo(Object[] quoteParametersArray, int dataBaseTypeFlag) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserRegisterInterface#checkInfoExists(java.lang.Object[], int)
	 */
	@Override
	public boolean checkInfoExists(Object[] quoteParametersArray, int dataBaseTypeFlag) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserRegisterInterface#queryInfo(java.lang.Object[], int)
	 */
	@Override
	public String[] queryInfo(Object[] quoteParametersArray, int dataBaseTypeFlag) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserRegisterInterface#updateIdentityInfo(java.lang.Object[], int)
	 */
	@Override
	public boolean updateIdentityInfo(Object[] quoteParametersArray, int dataBaseTypeFlag) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
