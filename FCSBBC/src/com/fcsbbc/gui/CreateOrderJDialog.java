/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * @author luo.changshu
 *
 */
public class CreateOrderJDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(CreateOrderJDialog.class.getName());
	private static final int feeSetp = 10;
	private final JPanel contentPanel = new JPanel();
//	private final JPanel contentPanel;
	private JTextField fundAvailableTextField;
	private JTextField quantityTextField;
	private JTextField maxQuantityTextField;
	private JTextField priceTextField;
	private JTextField maxPriceTextField;
	private JTextField feeTextField;
	private JTextField minPriceTextField;
	private JButton tradeSideBuyButton;
	private JButton tradeSideSellButton;
	private JButton openFlagOpenButton;
	private JButton openFlagCloseYButton;
	private JButton openFlagCloseTButton;
	private JButton quoteOrderButton;
	private String quoteOrderButtonContext;
	private String tradeSideButtonContext = "买";
	private String openFlagButtonContext = "开仓";
	private JComboBox<Object> instrumentComboBox;
	private String latestPrice, maxPrice, minPrice, settlementPrice;
	private int maxLots;
	private BigDecimal priceTick;
	private Object[] instrumentIDArray;
	private String instrumentSelected, instrumentEntry;
	public int operationResult = -2;
	private static final int actualWidth = 450, actualHeight = 360;
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	QuoteJPanel quoteJPanel;
	UserBaseBean userBaseBean;
	String fundAvailable;
	
	public int orderCreatedResult() {
		return operationResult;
	}
	
	public void closeCurrentWindow() {
		int operationResult = orderCreatedResult();
		if (operationResult == 2) {
			this.dispose();
		}
		else if (operationResult == 1) {
			System.out.println("User Choice Cancel Button");
		}
		else if (operationResult == -1) {
			System.out.println("User Choice Close Button");
		}
	}
	
//	public void initialMainPanel() {
////		contentPane = new JPanel();
////		contentPane.setLayout(null);
////		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
//		BaseDataServiceOperation baseDataServiceOperation = new BaseDataServiceOperation();
//        instrumentIDArray = baseDataServiceOperation.getInstrumentIDArray();	//get from database
//	}

	/**
	 * Create the dialog.
	 */
	public CreateOrderJDialog(UserBaseBean userBaseBean, QuoteJPanel quoteJPanel, String fundAvailable) {
		this.userBaseBean = userBaseBean;
		this.quoteJPanel = quoteJPanel;
		this.fundAvailable = fundAvailable;
//		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		this.setTitle("新建报单");
		if (width > actualWidth && height > actualHeight) {
			this.setBounds((width - actualWidth) / 2, (height - actualHeight) / 2, actualWidth, actualHeight);
		}
		else {
			this.setBounds(0, 0, width, height);
		}
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		BaseDataServiceOperation baseDataServiceOperation = new BaseDataServiceOperation();
        instrumentIDArray = baseDataServiceOperation.getInstrumentIDArray();	//get from database
        String fundAvailableFormat = ArithmeticUtil.getFormat(fundAvailable);
		
		JLabel fundAvailableLabel = new JLabel("可用资金：");
		fundAvailableLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		fundAvailableLabel.setBounds(100, 13, 68, 15);
		fundAvailableLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(fundAvailableLabel);
		
//		fundAvailableTextField = new JTextField();
		fundAvailableTextField = new JTextField(fundAvailableFormat);
		fundAvailableTextField.setColumns(19);
		fundAvailableTextField.setOpaque(false);
		fundAvailableTextField.setEditable(false);
		fundAvailableTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		fundAvailableTextField.setBounds(170, 10, 130, 21);
		fundAvailableTextField.setFont(UIFont.DEFAULT_FONT);
		fundAvailableTextField.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(fundAvailableTextField);
		
		JLabel instrumentLabel = new JLabel("合约");
		instrumentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instrumentLabel.setBounds(60, 53, 54, 15);
		instrumentLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(instrumentLabel);
		
		JLabel tradeSideLabel = new JLabel("方向");
		tradeSideLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tradeSideLabel.setBounds(60, 86, 54, 15);
		tradeSideLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(tradeSideLabel);
		
		tradeSideBuyButton = new JButton("买");
		tradeSideBuyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tradeSideButtonContext = "买";
				tradeSideBuyButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				tradeSideSellButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				String maxPriceContent = maxPriceTextField.getText().trim();
				if (maxPriceContent != null && maxPriceContent.length() > 0) {
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
					quoteOrderButton.setEnabled(true);
				}
				else {
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
					quoteOrderButton.setEnabled(false);
				}
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		tradeSideBuyButton.setBounds(110, 80, 61, 23);
		tradeSideBuyButton.setFont(UIFont.DEFAULT_FONT);
		tradeSideBuyButton.setFocusPainted(false);
		tradeSideBuyButton.setForeground(Color.black);
		tradeSideBuyButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		contentPanel.add(tradeSideBuyButton);
		
		tradeSideSellButton = new JButton("卖");
		tradeSideSellButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tradeSideButtonContext = "卖";
				tradeSideSellButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				tradeSideBuyButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				String maxPriceContent = maxPriceTextField.getText().trim();
				if (maxPriceContent != null && maxPriceContent.length() > 0) {
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
					quoteOrderButton.setEnabled(true);
				}
				else {
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
					quoteOrderButton.setEnabled(false);
				}
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		tradeSideSellButton.setBounds(170, 80, 60, 23);
		tradeSideSellButton.setFont(UIFont.DEFAULT_FONT);
		tradeSideSellButton.setFocusPainted(false);
		tradeSideSellButton.setForeground(Color.black);
		tradeSideSellButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPanel.add(tradeSideSellButton);
		
		JLabel openFlagLabel = new JLabel("开平");
		openFlagLabel.setHorizontalAlignment(SwingConstants.CENTER);
		openFlagLabel.setBounds(60, 116, 54, 15);
		openFlagLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(openFlagLabel);
		
		openFlagOpenButton = new JButton("开仓");
		openFlagOpenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagButtonContext = "开仓";
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		openFlagOpenButton.setBounds(110, 110, 61, 23);
		openFlagOpenButton.setFont(UIFont.DEFAULT_FONT);
		openFlagOpenButton.setFocusPainted(false);
		openFlagOpenButton.setForeground(Color.black);
		openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		contentPanel.add(openFlagOpenButton);
		
		openFlagCloseYButton = new JButton("平仓");
		openFlagCloseYButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagButtonContext = "平仓";
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		openFlagCloseYButton.setBounds(170, 110, 61, 23);
		openFlagCloseYButton.setFont(UIFont.DEFAULT_FONT);
		openFlagCloseYButton.setFocusPainted(false);
		openFlagCloseYButton.setForeground(Color.black);
		openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPanel.add(openFlagCloseYButton);
		
		openFlagCloseTButton = new JButton("平今");
		openFlagCloseTButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagButtonContext = "平今";
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		openFlagCloseTButton.setBounds(230, 110, 61, 23);
		openFlagCloseTButton.setFont(UIFont.DEFAULT_FONT);
		openFlagCloseTButton.setFocusPainted(false);
		openFlagCloseTButton.setForeground(Color.black);
		openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPanel.add(openFlagCloseTButton);
		
		JLabel quantityLabel = new JLabel("手数");
		quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		quantityLabel.setBounds(60, 146, 54, 15);
		quantityLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(quantityLabel);
		
		quantityTextField = new JTextField();
		quantityTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				String currentContext = quantityTextField.getText().trim();
				int keyValue = arg0.getKeyChar();
				//key entry
				if (keyValue >= KeyEvent.VK_0 && keyValue <= KeyEvent.VK_9) {
					
				}
				else if (arg0.getKeyChar() == KeyEvent.VK_ENTER || arg0.getKeyChar() == KeyEvent.VK_DELETE || arg0.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					if (currentContext == null || currentContext.length() == 0) {
						quantityTextField.setText("1");
					}
				}
				else {
					arg0.consume();
				}
				//entry content
				if (currentContext != null && currentContext.length() > 0) {
					if (maxLots > 0) {
						if (Integer.parseInt(currentContext) > maxLots) {
							arg0.consume();
							quantityTextField.setText(String.valueOf(maxLots));
						}
						if (currentContext.length() >= 7) {
							quantityTextField.setText(String.valueOf(new Double((Double.parseDouble(currentContext))).intValue()));
							arg0.consume();
						}
					}
					else if (currentContext.length() >= 7) {
						arg0.consume();
					}
				}
			}
		});
		quantityTextField.setText("1");
		quantityTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		quantityTextField.setBounds(110, 143, 85, 21);
		quantityTextField.setColumns(10);
		quantityTextField.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(quantityTextField);
		
		ImageIcon subIcon = new ImageIcon("image/sub_button.png");
		ImageIcon addIcon = new ImageIcon("image/add_button.png");
//		JButton quantitySubButton = new JButton("");
		JButton quantitySubButton = new JButton(subIcon);
		quantitySubButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String quantity = quantityTextField.getText().trim();
				if (quantity !=null && quantity.length() > 0) {
					int currentQuantity = Integer.parseInt(quantity);
					if (currentQuantity == 1) {
						quantityTextField.setText("1");
					}
					else if (currentQuantity > 1) {
						currentQuantity -= 1;
						quantityTextField.setText(String.valueOf(currentQuantity));
					}
				}
				else {
					quantityTextField.setText("1");
				}
			}
		});
		quantitySubButton.setBounds(194, 143, 19, 20);
		quantitySubButton.setFocusPainted(false);
		quantitySubButton.setPreferredSize(new Dimension(subIcon.getIconWidth(), subIcon.getIconHeight()));
		contentPanel.add(quantitySubButton);
		
//		JButton quantityAddButton = new JButton("");
		JButton quantityAddButton = new JButton(addIcon);
		quantityAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String quantity = quantityTextField.getText().trim();
				String maxQuantityString = maxQuantityTextField.getText().trim();
				int maxQuantityInt = 0;
				if (maxQuantityString != null && maxQuantityString.length() > 0) {
					maxQuantityInt = Integer.parseInt(maxQuantityString);
				}
				
				if (quantity !=null && quantity.length() > 0) {
					int currentQuantity = Integer.parseInt(quantity);
					if (currentQuantity < maxQuantityInt) {
						currentQuantity += 1;
						quantityTextField.setText(String.valueOf(currentQuantity));
					}
				}
				else {
					quantityTextField.setText("1");
				}
			}
		});
		quantityAddButton.setBounds(212, 143, 19, 20);
		quantityAddButton.setFocusPainted(false);
		quantityAddButton.setPreferredSize(new Dimension(subIcon.getIconWidth(), subIcon.getIconHeight()));
		contentPanel.add(quantityAddButton);
		
		JLabel maxQuantityLabel = new JLabel("≤");
		maxQuantityLabel.setVisible(false);
		maxQuantityLabel.setBounds(260, 146, 31, 15);
		maxQuantityLabel.setFont(UIFont.DEFAULT_FONT);
		maxQuantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(maxQuantityLabel);
		
		maxQuantityTextField = new JTextField();
		maxQuantityTextField.setColumns(10);
		maxQuantityTextField.setEditable(false);
		maxQuantityTextField.setOpaque(false);
		maxQuantityTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		maxQuantityTextField.setFont(UIFont.DEFAULT_FONT);
		maxQuantityTextField.setHorizontalAlignment(SwingConstants.LEFT);
		maxQuantityTextField.setBounds(305, 143, 66, 21);
		contentPanel.add(maxQuantityTextField);
		
		
		JLabel priceLabel = new JLabel("价格");
		priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		priceLabel.setBounds(60, 178, 54, 15);
		priceLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(priceLabel);
		
		priceTextField = new JTextField("0");
		priceTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				String currentContext = priceTextField.getText().trim();
				char keyChar = arg0.getKeyChar();
				if ((keyChar == '.') || (keyChar >= '0' && keyChar <= '9')) {
					
				}
				else if (arg0.getKeyChar() == KeyEvent.VK_ENTER || arg0.getKeyChar() == KeyEvent.VK_DELETE || arg0.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					if (currentContext == null || currentContext.length() == 0) {
						priceTextField.setText("0");
					}
				}
				else {
					arg0.consume();
				}
				if (currentContext != null && currentContext.length() >= 10) {
					arg0.consume();
				}
				String maxPriceContent = maxPriceTextField.getText().trim();
				String minPriceContent = minPriceTextField.getText().trim();
				if ((maxPriceContent != null && maxPriceContent.length() > 0) && (minPriceContent != null && minPriceContent.length() > 0)) {
					if (currentContext != null && currentContext.length() > 0) {
						currentContext = ArithmeticUtil.removeDecimalPoint(currentContext);
						currentContext = ArithmeticUtil.removeDecimalZero(currentContext);
						if (ArithmeticUtil.compare(maxPriceContent, currentContext)) {
							
						}
						else if (ArithmeticUtil.compare(currentContext, minPriceContent)) {
							
						}
						else {
							arg0.consume();
						}
					}
				}
			}
		});
		priceTextField.setColumns(10);
		priceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		priceTextField.setBounds(110, 175, 85, 21);
		priceTextField.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(priceTextField);
		
//		JButton priceSubButton = new JButton("");
		JButton priceSubButton = new JButton(subIcon);
		priceSubButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String currentContext = priceTextField.getText().trim();
				String minPriceContent = minPriceTextField.getText().trim();
				if ((minPriceContent != null && minPriceContent.length() > 0) && priceTick != null) {
					String maxPriceContent = maxPriceTextField.getText().trim();
					String minPriceLimited = ArithmeticUtil.add(minPriceContent, priceTick.toString()).toString();
					String priceTickValue = ArithmeticUtil.removeDecimalPoint(priceTick.toString());
					priceTickValue = ArithmeticUtil.removeDecimalZero(priceTickValue);
					int decimalLength = ArithmeticUtil.getNumericLength(priceTickValue);
					if (currentContext != null && currentContext.length() > 0) {
						currentContext = ArithmeticUtil.removeDecimalPoint(currentContext);
						currentContext = ArithmeticUtil.removeDecimalZero(currentContext);
						
						if ((maxPriceContent != null && maxPriceContent.length() > 0) && ArithmeticUtil.compare(currentContext, maxPriceContent)) {
							priceTextField.setText(maxPriceContent);
						}
						else if (ArithmeticUtil.compare(currentContext, minPriceContent)) {
							if (ArithmeticUtil.compareDetails(currentContext, minPriceLimited) >= 0) {
								//remainder is zero
								if (ArithmeticUtil.remainderZero(currentContext, priceTick.toString()) == 0) {
									currentContext = ArithmeticUtil.sub(currentContext, priceTick.toString(), decimalLength);
								}
								//remainder is not zero
								else if (ArithmeticUtil.remainderZero(currentContext, priceTick.toString()) == 1) {
									BigDecimal bigDecimal = ArithmeticUtil.getQuotient(currentContext, priceTick.toString());
									currentContext = ArithmeticUtil.mul(bigDecimal.toString(), priceTick.toString(), decimalLength);
								}
								priceTextField.setText(currentContext);
							}
							else if (ArithmeticUtil.compareDetails(currentContext, minPriceLimited) == -1) {
								priceTextField.setText(minPriceContent);
							}
						}
					}
					else {
						priceTextField.setText("0");
					}
				}
			}
		});
		priceSubButton.setBounds(194, 175, 19, 20);
		priceSubButton.setFocusPainted(false);
		priceSubButton.setPreferredSize(new Dimension(subIcon.getIconWidth(), subIcon.getIconHeight()));
		contentPanel.add(priceSubButton);
		
//		JButton priceAddButton = new JButton("");
		JButton priceAddButton = new JButton(addIcon);
		priceAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String currentContext = priceTextField.getText().trim();
				String maxPriceContent = maxPriceTextField.getText().trim();
				if ((maxPriceContent != null && maxPriceContent.length() > 0) && priceTick != null) {
					String minPriceContent = minPriceTextField.getText().trim();
					String maxPriceLimited = ArithmeticUtil.sub(maxPriceContent, priceTick.toString()).toString();
					String priceTickValue = ArithmeticUtil.removeDecimalPoint(priceTick.toString());
					priceTickValue = ArithmeticUtil.removeDecimalZero(priceTickValue);
					int decimalLength = ArithmeticUtil.getNumericLength(priceTickValue);
					if (currentContext != null && currentContext.length() > 0) {
						currentContext = ArithmeticUtil.removeDecimalPoint(currentContext);
						currentContext = ArithmeticUtil.removeDecimalZero(currentContext);
						
						if ((minPriceContent != null && minPriceContent.length() > 0) && ArithmeticUtil.compare(minPriceContent, currentContext)) {
							priceTextField.setText(minPriceContent);
						}
						else if (ArithmeticUtil.compare(maxPriceContent, currentContext)) {
							if (ArithmeticUtil.compareDetails(maxPriceLimited, currentContext) >= 0) {
								if (ArithmeticUtil.remainderZero(currentContext, priceTick.toString()) == 0) {
									currentContext = ArithmeticUtil.add(currentContext, priceTick.toString(), decimalLength);
								}
								else if (ArithmeticUtil.remainderZero(currentContext, priceTick.toString()) == 1) {
									BigDecimal bigDecimal = ArithmeticUtil.getQuotient(currentContext, priceTick.toString());
									bigDecimal = ArithmeticUtil.add(bigDecimal.toString(), "1");
									currentContext = ArithmeticUtil.mul(bigDecimal.toString(), priceTick.toString(), decimalLength);
								}
								priceTextField.setText(currentContext);
							}
							else if (ArithmeticUtil.compareDetails(maxPriceLimited, currentContext) == -1) {
								priceTextField.setText(maxPriceContent);
							}
						}
					}
					else {
						priceTextField.setText("0");
					}
				}
			}
		});
		priceAddButton.setBounds(212, 175, 19, 20);
		priceAddButton.setFocusPainted(false);
		priceAddButton.setPreferredSize(new Dimension(addIcon.getIconWidth(), addIcon.getIconHeight()));
		contentPanel.add(priceAddButton);
		
		JLabel maxPriceLabel = new JLabel("涨板");
		maxPriceLabel.setForeground(Color.RED);
		maxPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		maxPriceLabel.setBounds(260, 178, 36, 15);
		maxPriceLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(maxPriceLabel);
		
		maxPriceTextField = new JTextField();
		maxPriceTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					String maxPriceContent = maxPriceTextField.getText().trim();
					if (maxPriceContent != null && maxPriceContent.length() > 0) {
						priceTextField.setText(maxPriceContent);
					}
				}
				super.mousePressed(e);
			}
		});
		maxPriceTextField.setColumns(10);
		maxPriceTextField.setHorizontalAlignment(SwingConstants.LEFT);
		maxPriceTextField.setFont(UIFont.DEFAULT_FONT);
		maxPriceTextField.setEditable(false);
		maxPriceTextField.setOpaque(false);
		maxPriceTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		maxPriceTextField.setBounds(305, 175, 66, 21);
		contentPanel.add(maxPriceTextField);
		
		JLabel feeLabel = new JLabel("手续费");
		feeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		feeLabel.setBounds(60, 210, 54, 15);
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
		feeTextField.setColumns(10);
		feeTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		feeTextField.setFont(UIFont.DEFAULT_FONT);
		feeTextField.setBounds(110, 207, 85, 21);
		contentPanel.add(feeTextField);
		
//		JButton feeSubButton = new JButton("");
		JButton feeSubButton = new JButton(subIcon);
		feeSubButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentContent = feeTextField.getText().trim();
				String lots = quantityTextField.getText().trim();
				String price = priceTextField.getText().trim();
				String minPriceContent = minPriceTextField.getText().trim();
				if (minPriceContent != null && minPriceContent.length() > 0) {
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
			}
		});
		feeSubButton.setBounds(194, 207, 19, 20);
		feeSubButton.setFocusPainted(false);
		feeSubButton.setPreferredSize(new Dimension(subIcon.getIconWidth(), subIcon.getIconHeight()));
		contentPanel.add(feeSubButton);
		
//		JButton feeAddButton = new JButton("");
		JButton feeAddButton = new JButton(addIcon);
		feeAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentContent = feeTextField.getText().trim();
				String lots = quantityTextField.getText().trim();
				String price = priceTextField.getText().trim();
				String minPriceContent = minPriceTextField.getText().trim();
				if (minPriceContent != null && minPriceContent.length() > 0) {
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
			}
		});
		feeAddButton.setBounds(212, 207, 19, 20);
		feeAddButton.setFocusPainted(false);
		feeAddButton.setPreferredSize(new Dimension(addIcon.getIconWidth(), addIcon.getIconHeight()));
		contentPanel.add(feeAddButton);
		
		JLabel minPriceLabel = new JLabel("跌板");
		minPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		minPriceLabel.setBounds(260, 210, 36, 15);
		minPriceLabel.setFont(UIFont.DEFAULT_FONT);
		minPriceLabel.setForeground(new Color(50, 205, 50));
		contentPanel.add(minPriceLabel);
		
		minPriceTextField = new JTextField();
		minPriceTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON1) {
					String minPriceContent = minPriceTextField.getText().trim();
					if (minPriceContent != null && minPriceContent.length() > 0) {
						priceTextField.setText(minPriceContent);
					}
				}
				super.mousePressed(arg0);
			}
		});
		minPriceTextField.setColumns(10);
		minPriceTextField.setBounds(305, 207, 66, 21);
		minPriceTextField.setFont(UIFont.DEFAULT_FONT);
		minPriceTextField.setEditable(false);
		minPriceTextField.setOpaque(false);
		minPriceTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		minPriceTextField.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(minPriceTextField);
		
//		JButton quoteOrderButton = new JButton("下单(买 开仓)");
		quoteOrderButtonContext = "下单(" + tradeSideButtonContext + " " + openFlagButtonContext + ")";
		quoteOrderButton = new JButton(quoteOrderButtonContext);
		quoteOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String instrumentID = "";
				if (instrumentEntry != null && instrumentEntry.length() > 0) {
					instrumentID = instrumentEntry;
				}
				String lots = quantityTextField.getText().trim();
				String price = priceTextField.getText().trim();
				String maxLotsContent = maxQuantityTextField.getText().trim();
				String minPriceContent = minPriceTextField.getText().trim();
				String maxPriceContent = maxPriceTextField.getText().trim();
				String feeContent = feeTextField.getText().trim();
				boolean quoteFactorCheck = true;
				if ((maxLotsContent != null && maxLotsContent.length() > 0) && 
						(minPriceContent != null && minPriceContent.length() > 0) && 
						(maxPriceContent != null && maxPriceContent.length() > 0) && 
						(priceTick != null && priceTick.toString().length() > 0)) {
					//check instrumentID
					if (instrumentID != null && instrumentID.length() > 0) {
						if (!SearchUtil.loopSearch(instrumentIDArray, instrumentID)) {
							JOptionPane.showMessageDialog(null, "您选择或输入的期货合约不存在, 请选择或重新输入!", "报单合约错误", JOptionPane.ERROR_MESSAGE);
							quoteFactorCheck = false;
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "期货合约不能为空, 请选择或重新输入!", "报单合约错误", JOptionPane.ERROR_MESSAGE);
						quoteFactorCheck = false;
					}
					//check lots
					if (lots != null && lots.length() > 0) {
						if (quoteFactorCheck) {
							if (lots.indexOf(".") >= 0) {
								JOptionPane.showMessageDialog(null, "报单手数只能输入正整数, 请重新输入!", "报单手数错误", JOptionPane.ERROR_MESSAGE);
								quoteFactorCheck = false;
							}
							if (Integer.parseInt(maxLotsContent) == 0) {
								JOptionPane.showMessageDialog(null, "您的账户资金不足, 无法继续对合约(" + instrumentID + ")报单!", "账户资金不足", JOptionPane.ERROR_MESSAGE);
								quoteFactorCheck = false;
							}
							else if (Integer.parseInt(maxLotsContent) > 0) {
								if (ArithmeticUtil.compare("1", lots) || ArithmeticUtil.compare(lots, maxLotsContent)) {
									JOptionPane.showMessageDialog(null, "报单手数应在[1, " + maxLotsContent + "]之间, 请重新输入!", "报单手数错误", JOptionPane.ERROR_MESSAGE);
									quoteFactorCheck = false;
								}
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "报单手数不能为空, 请重新输入!", "报单手数错误", JOptionPane.ERROR_MESSAGE);
						quoteFactorCheck = false;
					}
					//check price
					if (price != null && price.length() > 0) {
						if (quoteFactorCheck) {
							String priceTickValue = ArithmeticUtil.removeDecimalPoint(priceTick.toString());
							priceTickValue = ArithmeticUtil.removeDecimalZero(priceTickValue);
							price = ArithmeticUtil.removeDecimalPoint(price);
							String warningInfo = "";
							if (ArithmeticUtil.compare(price, maxPriceContent) || ArithmeticUtil.compare(minPriceContent, price)) {
								warningInfo =  "报单价格不能超过涨跌停板限制[" + minPriceContent + ", " + maxPriceContent + "], 请重新输入!";
								JOptionPane.showMessageDialog(null, warningInfo, "报单价格错误", JOptionPane.ERROR_MESSAGE);
								quoteFactorCheck = false;
							}
							else if (ArithmeticUtil.remainderZero(price, priceTickValue) != 0) {
								warningInfo = "报单价格不满足最小报价单位(" + priceTickValue + ")限制, 请重新输入!";
								JOptionPane.showMessageDialog(null, warningInfo, "报单价格错误", JOptionPane.ERROR_MESSAGE);
								quoteFactorCheck = false;
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "报单价格不能为空, 请重新输入!", "报单价格错误",JOptionPane.ERROR_MESSAGE);
						quoteFactorCheck = false;
					}
					//check fee
					if (feeContent != null && feeContent.length() > 0) {
						if (quoteFactorCheck) {
							String cost = ArithmeticUtil.mul(lots, price).toString();
							String totalCost = ArithmeticUtil.add(cost, feeContent).toString();
							String feeAvailable = ArithmeticUtil.sub(fundAvailable, cost).toString();
							if (ArithmeticUtil.compareDetails(fundAvailable, totalCost) == -1) {
								JOptionPane.showMessageDialog(null, "手续费不能超过(" + feeAvailable + "), 请重新输入!", "手续费设置错误",JOptionPane.ERROR_MESSAGE);
								quoteFactorCheck = false;
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "手续费不能为空, 请重新输入!", "手续费设置错误",JOptionPane.ERROR_MESSAGE);
						quoteFactorCheck = false;
					}
				}
				if (quoteFactorCheck && 
						(maxLotsContent != null && maxLotsContent.length() > 0) && 
						(minPriceContent != null && minPriceContent.length() > 0)) {
					int insertResultCode = -1;
					lots = ArithmeticUtil.removeDecimalZero(lots);
					price = ArithmeticUtil.removeDecimalZero(price);
					feeContent = ArithmeticUtil.removeDecimalZero(feeContent);
					String orderInfo = "确定向市场发出该报单请求:\n" + 
					instrumentID + ", " + tradeSideButtonContext + ", " + openFlagButtonContext + ", " + lots + "手@" + price + "元\n" + 
							"支付手续费:" + feeContent + "元\n";
					Object[] operation ={"确定", "取消"};
					operationResult = JOptionPane.showOptionDialog(null, orderInfo, "报单确认",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, operation, operation[0]);
					boolean insertResult = false;
					if (operationResult == 0) {
						//	get user system id
						String userSystemID = userBaseBean.getUser_system_id();
						//	generate tradeSide
						String tradeSide = ("买".equals(tradeSideButtonContext)) ? "0" : "1";
						//	generate openSide
						String openSide = "-1";
						if ("开仓".equals(openFlagButtonContext)) {
							openSide = "0";
						}
						else if ("平仓".equals(openFlagButtonContext)) {
							openSide = "1";
						}
						else if ("平今".equals(openFlagButtonContext)) {
							openSide = "2";
						}
						//	default order_status
						String orderStatus = "0";
						//	generate timeStamp
						String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
						//generate order_id
						StringBuilder stringBuilder=new StringBuilder().append(tradeSide).append(openSide).append(price).append(lots).append(instrumentID).append(orderStatus).append(currentUTCTimeStamp).append(userSystemID).append(feeContent);
						String orderID = HashModule.sha256(stringBuilder.toString(), true);
						//insert order info into database
						QuoteServiceOperation quoteServiceOperation = new QuoteServiceOperation();
						Object[] quoteParametersArray = {orderID, tradeSide, openSide, price, lots, instrumentID, "0", currentUTCTimeStamp, userSystemID, feeContent};
						insertResult = quoteServiceOperation.newQuote(quoteParametersArray, ConfigMessage.DEFAULT_DATABASE_TYPE);
						insertResultCode = quoteServiceOperation.getOperationCode();
					}
					if (insertResult) {
						operationResult = 2;
						logger.info("Order insert success!");
						//通过P2P网络向外扩散该交易
						boolean sendOrderInfo = true;
					}
					else {
						if (insertResultCode == 2) {
							JOptionPane.showMessageDialog(null, "数据插入失败，请确认订单信息的有效性!", "数据插入失败", JOptionPane.ERROR_MESSAGE);
						}
						else if (insertResultCode == 11) {
							JOptionPane.showMessageDialog(null, "数据插入异常，请确认网络及系统配置信息是否正确!", "数据插入异常", JOptionPane.ERROR_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
						}
					}
					//call window event
					closeCurrentWindow();
				}
				else if (!(maxLotsContent != null && maxLotsContent.length() > 0)) {
//					quoteOrderButton.setBackground(Color.gray);
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
					quoteOrderButton.setEnabled(false);
				}
				else {
					logger.info("Special Condition--[" + instrumentComboBox.getEditor().getItem().toString().trim().toLowerCase() + "]");
				}
			}
		});
		quoteOrderButton.setFont(UIFont.DEFAULT_FONT);
		quoteOrderButton.setFocusPainted(false);
		quoteOrderButton.setEnabled(false);
		quoteOrderButton.setBounds(75, 241, 120, 25);
		quoteOrderButton.setForeground(Color.black);
		quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPanel.add(quoteOrderButton);
		
		JButton resetButton = new JButton("复位");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//reset quantity with 1
				quantityTextField.setText("1");
				//reset fee with 0
				feeTextField.setText("0");
				//reset price with lastPrice, settlementPrice, null
				String instrumentID = (instrumentEntry != null && instrumentEntry.length() > 0) ? instrumentEntry : instrumentSelected;
				String maxPriceContent = maxPriceTextField.getText().trim();
				if (SearchUtil.loopSearch(instrumentIDArray, instrumentID) && 
						(maxPriceContent != null && maxPriceContent.length() > 0 && 
						ArithmeticUtil.compare(maxPriceContent, "0"))) {
					List<Object[]> base_properties = baseDataServiceOperation.getInstrumentProperty(instrumentID);
					if (base_properties != null && base_properties.size() == 1) {
						settlementPrice = ((Object[])base_properties.get(0))[3].toString();
					}
					if (latestPrice != null && ArithmeticUtil.compare(latestPrice, "0")) {
						priceTextField.setText(latestPrice);
					}
					else {
						if (settlementPrice != null && ArithmeticUtil.compare(settlementPrice, "0")) {
							priceTextField.setText(settlementPrice);
						}
						else {
							priceTextField.setText("0");
						}
					}
				}
				else {
					priceTextField.setText("0");
				}
			}
		});
		resetButton.setBounds(200, 242, 60, 23);
		resetButton.setBackground(UIColor.DEFAULT_COLOR);
		resetButton.setFont(UIFont.DEFAULT_FONT);
		resetButton.setFocusPainted(false);
		resetButton.setForeground(Color.black);
		contentPanel.add(resetButton);
		
		instrumentComboBox = new JComboBox<>(instrumentIDArray);
		JTextField component = (JTextField) instrumentComboBox.getEditor().getEditorComponent();
		instrumentComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					instrumentSelected = arg0.getItem().toString();
					if ((instrumentSelected != null && instrumentSelected.length() > 0)) {
						if (SearchUtil.loopSearch(instrumentIDArray, instrumentSelected)) {
							instrumentEntry = instrumentSelected;
							if (quoteOrderButtonContext.contains("买")) {
								quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
							}
							else if (quoteOrderButtonContext.contains("卖")) {
								quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
							}
							List<Object[]> base_properties = baseDataServiceOperation.getInstrumentProperty(instrumentSelected);
							//	get right instrument
							if (base_properties != null && base_properties.size() == 1) {
								maxPrice = ((Object[])base_properties.get(0))[0].toString();
								minPrice = ((Object[])base_properties.get(0))[1].toString();
								priceTick = new BigDecimal(((Object[])base_properties.get(0))[2].toString());
								settlementPrice = ((Object[])base_properties.get(0))[3].toString();
								if ((maxPrice != null && ArithmeticUtil.compareDetails(maxPrice, "0") > 0) && 
										(minPrice != null && ArithmeticUtil.compareDetails(minPrice, "0") > 0) && 
										(priceTick != null && ArithmeticUtil.compare(priceTick.toString(), "0")) && 
										(settlementPrice != null && ArithmeticUtil.compareDetails(settlementPrice, "0") > 0)) {
									maxPriceTextField.setText(maxPrice);
									minPriceTextField.setText(minPrice);
									if (latestPrice != null && ArithmeticUtil.compareDetails(latestPrice, "0") > 0) {
										priceTextField.setText(latestPrice);
										quantityTextField.setText("1");
										feeTextField.setText("0");
									}
									else {
										priceTextField.setText(settlementPrice);
										quantityTextField.setText("1");
										feeTextField.setText("0");
									}
									if (quoteOrderButtonContext.contains("买")) {
										maxLots = ArithmeticUtil.getInteger(fundAvailable, maxPrice);
										maxQuantityTextField.setText(String.valueOf(maxLots));
									}
									else if (quoteOrderButtonContext.contains("卖")) {
										maxLots = ArithmeticUtil.getInteger(fundAvailable, minPrice);
										maxQuantityTextField.setText(String.valueOf(maxLots));
									}
									maxQuantityLabel.setVisible(true);
									quoteOrderButton.setEnabled(true);
								}
								else {
									JOptionPane.showMessageDialog(null, "初始化失败，请保持网络畅通，重新开始初始化!", "系统初始化错误", JOptionPane.ERROR_MESSAGE);
									System.out.println("Initial Failed, reload network!");
								}
								
							}
							else {
								JOptionPane.showMessageDialog(null, "初始化失败，请保持网络畅通，重新开始初始化!", "系统初始化错误", JOptionPane.ERROR_MESSAGE);
								System.out.println("Initial Failed, reload network!");
							}
						}
						else {
							maxPriceTextField.setText("");
							minPriceTextField.setText("");
							maxQuantityTextField.setText("");
							priceTextField.setText("0");
							quantityTextField.setText("1");
							feeTextField.setText("0");
							maxQuantityLabel.setVisible(false);
							quoteOrderButton.setEnabled(false);
							quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
						}
					}
				}
			}
		});
		
		component.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				String string = instrumentComboBox.getEditor().getItem().toString().trim().toLowerCase();
				instrumentEntry = string;
				if (string != null && string.length() > 0) {
					//right instrument
					if (SearchUtil.loopSearch(instrumentIDArray, string)) {
						if (quoteOrderButtonContext.contains("买")) {
							quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
						}
						else if (quoteOrderButtonContext.contains("卖")) {
							quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
						}
						List<Object[]> base_properties = baseDataServiceOperation.getInstrumentProperty(string);
						if (base_properties != null && base_properties.size() == 1) {
							maxPrice = ((Object[])base_properties.get(0))[0].toString();
							minPrice = ((Object[])base_properties.get(0))[1].toString();
							priceTick = new BigDecimal(((Object[])base_properties.get(0))[2].toString());
							settlementPrice = ((Object[])base_properties.get(0))[3].toString();
							if ((maxPrice != null && ArithmeticUtil.compareDetails(maxPrice, "0") > 0) && 
									(minPrice != null && ArithmeticUtil.compareDetails(minPrice, "0") > 0) && 
									(priceTick != null && ArithmeticUtil.compare(priceTick.toString(), "0")) && 
									(settlementPrice != null && ArithmeticUtil.compareDetails(settlementPrice, "0") > 0)) {
								maxPriceTextField.setText(maxPrice);
								minPriceTextField.setText(minPrice);
								if (latestPrice != null && ArithmeticUtil.compareDetails(latestPrice, "0") > 0) {
									priceTextField.setText(latestPrice);
									quantityTextField.setText("1");
									feeTextField.setText("0");
								}
								else {
									priceTextField.setText(settlementPrice);
									quantityTextField.setText("1");
									feeTextField.setText("0");
								}
								if (quoteOrderButtonContext.contains("买")) {
									maxLots = ArithmeticUtil.getInteger(fundAvailable, maxPrice);
									maxQuantityTextField.setText(String.valueOf(maxLots));
								}
								else if (quoteOrderButtonContext.contains("卖")) {
									maxLots = ArithmeticUtil.getInteger(fundAvailable, minPrice);
									maxQuantityTextField.setText(String.valueOf(maxLots));
								}
								maxQuantityLabel.setVisible(true);
								quoteOrderButton.setEnabled(true);
							}
							else {
								JOptionPane.showMessageDialog(null, "初始化失败，请保持网络畅通，重新开始初始化!", "系统初始化错误",JOptionPane.ERROR_MESSAGE);
								System.out.println("Initial Failed, reload network!");
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "初始化失败，请保持网络畅通，重新开始初始化!", "系统初始化错误",JOptionPane.ERROR_MESSAGE);
							System.out.println("Initial Failed, reload network!");
						}
					}
					//no match
					else {
						Object[] filterInstrumentID = SearchUtil.filterItems(instrumentIDArray, string);	//get data from memory
						//	no matched data
						if (filterInstrumentID != null && filterInstrumentID.length > 0) {
							instrumentComboBox.removeAllItems();
							for (Object object : filterInstrumentID) {
								instrumentComboBox.addItem(object);
							}
							instrumentComboBox.setSelectedItem(null);
							instrumentComboBox.showPopup();
						}
						else {
							instrumentComboBox.removeAllItems();
							instrumentComboBox.setSelectedItem(null);
						}
						component.setText(string);
						maxPriceTextField.setText("");
						minPriceTextField.setText("");
						maxQuantityTextField.setText("");
						priceTextField.setText("0");
						quantityTextField.setText("1");
						feeTextField.setText("0");
						maxQuantityLabel.setVisible(false);
						quoteOrderButton.setEnabled(false);
						quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
					}
				}
				//empty entry
				else {
					instrumentComboBox.removeAllItems();
					for (Object object : instrumentIDArray) {
						instrumentComboBox.addItem(object);
					}
					instrumentComboBox.setSelectedItem(null);
					component.setText("");
					maxPriceTextField.setText("");
					minPriceTextField.setText("");
					maxQuantityTextField.setText("");
					priceTextField.setText("0");
					quantityTextField.setText("1");
					feeTextField.setText("0");
					maxQuantityLabel.setVisible(false);
					quoteOrderButton.setEnabled(false);
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				int keyValue = e.getKeyChar();
				char entryChar = e.getKeyChar();
				if ((keyValue >= KeyEvent.VK_0 && keyValue <= KeyEvent.VK_9) || 
						(entryChar >= 'a' && entryChar <= 'z') || 
						(entryChar >= 'A' && entryChar <= 'Z')) {
				}
				else {
					e.consume();
				}
				//	limit entry length
				String string = (instrumentEntry != null && instrumentEntry.length() > 0) ? instrumentEntry : instrumentSelected;
				if (string != null && string.length() > 0) {
					if (string.length() >= 7) {
						e.consume();
					}
				}
			}
			
		});
		instrumentComboBox.setEditable(true);
		instrumentComboBox.setSelectedItem(null);
		instrumentComboBox.setBounds(110, 50, 120, 21);
		contentPanel.add(instrumentComboBox);
	}
}
