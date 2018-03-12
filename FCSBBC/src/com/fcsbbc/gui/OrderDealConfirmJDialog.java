/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.common.domain.OrderBaseBean;
import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.common.service.BaseDataServiceOperation;
import com.fcsbbc.common.service.QuoteServiceOperation;
import com.fcsbbc.cryptology.HashModule;
import com.fcsbbc.utils.ArithmeticUtil;
import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.SearchUtil;
import com.fcsbbc.utils.UIColor;
import com.fcsbbc.utils.UIFont;
import com.fcsbbc.utils.UtilTool;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author luo.changshu
 *
 */
public class OrderDealConfirmJDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(OrderDealConfirmJDialog.class.getName());
	private final JPanel contentPanel = new JPanel();
	private static final int feeSetp = 10;
	private JTextField fundAvailableTextField;
	private JTextField instrumentTextField;
	private JTextField tradeSideTextField;
	private JTextField quantityTextField;
	private JTextField priceTextField;
	private JTextField feeTextField;
	private JButton openFlagOpenButton;
	private JButton openFlagCloseYButton;
	private JButton openFlagCloseTButton;
	private Object[] instrumentIDArray;
	private BigDecimal priceTick;
	public int operationResult = -2;
	private String maxPrice, minPrice;
	private String convertTradeSide = "NA", displayTradeSide = "NA";
	private String openSide = "0";
	private final static int actualWidth = 450, actualHeight = 360;
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	QuoteJPanel quoteJPanel;
	UserBaseBean userBaseBean;
	OrderBaseBean orderBaseBean;
	String fundAvailable;
	OrderBaseBean orderConfirmInfo;
	QuoteServiceOperation quoteServiceOperation = new QuoteServiceOperation();
	
	public int orderConfirmResult() {
		return operationResult;
	}
	
	public void closeCurrentWindow() {
		int operationResult = orderConfirmResult();
		if (operationResult == 0) {
			this.dispose();
		}
		else if (operationResult == 1) {
			logger.info("User Choice Cancel Button in function<OrderDealConfirmJDialog:closeCurrentWindow>");
		}
		else if (operationResult == -1) {
			logger.info("User Choice Close Button in function<OrderDealConfirmJDialog:closeCurrentWindow>");
		}
		else if (operationResult == 2) {
			this.dispose();
			logger.info("System need initial in function<OrderDealConfirmJDialog:closeCurrentWindow>");
		}
		else if (operationResult == 3) {
			this.dispose();
			logger.info("User Cancel Transaction Manually in function<OrderDealConfirmJDialog:closeCurrentWindow>");
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public OrderDealConfirmJDialog(UserBaseBean userBaseBean, OrderBaseBean orderBaseBean, QuoteJPanel quoteJPanel, String fundAvailable) {
		this.userBaseBean = userBaseBean;
		this.quoteJPanel = quoteJPanel;
		this.fundAvailable = fundAvailable;
		this.setTitle("成交确认");
		if (width > actualWidth && height > actualHeight) {
			this.setBounds((width - actualWidth) / 2, (height - actualHeight) / 2, actualWidth, actualHeight);
		}
		else {
			this.setBounds(0, 0, width, height);
		}
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		
		BaseDataServiceOperation baseDataServiceOperation = new BaseDataServiceOperation();
        instrumentIDArray = baseDataServiceOperation.getInstrumentIDArray();	//get from database
        String fundAvailableFormat = ArithmeticUtil.getFormat(fundAvailable);
		
		JLabel fundAvailableLabel = new JLabel("可用资金：");
		fundAvailableLabel.setBounds(120, 13, 68, 15);
		fundAvailableLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		fundAvailableLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(fundAvailableLabel);
		
		fundAvailableTextField = new JTextField(fundAvailableFormat);
		fundAvailableTextField.setBounds(190, 10, 130, 21);
		fundAvailableTextField.setColumns(19);
		fundAvailableTextField.setOpaque(false);
		fundAvailableTextField.setEditable(false);
		fundAvailableTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		fundAvailableTextField.setFont(UIFont.DEFAULT_FONT);
		fundAvailableTextField.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(fundAvailableTextField);
		
		
		JLabel instrumentLabel = new JLabel("合约");
		instrumentLabel.setBounds(80, 53, 54, 15);
		instrumentLabel.setFont(UIFont.DEFAULT_FONT);
		instrumentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(instrumentLabel);
		
		instrumentTextField = new JTextField(orderBaseBean.getInstrument_id());
		instrumentTextField.setBounds(130, 50, 120, 21);
		instrumentTextField.setColumns(10);
//		instrumentTextField.setOpaque(false);
		instrumentTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		instrumentTextField.setEditable(false);
		instrumentTextField.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(instrumentTextField);
		
		
		JLabel tradeSideLabel = new JLabel("方向");
		tradeSideLabel.setBounds(80, 86, 54, 15);
		tradeSideLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tradeSideLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(tradeSideLabel);
		
		String tradeSide = orderBaseBean.getOrder_trade_side();
		if ("Buy".equals(tradeSide)) {
			convertTradeSide = "Sell";
			displayTradeSide = "卖出";
		}
		else if ("Sell".equals(tradeSide)) {
			convertTradeSide = "Buy";
			displayTradeSide = "买入";
		}
		tradeSideTextField = new JTextField(displayTradeSide);
		tradeSideTextField.setHorizontalAlignment(SwingConstants.LEFT);
		tradeSideTextField.setBounds(130, 82, 120, 21);
		tradeSideTextField.setColumns(10);
//		tradeSideTextField.setOpaque(false);
		tradeSideTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		tradeSideTextField.setEditable(false);
		tradeSideTextField.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(tradeSideTextField);
		
		
		JLabel openFlagLabel = new JLabel("开平");
		openFlagLabel.setBounds(80, 117, 54, 15);
		openFlagLabel.setHorizontalAlignment(SwingConstants.CENTER);
		openFlagLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(openFlagLabel);
		
		openFlagOpenButton = new JButton("开仓");
		openFlagOpenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				//check fundAvailable
				openSide = "0";
			}
		});
		openFlagOpenButton.setBounds(130, 114, 61, 23);
		openFlagOpenButton.setFont(UIFont.DEFAULT_FONT);
		openFlagOpenButton.setFocusPainted(false);
		openFlagOpenButton.setForeground(Color.black);
		openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		contentPanel.add(openFlagOpenButton);
		
		openFlagCloseYButton = new JButton("平昨");
		openFlagCloseYButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				//check position
				openSide = "1";
			}
		});
		openFlagCloseYButton.setBounds(190, 114, 61, 23);
		openFlagCloseYButton.setFont(UIFont.DEFAULT_FONT);
		openFlagCloseYButton.setFocusPainted(false);
		openFlagCloseYButton.setForeground(Color.black);
		openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPanel.add(openFlagCloseYButton);
		
		openFlagCloseTButton = new JButton("平今");
		openFlagCloseTButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				//check position
				openSide = "2";
			}
		});
		openFlagCloseTButton.setBounds(250, 114, 61, 23);
		openFlagCloseTButton.setFont(UIFont.DEFAULT_FONT);
		openFlagCloseTButton.setFocusPainted(false);
		openFlagCloseTButton.setForeground(Color.black);
		openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPanel.add(openFlagCloseTButton);
		
		JLabel quantityLabel = new JLabel("手数");
		quantityLabel.setBounds(80, 150, 54, 15);
		quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		quantityLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(quantityLabel);
		
		quantityTextField = new JTextField(orderBaseBean.getOrder_number().toString());
		quantityTextField.setBounds(130, 147, 120, 21);
		quantityTextField.setColumns(10);
//		quantityTextField.setOpaque(false);
		quantityTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		quantityTextField.setEditable(false);
		quantityTextField.setHorizontalAlignment(SwingConstants.LEFT);
		quantityTextField.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(quantityTextField);
		
		
		JLabel priceLabel = new JLabel("价格");
		priceLabel.setBounds(80, 182, 54, 15);
		priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		priceLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(priceLabel);
		
		priceTextField = new JTextField(orderBaseBean.getOrder_price().toString());
		priceTextField.setBounds(130, 179, 120, 21);
		priceTextField.setColumns(10);
		priceTextField.setHorizontalAlignment(SwingConstants.LEFT);
//		priceTextField.setOpaque(false);
		priceTextField.setEditable(false);
		priceTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		priceTextField.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(priceTextField);
		
		
		JLabel feeLabel = new JLabel("手续费");
		feeLabel.setBounds(80, 214, 54, 15);
		feeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		feeLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(feeLabel);
		
		feeTextField = new JTextField("0");
		feeTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				String currentContent = feeTextField.getText().trim();
				char keyChar = arg0.getKeyChar();
				if ((keyChar == '.') || (keyChar >= '0' && keyChar <= '9')) {
					
				}
				else if (arg0.getKeyChar() == KeyEvent.VK_ENTER || arg0.getKeyChar() == KeyEvent.VK_DELETE || arg0.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					if (currentContent == null || currentContent.length() == 0) {
						feeTextField.setText("0");
					}
				}
				else {
					arg0.consume();
				}
				if (currentContent != null && currentContent.length() >= 10) {
					arg0.consume();
				}
				String lots = quantityTextField.getText().trim();
				String price = priceTextField.getText().trim();
				if ((lots != null && lots.length() > 0) && (price != null && price.length() > 0)) {
					String cost = ArithmeticUtil.mul(lots, price).toString();
					String maxFeeContent = ArithmeticUtil.sub(fundAvailable, cost).toString();
					if (ArithmeticUtil.compareDetails(maxFeeContent, currentContent) >= 0) {
						
					}
					else {
						arg0.consume();
					}
				}
			}
		});
		feeTextField.setBounds(130, 211, 85, 21);
		feeTextField.setColumns(10);
		feeTextField.setFont(UIFont.DEFAULT_FONT);
		feeTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPanel.add(feeTextField);
		
		ImageIcon subIcon = new ImageIcon("image/sub_button.png");
		ImageIcon addIcon = new ImageIcon("image/add_button.png");
//		JButton feeSubButton = new JButton("");
		JButton feeSubButton = new JButton(subIcon);
		feeSubButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String currentContent = feeTextField.getText().trim();
				String lots = quantityTextField.getText().trim();
				String price = priceTextField.getText().trim();
				if ((lots != null && lots.length() > 0) && (price != null && price.length() > 0)) {
					String cost = ArithmeticUtil.mul(lots, price).toString();
					String maxFeeContent = ArithmeticUtil.sub(fundAvailable, cost).toString();
					if (currentContent != null && currentContent.length() > 0) {
						currentContent = ArithmeticUtil.removeDecimalPoint(currentContent);
						currentContent = ArithmeticUtil.removeDecimalZero(currentContent);
						if (ArithmeticUtil.compare(currentContent, maxFeeContent)) {
							feeTextField.setText(maxFeeContent);
						}
						else {
							String minFeeContent = ArithmeticUtil.sub(currentContent, String.valueOf(feeSetp)).toString();
							if (ArithmeticUtil.compareDetails(minFeeContent, "0") >= 0) {
								feeTextField.setText(minFeeContent);
							}
							else {
								feeTextField.setText("0");
							}
						}
					}
					else {
						feeTextField.setText("0");
					}
				}
			}
		});
		feeSubButton.setBounds(214, 211, 19, 20);
		feeSubButton.setFocusPainted(false);
		feeSubButton.setPreferredSize(new Dimension(subIcon.getIconWidth(), subIcon.getIconHeight()));
		contentPanel.add(feeSubButton);
		
//		JButton feeAddButton = new JButton("");
		JButton feeAddButton = new JButton(addIcon);
		feeAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String currentContent = feeTextField.getText().trim();
				String lots = quantityTextField.getText().trim();
				String price = priceTextField.getText().trim();
				if ((lots != null && lots.length() > 0) && (price != null && price.length() > 0)) {
					String cost = ArithmeticUtil.mul(lots, price).toString();
					String maxFeeContent = ArithmeticUtil.sub(fundAvailable, cost).toString();
					if (currentContent != null && currentContent.length() > 0) {
						currentContent = ArithmeticUtil.removeDecimalPoint(currentContent);
						currentContent = ArithmeticUtil.removeDecimalZero(currentContent);
						String limitedFeeContent = ArithmeticUtil.add(currentContent, String.valueOf(feeSetp)).toString();
						if (ArithmeticUtil.compareDetails(maxFeeContent, limitedFeeContent) >= 0) {
							feeTextField.setText(limitedFeeContent);
						}
						else {
							feeTextField.setText(maxFeeContent);
						}
					}
					else {
						feeTextField.setText("0");
					}
				}
			}
		});
		feeAddButton.setBounds(232, 211, 19, 20);
		feeAddButton.setFocusPainted(false);
		feeAddButton.setPreferredSize(new Dimension(addIcon.getIconWidth(), addIcon.getIconHeight()));
		contentPanel.add(feeAddButton);
		
		JButton resetButton = new JButton("复位");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//reset fee with 0
				feeTextField.setText("0");
			}
		});
		resetButton.setBounds(260, 209, 60, 23);
		resetButton.setBackground(UIColor.DEFAULT_COLOR);
		resetButton.setFont(UIFont.DEFAULT_FONT);
		resetButton.setFocusPainted(false);
		resetButton.setForeground(Color.black);
		contentPanel.add(resetButton);
		
		JButton okButton = new JButton("确认");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean quoteFactorCheck = true;
				List<Object[]> baseProperties;
				String instrumentID = instrumentTextField.getText().trim().toLowerCase();
				String tradeSide = "";
				String lots = quantityTextField.getText().trim();
				String price = priceTextField.getText().trim();
				String feeContent = feeTextField.getText().trim();
				String warningInfo = "";
				String initialOrderID = orderBaseBean.getOrder_id();
				//check instrumentID
				if (instrumentID != null && instrumentID.length() > 0) {
					if (!SearchUtil.loopSearch(instrumentIDArray, instrumentID)) {
						warningInfo = "合约选取无效, 请重新选择有效的报单进行成交!";
						JOptionPane.showMessageDialog(null, warningInfo, "报单合约错误", JOptionPane.ERROR_MESSAGE);
						quoteFactorCheck = false;
					}
					else {
						baseProperties = baseDataServiceOperation.getInstrumentProperty(instrumentID);
						if (baseProperties != null && baseProperties.size() == 1) {
							maxPrice = ((Object[])baseProperties.get(0))[0].toString();
							minPrice = ((Object[])baseProperties.get(0))[1].toString();
							priceTick = new BigDecimal(((Object[])baseProperties.get(0))[2].toString());
						}
						else {
							warningInfo = "合约信息已失效, 请保持网络畅通并重启软件!";
							JOptionPane.showMessageDialog(null, warningInfo, "系统初始化错误", JOptionPane.ERROR_MESSAGE);
							quoteFactorCheck = false;
							operationResult = 2;
							//call window event
//							closeCurrentWindow();
						}
					}
				}
				else {
					warningInfo = "未选取到有效合约, 请重新选择有效的报单进行成交!";
					JOptionPane.showMessageDialog(null, warningInfo, "报单合约错误", JOptionPane.ERROR_MESSAGE);
					quoteFactorCheck = false;
				}
				//check tradeSide
				if ("NA".equals(convertTradeSide)) {
					warningInfo = "报单买卖方向获取异常, 请重新选择有效的报单进行成交!";
					JOptionPane.showMessageDialog(null, warningInfo, "报单买卖方向错误", JOptionPane.ERROR_MESSAGE);
					quoteFactorCheck = false;
				}
				else {
					if (quoteFactorCheck) {
						if (quoteFactorCheck) {
							if ("Buy".equals(convertTradeSide)) {
								tradeSide = "0";
							}
							else if ("Sell".equals(convertTradeSide)) {
								tradeSide = "1";
							}
							else {
								warningInfo = "报单买卖方向获取异常, 请重新选择有效的报单进行成交!";
								JOptionPane.showMessageDialog(null, warningInfo, "报单买卖方向错误", JOptionPane.ERROR_MESSAGE);
								quoteFactorCheck = false;
							}
						}
					}
				}
				//check openSide
				if (openSide != null && openSide.length() > 0) {
					if (quoteFactorCheck) {
						if (!(Integer.parseInt(openSide) == 0 || Integer.parseInt(openSide) == 1 || Integer.parseInt(openSide) == 2)) {
							warningInfo = "报单开平方向获取异常, 请重新选择报单开平方向!";
							JOptionPane.showMessageDialog(null, warningInfo, "报单开平方向错误", JOptionPane.ERROR_MESSAGE);
							quoteFactorCheck = false;
						}
					}
				}
				else {
					warningInfo = "请重新选择有效的报单开平方向!";
					JOptionPane.showMessageDialog(null, warningInfo, "报单开平方向错误", JOptionPane.ERROR_MESSAGE);
					quoteFactorCheck = false;
				}
				//check lots
				if (lots != null && lots.length() > 0) {
					if (quoteFactorCheck) {
						if (lots.indexOf(".") >= 0) {
							warningInfo = "报单手数只能输入正整数, 请重新选择有效的报单进行成交!";
							JOptionPane.showMessageDialog(null, warningInfo, "报单手数错误", JOptionPane.ERROR_MESSAGE);
							quoteFactorCheck = false;
						}
						else if (Integer.parseInt(lots) == 0) {
							warningInfo = "报单手数至少为1手, 请重新选择有效的报单进行成交!";
							JOptionPane.showMessageDialog(null, warningInfo, "报单手数错误", JOptionPane.ERROR_MESSAGE);
							quoteFactorCheck = false;
						}
					}
				}
				else {
					warningInfo = "无法获取有效的报单手数, 请重新选择有效的报单进行成交!";
					JOptionPane.showMessageDialog(null, warningInfo, "报单手数错误", JOptionPane.ERROR_MESSAGE);
					quoteFactorCheck = false;
				}
				//check price
				if (price != null && price.length() > 0) {
					if (quoteFactorCheck) {
						String priceTickValue = ArithmeticUtil.removeDecimalPoint(priceTick.toString());
						priceTickValue = ArithmeticUtil.removeDecimalZero(priceTickValue);
						price = ArithmeticUtil.removeDecimalPoint(price);
						if (ArithmeticUtil.compare(price, maxPrice) || ArithmeticUtil.compare(minPrice, price)) {
							warningInfo =  "报单价格不能超过涨跌停板限制[" + minPrice + ", " + maxPrice + "], 请重新选择有效的报单进行成交!";
							JOptionPane.showMessageDialog(null, warningInfo, "报单价格错误", JOptionPane.ERROR_MESSAGE);
							quoteFactorCheck = false;
						}
						else if (ArithmeticUtil.remainderZero(price, priceTickValue) != 0) {
							warningInfo = "报单价格不满足最小报价单位(" + priceTickValue + ")限制, 请重新选择有效的报单进行成交!!";
							JOptionPane.showMessageDialog(null, warningInfo, "报单价格错误", JOptionPane.ERROR_MESSAGE);
							quoteFactorCheck = false;
						}
					}
				}
				else {
					warningInfo = "无法获取有效的报单价格, 请重新选择有效的报单进行成交!";
					JOptionPane.showMessageDialog(null, warningInfo, "报单价格错误",JOptionPane.ERROR_MESSAGE);
					quoteFactorCheck = false;
				}
				//check fee
				if (feeContent != null && feeContent.length() > 0) {
					if (quoteFactorCheck) {
						String cost = ArithmeticUtil.mul(lots, price).toString();
						String totalCost = ArithmeticUtil.add(cost, feeContent).toString();
						String feeAvailable = ArithmeticUtil.sub(fundAvailable, cost).toString();
						if (Integer.parseInt(openSide) == 0) {
							if (ArithmeticUtil.compareDetails(fundAvailable, totalCost) == -1) {
								warningInfo = "手续费不能超过(" + feeAvailable + "), 请重新输入!";
								JOptionPane.showMessageDialog(null, warningInfo, "手续费设置错误",JOptionPane.ERROR_MESSAGE);
								quoteFactorCheck = false;
							}
						}
						else if (Integer.parseInt(openSide) == 1 || Integer.parseInt(openSide) == 2) {
							logger.info("Release Risk");
						}
					}
				}
				else {
					warningInfo = "手续费最小可为0, 但不能为空, 请重新输入!";
					JOptionPane.showMessageDialog(null, warningInfo, "手续费设置错误",JOptionPane.ERROR_MESSAGE);
					quoteFactorCheck = false;
				}
				
				//check order_id
				if (quoteFactorCheck) {
					if (null != initialOrderID && !initialOrderID.equals("")) {
						//判断该订单是否已成交
						Object[] objects = {initialOrderID};
						List<Object[]> orderInfoList = quoteServiceOperation.query(objects, ConfigMessage.DEFAULT_DATABASE_TYPE);
						int queryResultCode = quoteServiceOperation.getOperationCode();
						if (null != orderInfoList && !orderInfoList.isEmpty()) {
							if (orderInfoList.size() == 1) {
								int orderStatus = (int) ((Object[]) orderInfoList.get(0))[0];
								if (orderStatus == 1) {
									quoteFactorCheck = false;
									operationResult = 2;
									warningInfo = "该订单已成交, 请重新选择有效的报单进行成交!";
									JOptionPane.showMessageDialog(null, warningInfo, "报单已成交",JOptionPane.ERROR_MESSAGE);
								}
								else if (orderStatus == 3) {
									quoteFactorCheck = false;
									operationResult = 2;
									warningInfo = "该订单已撤销, 请重新选择有效的报单进行成交!";
									JOptionPane.showMessageDialog(null, warningInfo, "报单已撤销",JOptionPane.ERROR_MESSAGE);
								}
								else if (orderStatus == 4) {
									quoteFactorCheck = false;
									operationResult = 2;
									warningInfo = "该订单已锁定, 请重新选择有效的报单进行成交!";
									JOptionPane.showMessageDialog(null, warningInfo, "报单已锁定",JOptionPane.ERROR_MESSAGE);
								}
							}
							else {
								quoteFactorCheck = false;
								operationResult = 2;
								warningInfo = "您选择的订单异常, 已提交监管机构核查, 请重新选择有效的报单进行成交!";
								JOptionPane.showMessageDialog(null, warningInfo, "报单异常",JOptionPane.ERROR_MESSAGE);
								logger.error("Get more than one order info in function<OrderDealConfirmJDialog:actionPerformed>, please check it!");
							}
						}
						else {
							quoteFactorCheck = false;
							if (queryResultCode == 1) {
								JOptionPane.showMessageDialog(null, "您选择的报单不存在, 请重新选择有效的报单进行成交!", "报单不存在", JOptionPane.ERROR_MESSAGE);
							}
							else if (queryResultCode == 11) {
								JOptionPane.showMessageDialog(null, "数据查询异常，请确认网络及系统配置信息是否正确!", "数据查询异常", JOptionPane.ERROR_MESSAGE);
							}
							else {
								JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					else {
						warningInfo = "无法获取有效的订单编号, 请重新选择有效的报单进行成交!";
						JOptionPane.showMessageDialog(null, warningInfo, "报单编号错误",JOptionPane.ERROR_MESSAGE);
						quoteFactorCheck = false;
					}
				}
				
				if (quoteFactorCheck) {
					lots = ArithmeticUtil.removeDecimalZero(lots);
					price = ArithmeticUtil.removeDecimalZero(price);
					feeContent = ArithmeticUtil.removeDecimalZero(feeContent);
					String openFlagButtonContext = "开仓";
					if (Integer.parseInt(openSide) == 1) {
						openFlagButtonContext = "平昨";
					}
					else if (Integer.parseInt(openSide) == 2) {
						openFlagButtonContext = "平今";
					}
					
					String orderInfo = "确定以下列报单信息进行成交:\n" + 
							instrumentID + ", " + displayTradeSide + ", " + openFlagButtonContext + ", " + lots + "手@" + price + "元\n" + 
									"支付手续费:" + feeContent + "元\n";
					Object[] operation ={ "确定", "取消" };
					operationResult = JOptionPane.showOptionDialog(null, orderInfo, "成交确认",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, operation, operation[0]);
//					System.out.println("temp operationResult = " + operationResult);
					if (operationResult == 0) {
						String userSystemID = userBaseBean.getUser_system_id();
						//生成报单编号
						String orderStatus = "4";
						String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
						StringBuilder stringBuilder = new StringBuilder().append(tradeSide).append(openSide).append(price).append(lots).append(instrumentID).append(orderStatus).append(currentUTCTimeStamp).append(userBaseBean.getUser_system_id()).append(feeContent);
						String orderID = HashModule.sha256(stringBuilder.toString(), true);
						//更新原报单状态为处理中
						Object[] initialOrderUpdate = {"2", initialOrderID};
						boolean initialOrderUpdateResult = quoteServiceOperation.updateOrderInfo(initialOrderUpdate, ConfigMessage.DEFAULT_DATABASE_TYPE);
						int initialOrderUpdateResultCode = quoteServiceOperation.getOperationCode();
						if (initialOrderUpdateResult && (null != userSystemID && !userSystemID.equals(""))) {
							//插入一笔新报单状态为锁定
							Object[] orderInsert = {orderID, tradeSide, openSide, price, lots, instrumentID, orderStatus, currentUTCTimeStamp, userBaseBean.getUser_system_id(), feeContent};
							boolean orderInsertResult = quoteServiceOperation.newQuote(orderInsert, ConfigMessage.DEFAULT_DATABASE_TYPE);
							int orderInsertResultCode = quoteServiceOperation.getOperationCode();
							if (orderInsertResult) {
								orderConfirmInfo = new OrderBaseBean(orderID, tradeSide, new BigDecimal(price), new BigDecimal(lots), instrumentID, orderStatus, currentUTCTimeStamp, new BigDecimal(feeContent));
								//通过P2P网络向外扩散该交易
								boolean sendOrderInfo = true;
								if (sendOrderInfo) {
									operationResult = 0;
									boolean x = false;
								}
								else {
									operationResult = 2;
								}
							}
							else {
								if (orderInsertResultCode == 2) {
									JOptionPane.showMessageDialog(null, "报单发送失败, 请确认网络及系统配置信息是否正确!", "报单发送失败", JOptionPane.ERROR_MESSAGE);
								}
								else if (orderInsertResultCode == 11) {
									JOptionPane.showMessageDialog(null, "数据库操作异常, 请升级软件至最新版本!", "数据库操作异常", JOptionPane.ERROR_MESSAGE);
								}
								else {
									JOptionPane.showMessageDialog(null, "未知错误, 请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
						else {
							if (initialOrderUpdateResultCode == 3) {
								JOptionPane.showMessageDialog(null, "报单状态更新失败, 请确认网络及系统配置信息是否正确!", "报单状态更新失败", JOptionPane.ERROR_MESSAGE);
							}
							else if (initialOrderUpdateResultCode == 11) {
								JOptionPane.showMessageDialog(null, "数据库操作异常, 请升级软件至最新版本!", "数据库操作异常", JOptionPane.ERROR_MESSAGE);
							}
							else {
								JOptionPane.showMessageDialog(null, "未知错误, 请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
				//call window event
				closeCurrentWindow();
			}
		});
		okButton.setBounds(110, 245, 60, 24);
		okButton.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(okButton);
		
		JButton cancelButton = new JButton("取消");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				operationResult = 3;
				closeCurrentWindow();
			}
		});
		cancelButton.setBounds(210, 245, 60, 24);
		cancelButton.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(cancelButton);
	}
}
