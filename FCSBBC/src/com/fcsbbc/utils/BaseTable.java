/**
 * 
 */
package com.fcsbbc.utils;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * @author luo.changshu
 *
 */
public class BaseTable extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vector<Vector<Object>> rowVector;
	Vector<String> columnVector;
	/**
	 * 
	 */
	public BaseTable(String[] parameters, Vector<Vector<Object>> vector) {
		// TODO Auto-generated constructor stub
		// 初始化列
		this.columnVector = new Vector<String>();
		for (String column : parameters) {
			columnVector.add(column);
		}
		// 初始化行
		this.rowVector = vector;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 * 
	 */
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.columnVector.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.rowVector.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return ((Vector<?>) rowVector.get(rowIndex)).get(columnIndex);
	}
	
	public String getColumnName(int columnIndex) {
		return this.columnVector.get(columnIndex);
	}
}
