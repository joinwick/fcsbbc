/**
 * 
 */
package com.fcsbbc.database;

import java.sql.SQLException;
import java.util.List;

/**General SQL Operation
 * @author luo.changshu
 *
 */
public interface BaseSQLInterface {
	
	public List<Object[]> query(String sql, Object[] objects) throws SQLException;
	
	public boolean queryExists(String sql, Object[] objects) throws SQLException;
	
	public List<Object[]> query(String sql, int columnNum, Object[] objects) throws SQLException;
	
	public List<Object[]> query(String sql, int columnNum, Object[] objects, boolean cryptologyFlag, int cryptologyNum, boolean hexStringFlag) throws SQLException;
	
	public boolean query(String sql, Object[] objects, int columnNum, boolean cryptologyFlag, int cryptologyNum, boolean hexStringFlag) throws SQLException;
	
	public boolean insert(String sql, Object[] objects) throws SQLException;
	
	public boolean insert(String sql, Object[] objects, boolean cryptologyFlag, int cryptologyNum, boolean hexStringFlag) throws SQLException;
	
	public boolean update(String sql, Object[] objects) throws SQLException;
	
	public boolean update(String sql, Object[] objects, boolean cryptologyFlag, int cryptologyNum, boolean hexStringFlag) throws SQLException;
	
	public boolean delete(String sql, Object[] objects) throws SQLException;

}
