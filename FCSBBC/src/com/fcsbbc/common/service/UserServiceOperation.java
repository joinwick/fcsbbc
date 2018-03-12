/**
 * 
 */
package com.fcsbbc.common.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.common.domain.UserDetailBean;
import com.fcsbbc.common.interfaces.UserServiceInterface;
import com.fcsbbc.cryptology.HashModule;
import com.fcsbbc.database.BaseSQLOperation;

/**
 * @author luo.changshu
 *
 */
public class UserServiceOperation implements UserServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(UserServiceOperation.class.getName());
	private int operationResultCode = 0;
//	private static final int dataBaseType = 1;	//0-mysql 1-sqllite
	
	public int getOperationCode() {
		return operationResultCode;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#query(java.lang.Object[], int, int, int)
	 */
	@Override
	public List<Object[]> query(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		List<Object[]> list = new ArrayList<>();
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		String sql = "";
		//	query user general info
		if (tableCode == 0) {
			//query all columns
			if (operationCode == 0) {
				sql = "select user_id, user_name, user_role, user_ca_path, is_active, user_system_id, generate_time from user_general_info where user_password = ? and user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user ca path info[ca/XXXXX.cer]
			else if (operationCode == 1) {
				sql = "select user_ca_path from user_general_info where user_password = ? and user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user role info[1:customer,2:member,3:regulator]
			else if (operationCode == 2) {
				sql = "select user_role from user_general_info where user_password = ? and user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user status[0:invalid,1:valid]
			else if (operationCode == 3) {
				sql = "select is_active from user_general_info where user_password = ? and user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user_id, user_telephone, user_role, user_ca_path, is_active, user_system_id
			else if (operationCode == 4) {
				sql = "select user_id, user_telephone, user_role, user_ca_path, is_active, user_system_id from user_general_info where user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			else if (operationCode == 5) {
				sql = "select user_id, user_name, user_password, user_telephone, user_role, user_ca_path, is_active, user_system_id, generate_time from user_general_info where user_id = ? or user_telephone = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			else {
				//catch exception-parameters error!
				operationResultCode = 10;
				logger.info("Please input right value[0, 1, 2, 3, 4, 5] for parameter[operationCode] in function <UserServiceOperation:query>");
			}
		}
		//	query user details info
		else if (tableCode == 1) {
			//query user detail info for customer
			if (operationCode == 0) {
				sql = "select user_system_id, user_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active from user_detail_info where user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user detail info for member
			else if (operationCode == 1) {
				sql = "select user_system_id, user_telephone, corporation, manager, manager_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active from user_detail_info where user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user default user_system_id
			else if (operationCode == 2) {
				sql = "select user_system_id from user_detail_info where user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user status:is_active
			else if (operationCode == 3) {
				sql = "select is_active from user_detail_info where user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			else {
				operationResultCode = 10;
				logger.info("Please input right value[0, 1, 2, 3] for parameter[operationCode] in function <UserServiceOperation:query>");
			}
		}
		//	query user audit info
		else if (tableCode == 2) {
			//query user audit info
			if (operationCode == 0) {
				sql = "select audit_id, user_id, audit_number, user_role, audit_time from audit_info where user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user_id, audit_number, user_role
			else if (operationCode == 1) {
				sql = "select user_id, audit_number, user_role from audit_info where user_id = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			else {
				operationResultCode = 10;
				logger.info("Please input right value[0, 1] for parameter[operationCode] in function <UserServiceOperation:query>");
			}
		}
		//	query user identity info
		else if (tableCode == 3) {
			//query user identity info
			if (operationCode == 0) {
				sql = "select identity_id, user_id, user_telephone, identity_code, generate_time, identity_status from identity_info where user_id = ? and identity_function = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query user_id[check telephone is available]
			else if (operationCode == 1) {
				sql = "select user_id, identity_status from identity_info where user_telephone = ? and identity_function = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query identity_status[check telephone is active]
			else if (operationCode == 2) {
				sql = "select identity_status from identity_info where user_telephone = ? and identity_function = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			//query identity_code, generate_time[check identity code is valid]
			else if (operationCode == 3) {
				sql = "select identity_code, generate_time from identity_info where user_telephone = ? and identity_function = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			else if (operationCode == 4) {
				sql = "select identity_code, generate_time, identity_status from identity_info where user_id = ? and user_telephone = ? and identity_function = ?";
				list = baseSQLOperation.query(sql, objects);
				operationResultCode = baseSQLOperation.getOperationResultCode();
			}
			else {
				operationResultCode = 10;
				logger.info("Please input right value[0, 1, 2, 3, 4] for parameter[operationCode] in function <UserServiceOperation:query>");
			}
		}
		else {
			operationResultCode = 10;
			logger.info("Please input right value[0, 1, 2, 3, 4] for parameter[tableCode] in function <UserServiceOperation:query>");
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#insertInfo(java.lang.Object[])
	 */
	@Override
	public boolean insertInfo(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		boolean insertResult = false;
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		String sql = "";
		if (objects != null && objects.length > 0) {
			int objectsLen = objects.length;
			//	insert user general info
			if (tableCode == 0) {
				if (operationCode == 0) {
					sql = "insert into user_general_info(user_id, user_name, user_password, user_telephone, user_role, user_ca_path, is_active, user_system_id, generate_time) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					insertResult = baseSQLOperation.insert(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else if (operationCode == 1) {
					sql = "insert into user_general_info(user_id, user_password, user_telephone, user_role, user_ca_path, is_active, user_system_id, generate_time) values (?, ?, ?, ?, ?, ?, ?, ?)";
					insertResult = baseSQLOperation.insert(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0] for parameter[operationCode] in function <UserServiceOperation:insertInfo>");
				}
			}
			//	insert user details info[user_system_id default set with 'null']
			else if (tableCode == 1) {
				//insert user_role = 1: user_system_id, user_id, user_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active
				if (operationCode == 0 && objectsLen == 9) {
					sql = "insert into user_detail_info(user_system_id, user_id, user_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					insertResult = baseSQLOperation.insert(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				//insert user_role = 2: user_system_id, user_id, user_telephone, corporation, manager, manager_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active
				else if (operationCode == 1 && objectsLen == 12) {
					sql = "insert into user_detail_info values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					insertResult = baseSQLOperation.insert(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1] for parameter[operationCode] in function <UserServiceOperation:insertInfo>");
				}
			}
			//	insert user audit info[audit_id default set with 'null']
			else if (tableCode == 2) {
				//insert audit info: audit_id, user_id, audit_number, user_role, audit_time[audit_id generate automatic]
				if (operationCode == 0 && objectsLen == 5) {
					sql = "insert into audit_info(audit_id, user_id, audit_number, user_role, audit_time) values (?, ?, ?, ?, ?)";
					insertResult = baseSQLOperation.insert(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1] for parameter[operationCode] in function <UserServiceOperation:insertInfo>");
				}
			}
			//	insert user identity info[identity_id default set with 'null']
			else if (tableCode == 3) {
				//insert identity info: identity_id, user_id, user_telephone, identity_code, generate_time, identity_status, identity_function[identity_id generate automatic]
				if (operationCode == 0) {
					sql = "insert into identity_info(identity_id, user_id, user_telephone, identity_code, generate_time, identity_status, identity_function) values (?, ?, ?, ?, ?, ?, ?)";
					insertResult = baseSQLOperation.insert(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1] for parameter[operationCode] in function <UserServiceOperation:insertInfo>");
				}
			}
			else {
				operationResultCode = 10;
				logger.info("Please input right value[0, 1, 2, 3] for parameter[tableCode] in function <UserServiceOperation:insertInfo>");
			}
		}
		return insertResult;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#updateInfo(java.lang.Object[])
	 */
	@Override
	public boolean updateInfo(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		boolean updateResult = false;
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		String sql = "";
		if (objects != null && objects.length > 0) {
			int objectsLen = objects.length;
			//	update user general info
			if (tableCode == 0) {
				// update user_name
				if (operationCode == 0 && objectsLen == 2) {
					sql = "update user_general_info set user_name = ? where user_id = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				//	update user_password
				else if (operationCode == 1 && objectsLen == 2) {
					sql = "update user_general_info set user_password = ? where user_id = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else if (operationCode == 2 && objectsLen == 3) {
					sql = "update user_general_info set user_password = ? , generate_time = ? where user_id = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1, 2] for parameter[operationCode] in function <UserServiceOperation:updateInfo>");
				}
			}
			//	update user details info
			else if (tableCode == 1) {
				//update user_role = 1: user_telephone, user_province, user_city, user_county, user_detail_address, user_fax
				if (operationCode == 0 && objectsLen == 7) {
					sql = "update user_detail_info set user_telephone = ?, user_province = ?, user_city = ?, user_county = ?, user_detail_address = ?, user_fax = ? where user_id = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				//update user_role = 2: corporation, user_telephone, manager, manager_telephone, user_province, user_city, user_county, user_detail_address, user_fax
				else if (operationCode == 1 && objectsLen == 10) {
					sql = "update user_detail_info set corporation = ?, user_telephone = ?, manager = ?, manager_telephone = ?, user_province = ?, user_city = ?, user_county = ?, user_detail_address = ?, user_fax = ? where user_id = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1] for parameter[operationCode] in function <UserServiceOperation:updateInfo>");
				}
			}
			//	update user audit info
			else if (tableCode == 2) {
				//update audit_number, user_role, audit_time
				if (operationCode == 0 && objectsLen == 4) {
					sql = "update audit_info set audit_number = ?, user_role = ?, audit_time = ? where user_id = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				//update audit_number, audit_time
				else if (operationCode == 1 && objectsLen == 3) {
					sql = "update audit_info set audit_number = ?, audit_time = ? where user_id = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				//update user_role, audit_time
				else if (operationCode == 2 && objectsLen == 3) {
					sql = "update audit_info set user_role = ?, audit_time = ? where user_id = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1, 2] for parameter[operationCode] in function <UserServiceOperation:updateInfo>");
				}
			}
			//	update identity info
			else if (tableCode == 3) {
				//update user_telephone, identity_code, generate_time, identity_status
				if (operationCode == 0) {
					sql = "update identity_info set user_telephone = ?, identity_code = ?, generate_time = ?, identity_status = ? where user_id = ? and identity_function = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				//update identity_status
				else if (operationCode == 1) {
					sql = "update identity_info set identity_status = ? where user_id = ? and user_telephone = ? and identity_function = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				//update identity_code, generate_time, identity_status
				else if (operationCode == 2) {
					sql = "update identity_info set identity_code = ?, generate_time = ?, identity_status = ? where user_id = ? and identity_function = ?";
					updateResult = baseSQLOperation.update(sql, objects);
					operationResultCode = baseSQLOperation.getOperationResultCode();
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1, 2] for parameter[operationCode] in function <UserServiceOperation:updateInfo>");
				}
			}
			else {
				operationResultCode = 10;
				logger.info("Please input right value[0, 1, 2, 3] for parameter[tableCode] in function <UserServiceOperation:updateInfo>");
			}
		}
		return updateResult;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#deleteInfo(java.lang.Object[])
	 */
	@Override
	public boolean deleteInfo(Object[] objects, int tableCode, boolean deleteConfirm, int dataBaseType) {
		// TODO Auto-generated method stub
		boolean deleteResult = false;
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		String sql = "";
		//	not delete directly, just modify the status of info[0:invalid, 1:valid]
		if (objects != null && objects.length > 0) {
			int objectsLen = objects.length;
			//	delete from table forever
			if (deleteConfirm) {
				//	delete from user general info
				if (tableCode == 0 && objectsLen == 1) {
					sql = "delete from user_general_info where user_id = ?";
					deleteResult = baseSQLOperation.delete(sql, objects);
				}
				//	delete from user details info
				else if (tableCode == 1 && objectsLen == 1) {
					sql = "delete from user_detail_info where user_id = ?";
					deleteResult = baseSQLOperation.delete(sql, objects);
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1] for parameter[tableCode] in function <UserServiceOperation:deleteInfo>");
				}
			}
			else {
				//	modify user general info --> invalid
				if (tableCode == 0 && objectsLen == 2) {
					sql = "update user_general_info set is_active = ? where user_id = ?";
					deleteResult = baseSQLOperation.update(sql, objects);
				}
				//	modify user details info --> invalid
				else if (tableCode == 1 && objectsLen == 2) {
					sql = "update user_detail_info set is_active = ? where user_id = ?";
					deleteResult = baseSQLOperation.update(sql, objects);
				}
				else {
					operationResultCode = 10;
					logger.info("Please input right value[0, 1] for parameter[tableCode] in function <UserServiceOperation:deleteInfo>");
				}
			}
		}
		return deleteResult;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#queryData(java.lang.Object[], int, int, int)
	 */
	@Override
	public boolean queryData(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		boolean queryDataExists = false;
		List<Object[]> list = query(objects, tableCode, operationCode, dataBaseType);
		operationResultCode = getOperationCode();
		if (operationResultCode == 0) {
			if (list != null && list.size() > 0) {
				queryDataExists = true;
			}
		}
		return queryDataExists;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#verifyQuery(java.lang.Object[], int, int, int)
	 */
	@Override
	public int verifyQuery(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		int queryResult = 0;	//default 0 :success
//		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
//		String sql = "";
//		//	query user general info
//		if (tableCode == 0) {
//			boolean userExists = false;
//		//check user id exists
//			if (operationCode == 1) {
//				Object[] user_id = {objects[1]};
//				
//			}
//		}
		return queryResult;
	}
	
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#queryInfo(java.lang.Object[])
	 */
	@Override
	public List<Object[]> queryInfo(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		//	UserBaseBean userBaseBean = new UserBaseBean();
		List<Object[]> list = new ArrayList<>();
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		String sql = "";
		if (objects != null && objects.length > 0) {
			int objectsLen = objects.length;
			//	query user general info
			if (tableCode == 0) {
				//	query all user general info
				if (operationCode == 1 && objectsLen == 2) {
					sql = "select user_name, user_role, user_ca_path, is_active, user_system_id from user_general_info where user_password = ? and user_id = ?";
					list = baseSQLOperation.query(sql, 5, objects, true, 1, true);
				}
				//	query user ca path info[ca/XXXXX.cer]
				else if (operationCode == 2 && objectsLen == 2) {
					sql = "select user_ca_path from user_general_info where user_password = ? and user_id = ?";
					list = baseSQLOperation.query(sql, 1, objects, true, 1, true);
				}
				//	query user role info[1:customer,2:member,3:regulator]
				else if (operationCode == 3 && objectsLen == 2) {
					sql = "select user_role from user_general_info where user_password = ? and user_id = ?";
					list = baseSQLOperation.query(sql, 1, objects, true, 1, true);
				}
				//	query user status[0:invalid,1:valid]
				else if (operationCode == 4 && objectsLen == 2) {
					sql = "select is_active from user_general_info where user_password = ? and user_id = ?";
					list = baseSQLOperation.query(sql, 1, objects, true, 1, true);
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryInfo>");
				}
			}
			//	query user details info
			else if (tableCode == 1) {
				//	query all user detail info for customer
				if (operationCode == 1 && objectsLen == 1) {
					sql = "select user_system_id, user_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active from user_detail_info where user_id = ?";
					list = baseSQLOperation.query(sql, 8, objects, false, 0, false);
				}
				//	query all user detail info for member
				else if (operationCode == 2 && objectsLen == 1) {
					sql = "select user_system_id, user_telephone, corporation, manager, manager_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active from user_detail_info where user_id = ?";
					list = baseSQLOperation.query(sql, 11, objects, false, 0, false);
				}
				//	query user default user system id info
				else if (operationCode == 3 && objectsLen == 1) {
					sql = "select user_system_id from user_detail_info where user_id = ?";
					list = baseSQLOperation.query(sql, 1, objects, false, 0, false);
				}
				//	query user status
				else if (operationCode == 4 && objectsLen == 1) {
					sql = "select is_active from user_detail_info where user_id = ?";
					list = baseSQLOperation.query(sql, 1, objects, false, 0, false);
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryInfo>");
				}
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#queryBean(java.lang.Object[], int, int)
	 */
	@Override
	public Object queryBean(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		Object object = new Object();
		if (objects != null && objects.length > 0) {
			int objectsLen = objects.length;
			List<Object[]> list = new ArrayList<>();
			//	query user general info
			if (tableCode == 0) {
				if (objectsLen == 2) {
					//	get user_name, user_role, user_ca_path, is_active, user_system_id
					list = queryInfo(objects, 0, 1, dataBaseType);
					if (!list.isEmpty() && list.size() > 0) {
						UserBaseBean userBaseBean = new UserBaseBean();
						userBaseBean.setUser_name((String)((Object[])list.get(0))[0]);
						userBaseBean.setUser_role((int) ((Object[]) list.get(0))[1]);
						userBaseBean.setUser_ca_path((String) ((Object[]) list.get(0))[2]);
						userBaseBean.setIs_active((int) ((Object[]) list.get(0))[3]);
						userBaseBean.setUser_system_id((String)((Object[])list.get(0))[4]);
						object = userBaseBean;
					}
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryBean>");
				}
			}
			//	query user detail info
			else if (tableCode == 1) {
				UserDetailBean userDetailBean = new UserDetailBean();
				//	query all user detail info for customer
				if (operationCode == 1 && objectsLen == 1) {
					//	get user_system_id, user_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active
					list = queryInfo(objects, 1, 1, dataBaseType);
					if (!list.isEmpty() && list.size() > 0) {
						userDetailBean.setUser_system_id((String)((Object[])list.get(0))[0]);
						userDetailBean.setUser_telephone((String)((Object[])list.get(0))[1]);
						userDetailBean.setUser_province((String)((Object[])list.get(0))[2]);
						userDetailBean.setUser_city((String)((Object[])list.get(0))[3]);
						userDetailBean.setUser_county((String)((Object[])list.get(0))[4]);
						userDetailBean.setUser_detail_address((String)((Object[])list.get(0))[5]);
						userDetailBean.setUser_fax((String)((Object[])list.get(0))[6]);
						userDetailBean.setIs_active((int)((Object[])list.get(0))[7]);
						object = userDetailBean;
					}
				}
				//	query all user detail info for member
				else if (operationCode == 2 && objectsLen == 1) {
					//	get user_system_id, user_telephone, corporation, manager, manager_telephone, user_province, user_city, user_county, user_detail_address, user_fax, is_active
					list = queryInfo(objects, 1, 2, dataBaseType);
					userDetailBean.setUser_system_id((String)((Object[])list.get(0))[0]);
					userDetailBean.setUser_telephone((String)((Object[])list.get(0))[1]);
					userDetailBean.setCorporator((String)((Object[])list.get(0))[2]);
					userDetailBean.setManager((String)((Object[])list.get(0))[3]);
					userDetailBean.setManager_telephone((String)((Object[])list.get(0))[4]);
					userDetailBean.setUser_province((String)((Object[])list.get(0))[5]);
					userDetailBean.setUser_city((String)((Object[])list.get(0))[6]);
					userDetailBean.setUser_county((String)((Object[])list.get(0))[7]);
					userDetailBean.setUser_detail_address((String)((Object[])list.get(0))[8]);
					userDetailBean.setUser_fax((String)((Object[])list.get(0))[9]);
					userDetailBean.setIs_active((int)((Object[])list.get(0))[10]);
					object = userDetailBean;
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryBean>");
				}
			}
		}
		return object;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#queryResult(java.lang.Object[], int, int)
	 */
	@Override
	public boolean queryResult(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		boolean queryResult = false;
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		String sql = "";
		if (objects != null && objects.length > 0) {
			int objectsLen = objects.length;
			//	query user general info
			if (tableCode == 0) {
				if (operationCode == 1 && objectsLen == 1) {
					sql = "select user_id from user_general_info where user_id = ?";
					queryResult = baseSQLOperation.query(sql, objects, 1, false, 0, false);
				}
				else if (operationCode == 2 && objectsLen == 2) {
					sql = "select user_id from user_general_info where user_password = ? and user_id = ?";
					queryResult = baseSQLOperation.query(sql, objects, 1, true, 1, true);
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryResult>");
				}
			}
			//	query user audit info
			else if (tableCode == 1) {
				if (operationCode == 1 && objectsLen == 1) {
					sql = "select audit_id from audit_info where user_id = ?";
					queryResult = baseSQLOperation.query(sql, objects, 1, false, 0, false);
				}
				else if (operationCode == 2 && objectsLen == 2) {
					sql = "select audit_id from audit_info where audit_number = ? and user_id = ?";
					queryResult = baseSQLOperation.query(sql, objects, 1, true, 1, true);
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryResult>");
				}
			}
			//	query user details info
			else if (tableCode == 2) {
				if (operationCode == 1 && objectsLen == 1) {
					sql = "select user_system_id from user_detail_info where user_id = ?";
					queryResult = baseSQLOperation.query(sql, objects, 1, false, 0, false);
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryResult>");
				}
			}
			else {
				logger.info("Please input right parameters for <UserServiceOperation:queryResult>");
			}
		}
		return queryResult;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.UserServiceInterface#queryVerify(java.lang.Object[], int, int)
	 * @1:pass;2:user not active;4:password wrong;5:user not exist
	 */
	@Override
	public int queryVerify(Object[] objects, int tableCode, int operationCode, int dataBaseType) {
		// TODO Auto-generated method stub
		int queryResult = 0;
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		String sql = "";
		if (objects != null && objects.length > 0) {
			int objectsLen = objects.length;
			boolean userExists = false;
			//	query user general info
			if (tableCode == 0) {
				//	check user id exists
				if (operationCode == 1 && objectsLen == 2) {
					Object[] user_id = {objects[1]};
					userExists = queryResult(user_id, 0, 1, dataBaseType);
					//	user id exists
					if (userExists) {
//						baseSQLOperation = new BaseSQLOperation();
						sql = "select is_active from user_general_info where user_password = ? and user_id = ?";
						List<Object[]> userStatus = baseSQLOperation.query(sql, 1, objects, true, 1, true);
						//	user password right
						if (!userStatus.isEmpty() && userStatus.size() > 0) {
							int userActive = (int) ((Object[]) userStatus.get(0))[0];
							//	user status is active
							if (userActive == 1) {
								queryResult = 1;
							}
							//	user status is inactive
							else if (userActive == 0) {
								queryResult = 2;
							}
							//	get wrong result
							else {
								logger.info("User Status in <UserServiceOperation:queryVerify> is:", userActive);
							}
						}
						//	user password wrong
						else {
							queryResult = 4;
							logger.info("User Password is wrong in <UserServiceOperation:queryVerify>");
						}
					}
					//	user id not exist
					else {
						queryResult = 5;
						logger.info("User not exist in <UserServiceOperation:queryVerify>");
					}
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryVerify>");
				}
			}
			//	query user audit info
			else if (tableCode == 1) {
				//	check user_id exists
				if (operationCode == 1 && objectsLen == 2) {
					Object[] user_id = {objects[1]};
					sql = "select user_id from audit_info where user_id = ?";
					userExists = baseSQLOperation.query(sql, user_id, 1, false, 0, false);
					//	user id exists
					if (userExists) {
						baseSQLOperation = new BaseSQLOperation(dataBaseType);
						sql = "select audit_id from audit_info where audit_number = ? and user_id = ?";
						List<Object[]> userStatus = baseSQLOperation.query(sql, 1, objects, true, 1, true);
						//	user password right
						if (!userStatus.isEmpty() && userStatus.size() > 0) {
							Long auditID = (Long) ((Object[])userStatus.get(0))[0];
							//	user audit info is right
							if (auditID > 0L) {
								queryResult = 1;
							}
							//	get wrong result
							else {
								logger.info("User Status in <UserServiceOperation:queryVerify> is:", auditID);
							}
						}
						//	user audit info is wrong
						else {
							queryResult = 4;
							logger.info("User Audit result is wrong in <UserServiceOperation:queryVerify>");
						}
					}
					//	user id not exist
					else {
						queryResult = 5;
						logger.info("User not exist in <UserServiceOperation:queryVerify>");
					}
				}
				else {
					logger.info("Please input right parameters for <UserServiceOperation:queryVerify>");
				}
			}
		}
		return queryResult;
	}
	
	public static void main(String[] args) {
//		UserBaseBean userBaseBean = new UserBaseBean();
		UserServiceOperation userServiceOperation = new UserServiceOperation();
		String password = "123456";
//		String initialString = (String) objects[i];
		String cryptologyString = HashModule.sha256(password, true);
		Object[] objects = {cryptologyString, "43092219900908799X"};
//		Object[] objects = {"15021241266"};
//		System.out.println(queryObjects.length);
		ArrayList<Object[]> list = (ArrayList<Object[]>) userServiceOperation.query(objects, 0, 0, 1);
		System.out.println("operationResultCode = " + userServiceOperation.getOperationCode());
//		ArrayList<Object[]> list = (ArrayList<Object[]>) userServiceOperation.queryInfo(queryObjects, 0, 1, 1);
//		select user_id, user_name, user_role, user_ca_path, is_active, user_system_id
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.get(i).length; j++) {
					System.out.println(((Object[]) list.get(i))[j]);
				}
			}
//			userBaseBean.setUser_id((String) ((Object[]) list.get(0))[0]);
//			userBaseBean.setUser_name((String) ((Object[]) list.get(0))[1]);
//			userBaseBean.setUser_role((int) ((Object[]) list.get(0))[2]);
//			userBaseBean.setUser_ca_path((String) ((Object[]) list.get(0))[3]);
//			userBaseBean.setIs_active((int) ((Object[]) list.get(0))[4]);
//			userBaseBean.setUser_system_id((String) ((Object[]) list.get(0))[5]);
		} 
//		System.out.println(userBaseBean.getUser_ca_path());
		
//		Object[] queryObjects01 = {"123456", "43092219900908799X"};
//		System.out.println("queryResult:" + userServiceOperation.queryResult(queryObjects01, 0, 2, 1));
//		
//		Object[] queryObjects02 = {"123456", "43092219900908799X"};
//		int queryResult = userServiceOperation.queryVerify(queryObjects02, 0, 1, 1);
//		System.out.println("queryResult = " + queryResult);
//		if (queryResult == 1) {
//			System.out.println("Verify Pass!");
//		}
//		else if (queryResult == 2) {
//			System.out.println("User is inactive!");
//		}
//		else if (queryResult == 3) {
//			System.out.println("Verify Pass!");
//		}
//		else if (queryResult == 4) {
//			System.out.println("User password / audit number is wrong!");
//		}
//		else if (queryResult == 5) {
//			System.out.println("User Not Exist!");
//		}
//		
//		Object[] updateObjects = {"123456", "43092219900908799X"};
//		System.out.println(userServiceOperation.updateInfo(updateObjects, 0, 2));
//		
//		Object[] updateObjects01 = {"逸海001", "43092219900908799X"};
//		System.out.println(userServiceOperation.updateInfo(updateObjects01, 0, 1));
//		
//		Object[] updateObjects02 = {"13164954337", "上海市", "上海市", "浦东新区", "乐昌路399号", "021-58137786", "43092219900908799X"};
//		System.out.println(userServiceOperation.updateInfo(updateObjects02, 1, 1));
//		
//		Object[] insertObjects01 = {"123456", "411528198902160055", "林拜", 1, "ca/411528198902160055.cer", 1};
//		System.out.println(userServiceOperation.insertInfo(insertObjects01, 0, 1));
		
//		Object[] insertObjects02 = {6, "411528198902160055", "13164954339", "上海市", "上海市", "浦东新区", "来安路996路", "021-58987764", 1};
//		System.out.println(userServiceOperation.insertInfo(insertObjects02, 1, 1));
		
//		Object[] insertObjects03 = {6, "411528198902160055", "13164954339", "郑秋冬", "罗伊人", "13164964340", "上海市", "上海市", "浦东新区", "来安路996路", "021-58987764", 1};
//		System.out.println(userServiceOperation.insertInfo(insertObjects03, 1, 2));
		
//		Object[] deleteObjects01 = {"411528198902160055"};
//		System.out.println(userServiceOperation.deleteInfo(deleteObjects01, 0, true));
//		
//		Object[] deleteObjects02 = {0, "411528198902160055"};
//		System.out.println(userServiceOperation.deleteInfo(deleteObjects02, 1, false));
		
	}
}
