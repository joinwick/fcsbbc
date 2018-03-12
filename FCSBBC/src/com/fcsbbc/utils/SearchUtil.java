/**
 * 
 */
package com.fcsbbc.utils;

import java.util.ArrayList;
import java.util.List;

import com.fcsbbc.common.service.BaseDataServiceOperation;

/**
 * @author luo.changshu
 *
 */
public class SearchUtil {
	
	public static boolean loopSearch(Object[] objects, Object searchValue) {
		boolean searchResult = false;
		if ((objects != null && objects.length > 0) && 
				(searchValue != null && searchValue.toString().length() > 0)) {
			for (Object object : objects) {
				if (object.equals(searchValue)) {
					searchResult = true;
					break;
				}
			}
		}
		return searchResult;
	}
	
	public static Object[] filterItems(Object[] objects, String filterCondition) {
		Object[] filterItems = null;
		List<Object> tempList = new ArrayList<>();
		for (Object object : objects) {
			if (object.toString().startsWith(filterCondition)) {
				tempList.add(object);
			}
		}
		if (tempList != null && tempList.size() > 0) {
			filterItems = new Object[tempList.size()];
			for (int i = 0; i < tempList.size(); i++) {
				filterItems[i] = tempList.get(i);
			}
		}
		return filterItems;
	}
	
	public static void main(String[] args) {
		BaseDataServiceOperation baseDataServiceOperation = new BaseDataServiceOperation();
		Object[] objects = baseDataServiceOperation.getInstrumentIDArray();
		Object[] filterItems = filterItems(objects, "ax");
		if (filterItems != null ) {
			for (int i = 0; i < filterItems.length; i++) {
				System.out.println(filterItems[i]);
			}
		}
		else {
			System.out.println("No Match!");
		}
		
	}
}
