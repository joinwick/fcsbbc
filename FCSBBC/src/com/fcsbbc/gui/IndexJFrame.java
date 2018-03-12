/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.utils.UIFont;

/**
 * @author luo.changshu
 *
 */
public class IndexJFrame extends JFrame implements MouseListener, ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(IndexJFrame.class.getName());
	// 定义用户对象
	private UserBaseBean userBaseBean;
	// 定义辅助变量
	int sign_tradeClearing = 0;
	int sign_fundManage = 0;
	int sign_userManage = 0;
	int sign_systemConfigure = 0;
	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	// 定义全局组件
	JPanel backgroundPanel, topPanel, topMenu, topPrompt, centerPanel, subPanel, subMenu;
	JTabbedPane jTabbedPane;
//	JLabel home, baseData, purchase_sale_stock, userManager;
	JLabel userManage, tradeClearing, fundManage, tradeQuery, tradeMonitor, instrumentMaintain, systemConfigure;
	public IndexJFrame(UserBaseBean userBaseBean) {
		this.userBaseBean = userBaseBean;
		//窗口淡入淡出
		new WindowFade(this);
		// 设置tab面板缩进
		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));
		try {
			Image imgae = ImageIO.read(new File("image/logo.png"));
			this.setIconImage(imgae);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Image File not exist in <IndexJFrame:IndexJFrame>", e.getMessage());
		}
		initBackgroundPanel();
		this.setTitle("基于区块链的期货结算系统");
		this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	// 初始化背景面板
	public void initBackgroundPanel() {
		backgroundPanel = new JPanel(new BorderLayout());
		initTop();
		initCenterPanel();
		backgroundPanel.add(topPanel, "North");
		backgroundPanel.add(centerPanel, "Center");
		this.add(backgroundPanel);
	}
	
	// 初始化顶部面板
	public void initTop() {
		initTopMenu();
		initTopPrompt();
		topPanel = new JPanel(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(width, 40));
		topPanel.add(topMenu, "West");
		topPanel.add(topPrompt, "East");
	}
	
	// 初始化顶部菜单
	public void initTopMenu() {
		topMenu = new JPanel();
		topMenu.setPreferredSize(new Dimension(500, 40));
		topMenu.setOpaque(false);
		String[] itemsList = {};
		//	customer
		if (this.userBaseBean.getUser_role() == 1) {
			itemsList = new String[]{"交易结算", "资金管理", "用户管理", "系统设置"};
			tradeClearing = CreateMenuLabel(tradeClearing, itemsList[0], "tradeClearing", topMenu);
			tradeClearing.setName("tradeClearing");
			fundManage = CreateMenuLabel(fundManage, itemsList[1], "fundManage", topMenu);
			fundManage.setName("fundManage");
			userManage = CreateMenuLabel(userManage, itemsList[2], "userManage", topMenu);
			userManage.setName("userManage");
			systemConfigure = CreateMenuLabel(systemConfigure, itemsList[3], "systemConfigure", topMenu);
			systemConfigure.setName("systemConfigure");
		}
		//	member
		else if (this.userBaseBean.getUser_role() == 2) {
			itemsList = new String[]{"交易查询", "资金管理", "用户管理"};
			tradeQuery = CreateMenuLabel(tradeQuery, itemsList[0], "tradeQuery", topMenu);
			tradeQuery.setName("tradeQuery");
			fundManage = CreateMenuLabel(fundManage, itemsList[1], "fundManage", topMenu);
			fundManage.setName("fundManage");
			userManage = CreateMenuLabel(userManage, itemsList[2], "userManage", topMenu);
			userManage.setName("userManage");
		}
		//	regulator
		else if (this.userBaseBean.getUser_role() == 3) {
			itemsList = new String[]{"交易监控", "合约维护", "用户管理", "系统设置"};
			tradeMonitor = CreateMenuLabel(tradeMonitor, itemsList[0], "tradeMonitor", topMenu);
			tradeMonitor.setName("tradeMonitor");
			instrumentMaintain = CreateMenuLabel(instrumentMaintain, itemsList[1], "instrumentMaintain", topMenu);
			instrumentMaintain.setName("instrumentMaintain");
			userManage = CreateMenuLabel(userManage, itemsList[2], "userManage", topMenu);
			userManage.setName("userManage");
			systemConfigure = CreateMenuLabel(systemConfigure, itemsList[3], "systemConfigure", topMenu);
			systemConfigure.setName("systemConfigure");
		}
	}
	
	// 创建顶部菜单Label
	public JLabel CreateMenuLabel(JLabel jLabel, String text, String name, JPanel jPanel) {
		JLabel line = new JLabel("<html>&nbsp;<font color='#D2D2D2'>|</font>&nbsp;</html>");
		Icon icon = new ImageIcon("image/" + name + ".png");
		jLabel = new JLabel(icon);
		jLabel.setText("<html><font color='black'>" + text + "</font>&nbsp;</html>");
		jLabel.addMouseListener(this);
		jLabel.setFont(UIFont.DEFAULT_FONT);
		jPanel.add(jLabel);
		if (!"userManager".equals(name)) {
			jPanel.add(line);
		}
		return jLabel;
	}
	
	// 初始化顶部欢迎面板
	public void initTopPrompt() {
		Icon icon = new ImageIcon("image/male.png");
		JLabel label = new JLabel(icon);
		if (userBaseBean != null) {
			String user_name = this.userBaseBean.getUser_name();
			if (null != user_name && !user_name.equals("")) {
				label.setText("<html><font color='black'>欢迎您, </font><font color='#336699'><b>" + user_name + "</b></font></html>");
			}
			else {
				label.setText("<html><font color='black'>欢迎您, </font><font color='#336699'><b>" + this.userBaseBean.getUser_id() + "</b></font></html>");
			}
		} 
		else {
			label.setText("<html><font color='black'>欢迎您，</font><font color='#336699'><b></b></font></html>");
		}
		label.setFont(UIFont.DEFAULT_FONT);
		topPrompt = new JPanel();
		topPrompt.setPreferredSize(new Dimension(300, 40));
		topPrompt.setOpaque(false);
		topPrompt.add(label);
	}
	
	// 初始化中心面板
	public void initCenterPanel() {
		//	customer
		if (this.userBaseBean.getUser_role() == 1) {
			centerPanel = new JPanel(new BorderLayout());
			tradeClearing.setText("<html><font color='#336699' style='font-weight:bold'>" + "交易结算" + "</font>&nbsp;</html>");
			createTradingPane();
			centerPanel.setOpaque(false);
		}
		//	member
		else if (this.userBaseBean.getUser_role() == 2) {
			centerPanel = new JPanel(new BorderLayout());
			tradeClearing.setText("<html><font color='#336699' style='font-weight:bold'>" + "交易结算" + "</font>&nbsp;</html>");
			createTradingPane();
			centerPanel.setOpaque(false);
		}
		//	regulator
		else if (this.userBaseBean.getUser_role() == 3) {
			centerPanel = new JPanel(new BorderLayout());
			tradeClearing.setText("<html><font color='#336699' style='font-weight:bold'>" + "交易结算" + "</font>&nbsp;</html>");
			createTradingPane();
			centerPanel.setOpaque(false);
		}
	}

	// 初始化辅助变量
	public void initSign() {
		sign_tradeClearing = 0;
		sign_fundManage = 0;
		sign_userManage = 0;
		sign_systemConfigure = 0;
	}

	// 创建交易结算面板
	public void createTradingPane() {
		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(UIFont.DEFAULT_FONT);
		jTabbedPane.addTab("委托", new QuoteJPanel(userBaseBean).backgroundPanel);
		jTabbedPane.addTab("成交", new TransactionJPanel(userBaseBean).backgroundPanel);
		jTabbedPane.addTab("持仓", new PositionJPanel(userBaseBean).backgroundPanel);
		jTabbedPane.addTab("合约", new InstrumentJPanel(userBaseBean).backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
//		centerPanel.removeAll();
//		try {
//			Image bgimg = ImageIO.read(new File("image/indexbackground.png"));
//			ImagePanel centerBackground = new ImagePanel(bgimg);
//			centerPanel.add(centerBackground, "Center");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	//	创建资金管理面板
	public void createFundPane() {
//		centerPanel.removeAll();
//		// 设置tab标题位置
//		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		// 设置tab布局
//		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
//		jTabbedPane.setFont(UIFont.DEFAULT_FONT);
//		jTabbedPane.addTab("商品管理", new GoodsManagerJPanel().backgroundPanel);
//		centerPanel.add(jTabbedPane, "Center");
		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(UIFont.DEFAULT_FONT);
		jTabbedPane.addTab("资金流水", new FundDetailJPanel(userBaseBean).backgroundPanel);
		jTabbedPane.addTab("资金入链", new FundImportJPanel(userBaseBean).backgroundPanel);
		jTabbedPane.addTab("资金出链", new FundExportJPanel(userBaseBean).backgroundPanel);
//		jTabbedPane.addTab("持仓", new PositionJPanel(userBaseBean).backgroundPanel);
//		jTabbedPane.addTab("合约", new InstrumentJPanel(userBaseBean).backgroundPanel);
//		jTabbedPane.addTab("资金", new FundDetailJPanel(userBaseBean).backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
	}

	//	创建用户管理面板
	public void createUserManagePane() {
		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(UIFont.DEFAULT_FONT);
		jTabbedPane.addTab("用户详情", new UserDetailJPanel(userBaseBean).backgroundPanel);
		jTabbedPane.addTab("修改密码", new UserPasswordModifyJPanel(userBaseBean).backgroundPanel);
//		jTabbedPane.addTab("出库单", new StockOutputManagerJPanel(user).backgroundPanel);
//		jTabbedPane.addTab("仓库管理", new WarehouseManagerJPanel().backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
	}

	//	创建系统设置管理面板 
	public void createSystemManagePane() {
		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(UIFont.DEFAULT_FONT);
		jTabbedPane.addTab("界面设置", new SystemUIJPanel(userBaseBean).backgroundPanel);
		jTabbedPane.addTab("系统参数", new SystemParameterJPanel(userBaseBean).backgroundPanel);
//		jTabbedPane.addTab("出库单", new StockOutputManagerJPanel(user).backgroundPanel);
//		jTabbedPane.addTab("仓库管理", new WarehouseManagerJPanel().backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
	}

	//	创建用户管理面板
//	public void creatUserManagerTab() {
//		centerPanel.removeAll();
//		// 设置tab标题位置
//		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		// 设置tab布局
//		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
//		jTabbedPane.setFont(UIFont.DEFAULT_FONT);
//		jTabbedPane.addTab("用户管理", new UserManagerJPanel(user, this).backgroundPanel);
//		centerPanel.add(jTabbedPane, "Center");
//	}
		
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == tradeClearing) {
			initSign();
			sign_tradeClearing = 1;
			createTradingPane();
			tradeClearing.setText("<html><font color='#336699' style='font-weight:bold'>" + "交易结算" + "</font>&nbsp;</html>");
			fundManage.setText("<html><font color='black'>" + "资金管理" + "</font>&nbsp;</html>");
			userManage.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			systemConfigure.setText("<html><font color='black'>" + "系统管理" + "</font>&nbsp;</html>");
		} 
		else if (e.getSource() == fundManage) {
			initSign();
			sign_fundManage = 1;
			createFundPane();
			fundManage.setText("<html><font color='#336699' style='font-weight:bold'>" + "资金管理" + "</font>&nbsp;</html>");
			tradeClearing.setText("<html><font color='black'>" + "交易结算" + "</font>&nbsp;</html>");
			userManage.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			systemConfigure.setText("<html><font color='black'>" + "系统设置" + "</font>&nbsp;</html>");
		} 
		else if (e.getSource() == userManage) {
			initSign();
			sign_userManage = 1;
			createUserManagePane();
			userManage.setText("<html><font color='#336699' style='font-weight:bold'>" + "用户管理" + "</font>&nbsp;</html>");
			tradeClearing.setText("<html><font color='black'>" + "交易结算" + "</font>&nbsp;</html>");
			fundManage.setText("<html><font color='black'>" + "资金管理" + "</font>&nbsp;</html>");
			systemConfigure.setText("<html><font color='black'>" + "系统设置" + "</font>&nbsp;</html>");
		} 
		else if (e.getSource() == systemConfigure) {
			initSign();
			sign_systemConfigure = 1;
			createSystemManagePane();
			systemConfigure.setText("<html><font color='#336699' style='font-weight:bold'>" + "系统设置" + "</font>&nbsp;</html>");
			tradeClearing.setText("<html><font color='black'>" + "交易结算" + "</font>&nbsp;</html>");
			fundManage.setText("<html><font color='black'>" + "资金管理" + "</font>&nbsp;</html>");
			userManage.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
		} else {
			System.out.println("ok");
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == tradeClearing) {
			if (sign_tradeClearing == 0) {
				tradeClearing.setText("<html><font color='black'>" + "交易结算" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == fundManage) {
			if (sign_fundManage == 0) {
				fundManage.setText("<html><font color='black'>" + "资金管理" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == userManage) {
			if (sign_userManage == 0) {
				userManage.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == systemConfigure) {
			if (sign_systemConfigure == 0) {
				systemConfigure.setText("<html><font color='black'>" + "系统设置" + "</font>&nbsp;</html>");
			}
		}
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

}
