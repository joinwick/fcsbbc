/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import com.fcsbbc.common.domain.OrderBaseBean;
import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.common.service.QuoteServiceOperation;
import com.fcsbbc.utils.BaseTable;
import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.LocalizeConfig;
import com.fcsbbc.utils.TableUtil;
import com.fcsbbc.utils.UIFont;

/**
 * @author luo.changshu
 * @category Instrument Quote
 */
public class QuoteJPanel implements ActionListener, MouseListener, DocumentListener{
	// 定义全局组件
	JPanel backgroundPanel, topPanel, quickToolPanel, searchPanel, tablePanel;
	JComboBox<Object> tradeSideComboBox, select_warehouse;
	JTextField instrumentSearchTextField;
	BaseTable baseTableModule;
	JTable quoteTable;
	JScrollPane jScrollPane;
	JLabel instrumentLabel, tradeSideLabel, quickToolLabel;
	private int orderConfirmResult = -2, orderCreateResult = -2;
	TableCellRenderer tableCellRenderer = new ColorTableCellRenderer();
	// 用户对象
	UserBaseBean userBaseBean;
	OrderBaseBean orderBaseBean;
	JPopupMenu jPopupMenu;
	public QuoteJPanel(UserBaseBean userBaseBean) {
		this.userBaseBean = userBaseBean;
		backgroundPanel = new JPanel(new BorderLayout());
		initTopPanel();
		initTablePanel();
	}

	/**
	 * initial top panel
	 */
	private void initTopPanel() {
		// TODO Auto-generated method stub
		topPanel = new JPanel(new BorderLayout());
		initToolPanel();
		initSearchPanel();
		//add topPanel to backgroundPanel
		backgroundPanel.add(topPanel, "North");
	}

	/**
	 * initial tool panel
	 */
	private void initToolPanel() {
		// TODO Auto-generated method stub
		quickToolPanel = new JPanel();
		Icon newQuoteIcon = new ImageIcon("image/add.png");
		quickToolLabel = new JLabel(newQuoteIcon);
		quickToolLabel.setToolTipText(LocalizeConfig.SHORT_CUT_NEW_ORDER);
		quickToolLabel.addMouseListener(this);
		//	add label to quickToolPanel
		quickToolPanel.add(quickToolLabel);
		topPanel.add(quickToolPanel, "West");
	}
	
	/**
	 * initial search panel
	 */
	private void initSearchPanel() {
		// TODO Auto-generated method stub
		searchPanel = new JPanel();
		//	instrument search
		instrumentSearchTextField = new JTextField(10);
		instrumentSearchTextField.getDocument().addDocumentListener(this);
		//	trade side
		tradeSideComboBox = new JComboBox<>();
//		CategoryServiceImpl categoryService = new CategoryServiceImpl();
		List<Object[]> tradeSideList = new ArrayList<>();
		Object[] tradeSideBuy = {"0", ConfigMessage.ORDER_TRADESIDE_BUY};
		Object[] tradeSideSell = {"1", ConfigMessage.ORDER_TRADESIDE_SELL};
		tradeSideList.add(tradeSideBuy);
		tradeSideList.add(tradeSideSell);
		tradeSideComboBox.addItem(new QueryItem(LocalizeConfig.QUERY_CONDITION_ALL, LocalizeConfig.QUERY_CONDITION_ALL));
		if (tradeSideList != null && tradeSideList.size() > 0) {
			for (Object[] object : tradeSideList) {
				tradeSideComboBox.addItem(new QueryItem((String) object[0], (String) object[1]));
			}
		}
		tradeSideComboBox.addActionListener(this);
		//	other type
		//	add label
		instrumentLabel = new JLabel(LocalizeConfig.QUERY_HINTS_ORDER_INSTRUMENT);
		tradeSideLabel = new JLabel(LocalizeConfig.QUERY_HINTS_ORDER_TRADESIDE);
		//	add label & text-field to searchPanel
		searchPanel.add(instrumentLabel);
		searchPanel.add(instrumentSearchTextField);
		searchPanel.add(tradeSideLabel);
		searchPanel.add(tradeSideComboBox);
		topPanel.add(searchPanel, "East");
	}

	/**
	 * initial table panel
	 */
	private void initTablePanel() {
		// TODO Auto-generated method stub
		String[] conditionParams = { "", LocalizeConfig.QUERY_CONDITION_ALL};
		String[] quoteTableColumns = {
				LocalizeConfig.ORDER_PROPERTY_ORDER_ID, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_INSTRUMENT, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_TRADESIDE, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_PRICE, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_LOTS, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_TIME, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_STATUS, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_USERCODE};
		QuoteServiceOperation quoteServiceOperation = new QuoteServiceOperation();
		Vector<Vector<Object>> vector = new Vector<>();
		vector = quoteServiceOperation.queryQuote(conditionParams, ConfigMessage.DEFAULT_DATABASE_TYPE);
		baseTableModule = new BaseTable(quoteTableColumns, vector);
		quoteTable = new JTable(baseTableModule);
		
		quoteTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				//mouse right click
				if (arg0.getButton() == MouseEvent.BUTTON3) {
					int row = quoteTable.getSelectedRow();
					if (row == -1) {
						return;
					}
					int[] rows = quoteTable.getSelectedRows();
					boolean isSelected = false;
					for(int r : rows) {
	                    if(row == r) {
	                    	isSelected = true;
	                        break;
	                    }
	                }
					if(!isSelected) {
						quoteTable.setRowSelectionInterval(row, row);
					}
					int column_count = quoteTable.getColumnCount();
					String userSystemID = "NA";
					if (column_count > 0) {
						String orderID = quoteTable.getValueAt(row, 0).toString();
						String tradeSide = quoteTable.getValueAt(row, 2).toString();
						String price = quoteTable.getValueAt(row, 3).toString();
						String lots = quoteTable.getValueAt(row, 4).toString();
						String instrumentID = quoteTable.getValueAt(row, 1).toString();
						String orderStatus = quoteTable.getValueAt(row, 6).toString();
						userSystemID = quoteTable.getValueAt(row, 7).toString();
						orderBaseBean = new OrderBaseBean(orderID, tradeSide, new BigDecimal(price), new BigDecimal(lots), instrumentID, orderStatus); 
					}
					//报单状态为new且报单不是当前客户的报单
					if ("New".equals(orderBaseBean.getOrder_status()) && (!userSystemID.equals(orderBaseBean.getUser_system_id()))) {
						createPopupMenu(userBaseBean, orderBaseBean, QuoteJPanel.this, "500000.00");
						jPopupMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
					}
//					createPopupMenu(userBaseBean, orderBaseBean, QuoteJPanel.this, "500000.00");
//					jPopupMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
					if (orderConfirmResult == 0) {
						System.out.println("update database");
						refreshTable();//refresh table data
					}
				}
				//鼠标双击
				else if (arg0.getClickCount() == 2) {
					int row = quoteTable.getSelectedRow();
					int column_count = quoteTable.getColumnCount();
					if (column_count > 0) {
						String orderID = quoteTable.getValueAt(row, 0).toString();
						String tradeSide = quoteTable.getValueAt(row, 2).toString();
						String price = quoteTable.getValueAt(row, 3).toString();
						String lots = quoteTable.getValueAt(row, 4).toString();
						String instrumentID = quoteTable.getValueAt(row, 1).toString();
						String orderStatus = quoteTable.getValueAt(row, 6).toString();
						orderBaseBean = new OrderBaseBean(orderID, tradeSide, new BigDecimal(price), new BigDecimal(lots), instrumentID, orderStatus); 
					}
					JDialog jDialog = new OrderDealConfirmJDialog(userBaseBean, orderBaseBean, QuoteJPanel.this, "500000.00");
					jDialog.setModal(true);
					jDialog.setVisible(true);
					orderConfirmResult = ((OrderDealConfirmJDialog)jDialog).orderConfirmResult();
					if (orderConfirmResult == 0) {
						System.out.println("update database");
						refreshTable();//refresh table data
					}
				}
            }
		});
		TableUtil.setTableStyle(quoteTable);
		//	get default table mode
		DefaultTableColumnModel defaultTableColumnModel = (DefaultTableColumnModel) quoteTable.getColumnModel();
		//界面隐藏第八列:客户编码
		defaultTableColumnModel.getColumn(7).setMinWidth(0);
		defaultTableColumnModel.getColumn(7).setMaxWidth(0);
		
//		TableCellRenderer tableCellRenderer = new ColorTableCellRenderer();
		quoteTable.setDefaultRenderer(Object.class, tableCellRenderer);
		jScrollPane = new JScrollPane(quoteTable);
		TableUtil.setJScrollPaneStyle(jScrollPane);
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		tablePanel.add(jScrollPane);
		backgroundPanel.add(tablePanel, "Center");
	}
	
	private void createPopupMenu(UserBaseBean userBaseBean, OrderBaseBean orderBaseBean, QuoteJPanel quoteJPanel, String fundAvailable) {
		jPopupMenu = new JPopupMenu();
		JMenuItem jMenuItem = new JMenuItem();
		jMenuItem.setText(LocalizeConfig.MOUSE_CLICK_RIGHT_MENUITEM_DEAL);
		jMenuItem.setIcon(new ImageIcon("image/delete.png"));
		jMenuItem.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK));
		jMenuItem.setFont(UIFont.DEFAULT_FONT);
		jMenuItem.setHorizontalAlignment(SwingConstants.LEFT);
		jMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JDialog jDialog = new OrderDealConfirmJDialog(userBaseBean, orderBaseBean, quoteJPanel, fundAvailable);
				jDialog.setModal(true);
				jDialog.setVisible(true);
//				orderConfirmResult = ((OrderDealConfirmJDialog)jDialog).orderConfirmResult();
//				System.out.println("final operationResult = " + orderConfirmResult);
			}
		});
		jPopupMenu.add(jMenuItem);
	}
	
	public void refreshTable() {
		backgroundPanel.remove(tablePanel);
		String searchInstrument = instrumentSearchTextField.getText().trim();
		QueryItem tradeSideItem = (QueryItem) tradeSideComboBox.getSelectedItem();
		String[] conditionParams = {searchInstrument, tradeSideItem.getKey()};
		//"报单编号", "合约", "买卖", "委托价", "委托量", "报单时间", "状态", "客户编码";
		String[] quoteTableColumns = {
				LocalizeConfig.ORDER_PROPERTY_ORDER_ID, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_INSTRUMENT, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_TRADESIDE, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_PRICE, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_LOTS, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_TIME, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_STATUS, 
				LocalizeConfig.ORDER_PROPERTY_ORDER_USERCODE};
		QuoteServiceOperation quoteServiceOperation = new QuoteServiceOperation();
		Vector<Vector<Object>> vector = new Vector<>();
		vector = quoteServiceOperation.queryQuote(conditionParams, ConfigMessage.DEFAULT_DATABASE_TYPE);
		baseTableModule = new BaseTable(quoteTableColumns, vector);
		quoteTable = new JTable(baseTableModule);
		TableUtil.setTableStyle(quoteTable);
		
		DefaultTableColumnModel defaultTableColumnModel = (DefaultTableColumnModel) quoteTable.getColumnModel();
		defaultTableColumnModel.getColumn(7).setMinWidth(0);
		defaultTableColumnModel.getColumn(7).setMaxWidth(0);
		quoteTable.setDefaultRenderer(Object.class, tableCellRenderer);
		jScrollPane = new JScrollPane(quoteTable);
		TableUtil.setJScrollPaneStyle(jScrollPane);
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		tablePanel.add(jScrollPane);
		backgroundPanel.add(tablePanel, "Center");
		backgroundPanel.validate();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
//		refreshTable();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		refreshTable();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		refreshTable();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == quickToolLabel) {
//			String fundAvailable = getFund(userBaseBean.getUser_id());	//实时获取用户资金
//			new CreateOrderJFrame(userBaseBean, this, fundAvailable);
			JDialog jDialog = new CreateOrderJDialog(userBaseBean, this, "500000.00");
			jDialog.setModal(true);
			jDialog.setVisible(true);
			orderCreateResult = ((CreateOrderJDialog)jDialog).orderCreatedResult();
			if (orderCreateResult == 2) {
				System.out.println("insert database");
				refreshTable();//refresh table data
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == quickToolLabel) {
			quickToolLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	//报单方向筛选
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == tradeSideComboBox) {
			refreshTable();
		}
	}

}
