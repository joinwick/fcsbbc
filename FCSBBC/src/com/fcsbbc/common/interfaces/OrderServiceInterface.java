/**
 * 
 */
package com.fcsbbc.common.interfaces;

import java.util.concurrent.ConcurrentHashMap;

import com.fcsbbc.common.domain.OrderBaseBean;

/**
 * @author luo.changshu
 *
 */
public interface OrderServiceInterface {

	public boolean judgeOrderExist(String orderID, ConcurrentHashMap<String, OrderBaseBean> OrdersHashMap) throws Exception;

	public boolean addOrderToHashTable(String orderID, ConcurrentHashMap<String, OrderBaseBean> OrdersHashMap) throws Exception;
	
}
