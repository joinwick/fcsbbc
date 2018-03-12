/**
 * 
 */
package com.fcsbbc.common.interfaces;

import java.util.List;

/**
 * @author luo.changshu
 *
 */
public interface UserServiceInterface {
	
	public List<Object[]> query(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public int verifyQuery(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public boolean queryData(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public List<Object[]> queryInfo(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public Object queryBean(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public boolean queryResult(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public int queryVerify(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public boolean updateInfo(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public boolean insertInfo(Object[] objects, int tableCode, int operationCode, int dataBaseType) throws Exception;
	
	public boolean deleteInfo(Object[] objects, int tableCode, boolean deleteConfirm, int dataBaseType) throws Exception;

}
