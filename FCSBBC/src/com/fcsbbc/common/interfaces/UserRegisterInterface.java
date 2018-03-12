/**
 * 
 */
package com.fcsbbc.common.interfaces;

/**
 * @author luo.changshu
 *
 */
public interface UserRegisterInterface {
	//	add new account general info
	public boolean newAccount(Object[] accountParametersArray, int processID, int dataBaseTypeFlag) throws Exception;
	
	//	add new account identity info
	public boolean newIdentityInfo(Object[] accountParametersArray, int dataBaseTypeFlag) throws Exception;
	
	//	query info exists
	public boolean checkInfoExists(Object[] accountParametersArray, int dataBaseTypeFlag) throws Exception;
	
	//	query account info
	public String[] queryInfo(Object[] accountParametersArray, int dataBaseTypeFlag) throws Exception;
	
	//	update account identity info
	public boolean updateIdentityInfo(Object[] accountParametersArray, int dataBaseTypeFlag) throws Exception;
	
	//
}
