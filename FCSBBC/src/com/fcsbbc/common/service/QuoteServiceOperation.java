/**
 * 
 */
package com.fcsbbc.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fcsbbc.common.interfaces.QuoteServiceInterface;
import com.fcsbbc.database.BaseSQLOperation;
import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.LocalizeConfig;
import com.fcsbbc.utils.UtilTool;

/**
 * @author luo.changshu
 *
 */
public class QuoteServiceOperation implements QuoteServiceInterface {
	private static Logger logger = LoggerFactory.getLogger(QuoteServiceOperation.class.getName());
	private int operationResultCode = 0;
	
	public int getOperationCode() {
		return operationResultCode;
	}
	
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.QuoteServiceInterface#newQuote(java.lang.Object[])
	 */
	@Override
	public boolean newQuote(Object[] objects, int dataBaseTypeFlag) {
		// TODO Auto-generated method stub
		boolean insertResult = false;
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseTypeFlag);
		String sql = "insert into order_info(order_id, order_trade_side, order_open_side, order_price, order_number, "
				+ "instrument_id, order_status, order_time, user_system_id, order_fee) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		insertResult = baseSQLOperation.insert(sql, objects);
		operationResultCode = baseSQLOperation.getOperationResultCode();
		return insertResult;
	}
	
	public boolean updateOrderInfo(Object[] objects, int dataBaseTypeFlag) {
		boolean updateResult = false;
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseTypeFlag);
		String sql = "update order_info set order_status = ? where order_id = ?";
		updateResult = baseSQLOperation.update(sql, objects);
		operationResultCode = baseSQLOperation.getOperationResultCode();
		return updateResult;
	}
	
	public List<Object[]> query(Object[] objects, int dataBaseType){
		List<Object[]> list = new ArrayList<>();
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseType);
		String sql = "select order_status from order_info where order_id = ?";
		list = baseSQLOperation.query(sql, objects);
		operationResultCode = baseSQLOperation.getOperationResultCode();
		return list;
	}
	/* (non-Javadoc)
	 * @see com.fcsbbc.common.interfaces.QuoteServiceInterface#queryQuote(java.lang.Object[])
	 */
	@Override
	public Vector<Vector<Object>> queryQuote(Object[] objects, int dataBaseTypeFlag) {
		// TODO Auto-generated method stub
		Vector<Vector<Object>> queryTesult = new Vector<>();
		BaseSQLOperation baseSQLOperation = new BaseSQLOperation(dataBaseTypeFlag);
		StringBuilder querySQLStringBuilder = new StringBuilder("select t.order_id, t.instrument_id, t.order_trade_side, t.order_price, "
				+ "t.order_number, t.order_time, t.order_status, t.user_system_id "
				+ "from order_info t "
				+ "where 1 = 1 and t.order_status in (0, 2, 4)");
		String searchCondition = objects[0].toString().trim();
		//	search context
		if (!searchCondition.isEmpty()) {
			querySQLStringBuilder.append(" and t.instrument_id like '%" + objects[0] + "%' ");
		}
		//	trade side
		if (!LocalizeConfig.QUERY_CONDITION_ALL.equals(objects[1])) {
			if (Integer.parseInt(objects[1].toString()) == 0) {
				querySQLStringBuilder.append(" and t.order_trade_side = 0 ");
			}
			else if (Integer.parseInt(objects[1].toString()) == 1) {
				querySQLStringBuilder.append(" and t.order_trade_side = 1 ");
			}
			else {
				logger.error("Please Entry Right Parameters, Exception in <queryQuote:query by tradeSide>");
			}
		}
		
		String querySQL = querySQLStringBuilder.toString();
		List<Object[]> list = baseSQLOperation.query(querySQL, null);
//		List<Object[]> list = baseSQLOperation.query(querySQL, 8, null, false, 0, false);
		operationResultCode = baseSQLOperation.getOperationResultCode();
		if (!list.isEmpty()) {
			for (Object[] object : list) {
				Vector<Object> tempResult = new Vector<>();
				for (int i = 0; i < object.length; i++) {
					//	process order trade-side
					if(i == 2) {
						if (Integer.parseInt((object[i]).toString()) == 0) {
							object[i] = ConfigMessage.ORDER_TRADESIDE_BUY;
						}
						else if (Integer.parseInt((object[i]).toString()) == 1) {
							object[i] = ConfigMessage.ORDER_TRADESIDE_SELL;
						}
						else {
							object[i] = "NA";
						}
					}
					//Convert UTC-time to Local-time
					if (i == 5) {
						String utcTimeStamp = (object[i]).toString();
						String utcTime = UtilTool.timestampToDateTime(String.valueOf(utcTimeStamp), 1);
						object[i] = UtilTool.SourceTimeToDestinationTime(utcTime, ConfigMessage.UTC_TIME_ZONE, ConfigMessage.LOCAL_TIME_ZONE, 1, 0);
					}
					//	process order status
					if (i == 6) {
						if (Integer.parseInt((object[i]).toString()) == 0) {
							object[i] = ConfigMessage.ORDER_STATUS_NEW;
						}
						else if (Integer.parseInt((object[i]).toString()) == 1) {
							object[i] = ConfigMessage.ORDER_STATUS_CLOSED;
						}
						else if (Integer.parseInt((object[i]).toString()) == 2) {
							object[i] = ConfigMessage.ORDER_STATUS_PROCESSING;
						}
						else if (Integer.parseInt((object[i]).toString()) == 3) {
							object[i] = ConfigMessage.ORDER_STATUS_CANCELLED;
						}
						else if (Integer.parseInt((object[i]).toString()) == 4) {
							object[i] = ConfigMessage.ORDER_STATUS_HOLDING;
						}
						else {
							object[i] = "NA";
						}
					}
					tempResult.add(object[i]);
				}
				queryTesult.add(tempResult);
			}
		}
		return queryTesult;
	}

}
