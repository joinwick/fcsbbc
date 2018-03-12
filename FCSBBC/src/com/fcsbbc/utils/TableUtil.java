/**
 * 
 */
package com.fcsbbc.utils;

import java.awt.Color;
import java.util.Enumeration;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * @author luo.changshu
 *
 */
public class TableUtil {
	
	public static void setTableStyle(JTable jTable){
		//	set color of selected row
		jTable.setSelectionBackground(new Color(51, 153, 255));
		//	set height of  
		jTable.setRowHeight(35);
		jTable.setAutoCreateRowSorter(true);
		//设置表头内容居中显示
		DefaultTableCellRenderer defaultTableCellRenderer = (DefaultTableCellRenderer) jTable.getTableHeader().getDefaultRenderer();
		defaultTableCellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
//		DefaultTableCellHeaderRenderer defaultTableCellHeaderRenderer = new DefaultTableCellHeaderRenderer();
//		defaultTableCellHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
//		jTable.getTableHeader().setDefaultRenderer(defaultTableCellHeaderRenderer);
		//设置表格内容居中显示
//		DefaultTableCellRenderer dTableCellRenderer = new DefaultTableCellRenderer();//单元格渲染器
//		dTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
//		jTable.setDefaultRenderer(Object.class, dTableCellRenderer);
		jTable.setFont(UIFont.DEFAULT_FONT);
		fitTableColumns(jTable);
	}
	
	private static void fitTableColumns(JTable jTable) {
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JTableHeader jTableHeader = jTable.getTableHeader();
		int rowCount = jTable.getRowCount();
//		Enumeration columns = jTable.getColumnModel().getColumns();
		Enumeration<TableColumn> tableColumns = jTable.getColumnModel().getColumns();
		while (tableColumns.hasMoreElements()) {
//			TableColumn column = (TableColumn) tableColumns.nextElement();
			TableColumn tableColumn = tableColumns.nextElement();
			int columnIndex = jTableHeader.getColumnModel().getColumnIndex(tableColumn.getIdentifier());
			int width = (int) jTableHeader.getDefaultRenderer()
					.getTableCellRendererComponent(jTable, tableColumn.getIdentifier(), false, false, -1, columnIndex)
					.getPreferredSize().getWidth();
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				int preferedWidth = (int) jTable.getCellRenderer(rowIndex, columnIndex)
						.getTableCellRendererComponent(jTable, jTable.getValueAt(rowIndex, columnIndex), false, false, rowIndex, columnIndex)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			//	set column weight fit with content
			jTableHeader.setResizingColumn(tableColumn);
			tableColumn.setWidth(width + jTable.getIntercellSpacing().width);
		}
	}
	
	/**
	 * set JScrollPane format
	 * @param jScrollPane
	 */
	public static void setJScrollPaneStyle(JScrollPane jScrollPane) {
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPane.getViewport().setOpaque(false);
		jScrollPane.getVerticalScrollBar().setOpaque(false);
	}
}
