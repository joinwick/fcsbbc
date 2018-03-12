/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author luo.changshu
 *
 */
public class ColorTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component render = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if ("Processing".equals(table.getValueAt(row, 6).toString()) || "Holding".equals(table.getValueAt(row, 6).toString())) {
            render.setBackground (Color.LIGHT_GRAY);
        }
		DefaultTableCellRenderer dTableCellRenderer = new DefaultTableCellRenderer();//单元格渲染器
		dTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(Object.class, dTableCellRenderer);
        return render;
	}
}
