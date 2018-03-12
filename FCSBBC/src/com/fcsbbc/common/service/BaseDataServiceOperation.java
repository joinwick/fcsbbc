/**
 * 
 */
package com.fcsbbc.common.service;

import java.util.ArrayList;
import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import com.fcsbbc.database.BaseSQLOperation;

/**
 * @author luo.changshu
 *
 */
public class BaseDataServiceOperation {
//	private static Logger logger = LoggerFactory.getLogger(BaseDataServiceOperation.class.getName());
	private static final int dataBaseType = 1;
	
	public List<Object[]> getInstrumentProperty(String instrumentID){
		List<Object[]> instrumentProperties = new ArrayList<>();
		String sql = "select rise_limit_price, fall_limit_price, minimum_price_movement_value, settlement_price "
				+ "from daily_instrument_info where "
				+ "is_active = '1' and "
				+ "instrument_id = ?;";
		Object[] objects = {instrumentID};
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
//		instrumentProperties = baseSQLOperation.query(sql, 4, objects);
		instrumentProperties = baseSQLOperation.query(sql, objects);
		return instrumentProperties;
	}
	
	public List<Object[]> getInstrumentIDList(){
		List<Object[]> instrumentIDList = new ArrayList<>();
		String sql = "select distinct(instrument_id) "
				+ "from daily_instrument_info where "
				+ "is_active = '1';";
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
//		instrumentIDList = baseSQLOperation.query(sql, 1, null);
		instrumentIDList = baseSQLOperation.query(sql, null);
		return instrumentIDList;
	}
	
	public Object[] getInstrumentIDArray() {
		Object[] instrumentIDArray = null;
		List<Object[]> instrumentIDList = new ArrayList<>();
		String sql = "select distinct(instrument_id) "
				+ "from daily_instrument_info where "
				+ "is_active = '1';";
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
//		instrumentIDList = baseSQLOperation.query(sql, 1, null);
		instrumentIDList = baseSQLOperation.query(sql, null);
		if (instrumentIDList !=null && instrumentIDList.size() > 0) {
			instrumentIDArray = new String[instrumentIDList.size()];
			for (int i = 0; i < instrumentIDList.size(); i++) {
				instrumentIDArray[i] = ((Object[])instrumentIDList.get(i))[0];
			}
		}
		return instrumentIDArray;
	}
	
	public Object[] getInstrumentIDArrayByCondition(String condition) {
		Object[] instrumentIDArray = null;
		String querySQL = null;
		List<Object[]> instrumentIDList = new ArrayList<>();
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		StringBuilder querySQLStringBuilder = new StringBuilder("select distinct(instrument_id) "
				+ "from daily_instrument_info where "
				+ "is_active = '1'");
		if (condition != null && !condition.isEmpty()) {
			querySQLStringBuilder.append(" and instrument_id like '" + condition + "%' ");
			querySQL = querySQLStringBuilder.toString();
		}
		else if (condition == null || condition.isEmpty()) {
			querySQL = querySQLStringBuilder.toString();
		}
//		instrumentIDList = baseSQLOperation.query(querySQL, 1, null);
		instrumentIDList = baseSQLOperation.query(querySQL, null);
		if (instrumentIDList !=null && instrumentIDList.size() > 0) {
			instrumentIDArray = new String[instrumentIDList.size()];
			for (int i = 0; i < instrumentIDList.size(); i++) {
				instrumentIDArray[i] = ((Object[])instrumentIDList.get(i))[0];
			}
		}
		return instrumentIDArray;
	}
	
	public static void main(String[] args) {
		BaseDataServiceOperation baseDataServiceOperation = new BaseDataServiceOperation();
		List<Object[]> instrumentProperties = baseDataServiceOperation.getInstrumentProperty("cu1806");
		for (int i = 0; i < instrumentProperties.size(); i++) {
			System.out.println(
					((Object[])instrumentProperties.get(i))[0] +
					" : " + 
					((Object[])instrumentProperties.get(i))[1] + 
					" : " + 
					((Object[])instrumentProperties.get(i))[2]
							);
		}
		List<Object[]> instrumentIDList = baseDataServiceOperation.getInstrumentIDList();
		for (int i = 0; i < instrumentIDList.size(); i++) {
			System.out.println(((Object[])instrumentIDList.get(i))[0]);
		}
		
	}
}
