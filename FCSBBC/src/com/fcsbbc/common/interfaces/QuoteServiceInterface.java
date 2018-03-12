/**
 * 
 */
package com.fcsbbc.common.interfaces;

import java.util.Vector;

/**
 * @author luo.changshu
 *
 */
public interface QuoteServiceInterface {
	//	add new quote order
	public boolean newQuote(Object[] quoteParametersArray, int dataBaseTypeFlag) throws Exception;
	
	//	query quote order
	public Vector<Vector<Object>> queryQuote(Object[] quoteParametersArray, int dataBaseTypeFlag) throws Exception;
	
	//	update quote order
	public boolean updateOrderInfo(Object[] quoteParametersArray, int dataBaseTypeFlag) throws Exception;
}
