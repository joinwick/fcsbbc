/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.common.service.BaseDataServiceOperation;
import com.fcsbbc.utils.ArithmeticUtil;
import com.fcsbbc.utils.SearchUtil;
import com.fcsbbc.utils.UIColor;
import com.fcsbbc.utils.UIFont;

/**
 * @author luo.changshu
 *
 */
public class CreateQuoteOrderFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField fundAvailableTextField;
	private JTextField quantityTextField;
	private JTextField maxQuantityTextField;
	private JTextField priceTextField;
	private JTextField maxPriceTextField;
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
	
	public CreateQuoteOrderFrame(UserBaseBean userBaseBean, QuoteJPanel quoteJPanel, String fundAvailable) {
		this.userBaseBean = userBaseBean;
		this.quoteJPanel = quoteJPanel;
		this.fundAvailable = fundAvailable;
		initialBackgroundPanel();
		this.add(contentPane);
		this.setTitle("新建报单");
		if (width > actualWidth && height > actualHeight) {
			this.setBounds((width - actualWidth) / 2, (height - actualHeight) / 2, actualWidth, actualHeight);
		}
		else {
			this.setBounds(0, 0, width, height);
		}
//		this.setBounds(600, 400, 450, 360);
		this.setVisible(true);
//		this.setLocationRelativeTo(quoteJPanel);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	/**
	 * 
	 */
	private void initialBackgroundPanel() {
		// TODO Auto-generated method stub
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		BaseDataServiceOperation baseDataServiceOperation = new BaseDataServiceOperation();
        instrumentIDArray = baseDataServiceOperation.getInstrumentIDArray();	//get from database
        
//        fundAvailable = "5000.00";	//get from block-chain
        String fundAvailableFormat = ArithmeticUtil.getFormat(fundAvailable);
        
		JLabel fundAvailableLabel = new JLabel("可用资金：");
		fundAvailableLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		fundAvailableLabel.setBounds(90, 13, 68, 15);
		fundAvailableLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(fundAvailableLabel);
		
		fundAvailableTextField = new JTextField(fundAvailableFormat);
		fundAvailableTextField.setColumns(19);
		fundAvailableTextField.setFont(UIFont.DEFAULT_FONT);
		fundAvailableTextField.setEditable(false);
		fundAvailableTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		fundAvailableTextField.setBounds(160, 10, 130, 21);
		fundAvailableTextField.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(fundAvailableTextField);
		
		JLabel instrumentLabel = new JLabel("合约");
		instrumentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instrumentLabel.setBounds(50, 60, 54, 15);
		instrumentLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(instrumentLabel);
		
		JLabel tradeSideLabel = new JLabel("方向");
		tradeSideLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tradeSideLabel.setBounds(50, 90, 54, 15);
		tradeSideLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(tradeSideLabel);
		
		tradeSideBuyButton = new JButton("买");
		tradeSideBuyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tradeSideButtonContext = "买";
//				tradeSideBuyButton.setBackground(UIColor.DEFAULT_BACKGROUND);
//				tradeSideSellButton.setBackground(UIColor.DEFAULT_COLOR);
				tradeSideBuyButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				tradeSideSellButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				String maxPriceContent = maxPriceTextField.getText().trim();
				if (maxPriceContent != null && maxPriceContent.length() > 0) {
//					quoteOrderButton.setBackground(UIColor.DEFAULT_RED);
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
					quoteOrderButton.setEnabled(true);
				}
				else {
//					quoteOrderButton.setBackground(Color.gray);
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
					quoteOrderButton.setEnabled(false);
				}
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		tradeSideBuyButton.setBounds(100, 86, 61, 23);
		tradeSideBuyButton.setFont(UIFont.DEFAULT_FONT);
		tradeSideBuyButton.setFocusPainted(false);
		tradeSideBuyButton.setForeground(Color.black);
//		tradeSideBuyButton.setForeground(Color.white);
//		tradeSideBuyButton.setBackground(UIColor.DEFAULT_BACKGROUND);		
		tradeSideBuyButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		contentPane.add(tradeSideBuyButton);
		
		tradeSideSellButton = new JButton("卖");
		tradeSideSellButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tradeSideButtonContext = "卖";
//				tradeSideBuyButton.setBackground(UIColor.DEFAULT_COLOR);
//				tradeSideSellButton.setBackground(UIColor.DEFAULT_BACKGROUND);
				tradeSideSellButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				tradeSideBuyButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				String maxPriceContent = maxPriceTextField.getText().trim();
				if (maxPriceContent != null && maxPriceContent.length() > 0) {
//					quoteOrderButton.setBackground(UIColor.DEFAULT_GREEN);
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
					quoteOrderButton.setEnabled(true);
				}
				else {
//					quoteOrderButton.setBackground(Color.gray);
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
					quoteOrderButton.setEnabled(false);
				}
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		tradeSideSellButton.setBounds(160, 86, 60, 23);
		tradeSideSellButton.setFont(UIFont.DEFAULT_FONT);
		tradeSideSellButton.setFocusPainted(false);
		tradeSideSellButton.setForeground(Color.black);
//		tradeSideSellButton.setBackground(UIColor.DEFAULT_COLOR);
		tradeSideSellButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPane.add(tradeSideSellButton);
		
		JLabel openFlagLabel = new JLabel("开平");
		openFlagLabel.setHorizontalAlignment(SwingConstants.CENTER);
		openFlagLabel.setBounds(50, 120, 54, 15);
		openFlagLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(openFlagLabel);
		
		openFlagOpenButton = new JButton("开仓");
		openFlagOpenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				openFlagOpenButton.setBackground(UIColor.DEFAULT_BACKGROUND);
//				openFlagCloseYButton.setBackground(UIColor.DEFAULT_COLOR);
//				openFlagCloseTButton.setBackground(UIColor.DEFAULT_COLOR);
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagButtonContext = "开仓";
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		openFlagOpenButton.setBounds(100, 115, 61, 23);
		openFlagOpenButton.setFont(UIFont.DEFAULT_FONT);
		openFlagOpenButton.setFocusPainted(false);
		openFlagOpenButton.setForeground(Color.black);
//		openFlagOpenButton.setBackground(UIColor.DEFAULT_BACKGROUND);
		openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		contentPane.add(openFlagOpenButton);
		
		openFlagCloseYButton = new JButton("平仓");
		openFlagCloseYButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				openFlagCloseYButton.setBackground(UIColor.DEFAULT_BACKGROUND);
//				openFlagOpenButton.setBackground(UIColor.DEFAULT_COLOR);
//				openFlagCloseTButton.setBackground(UIColor.DEFAULT_COLOR);
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagButtonContext = "平仓";
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		openFlagCloseYButton.setBounds(160, 115, 61, 23);
		openFlagCloseYButton.setFont(UIFont.DEFAULT_FONT);
		openFlagCloseYButton.setFocusPainted(false);
		openFlagCloseYButton.setForeground(Color.black);
//		openFlagCloseYButton.setBackground(UIColor.DEFAULT_COLOR);
		openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPane.add(openFlagCloseYButton);
		
		openFlagCloseTButton = new JButton("平今");
		openFlagCloseTButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				openFlagCloseTButton.setBackground(UIColor.DEFAULT_BACKGROUND);
//				openFlagCloseYButton.setBackground(UIColor.DEFAULT_COLOR);
//				openFlagOpenButton.setBackground(UIColor.DEFAULT_COLOR);
				openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
				openFlagCloseYButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagOpenButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				openFlagButtonContext = "平今";
				quoteOrderButtonContext = "下单(" + tradeSideButtonContext +" " + openFlagButtonContext +")";
				quoteOrderButton.setText(quoteOrderButtonContext);
			}
		});
		openFlagCloseTButton.setBounds(220, 115, 61, 23);
		openFlagCloseTButton.setFont(UIFont.DEFAULT_FONT);
		openFlagCloseTButton.setFocusPainted(false);
		openFlagCloseTButton.setForeground(Color.black);
//		openFlagCloseTButton.setBackground(UIColor.DEFAULT_COLOR);
		openFlagCloseTButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPane.add(openFlagCloseTButton);
		
		JLabel quantityLabel = new JLabel("手数");
		quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		quantityLabel.setBounds(50, 150, 54, 15);
		quantityLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(quantityLabel);
		
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
		quantityTextField.setBounds(100, 148, 85, 21);
		quantityTextField.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(quantityTextField);
		quantityTextField.setColumns(10);
		
		ImageIcon subIcon = new ImageIcon("image/sub_button.png");
		ImageIcon addIcon = new ImageIcon("image/add_button.png");
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
//					JOptionPane.showMessageDialog(null, "手数不能为空!");
				}
			}
		});
		quantitySubButton.setPreferredSize(new Dimension(subIcon.getIconWidth(), subIcon.getIconHeight()));
		quantitySubButton.setBounds(184, 148, 19, 20);
		quantitySubButton.setFocusPainted(false);
		contentPane.add(quantitySubButton);
		
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
//					JOptionPane.showMessageDialog(null, "手数不能为空!");
				}
			}
		});
		quantityAddButton.setPreferredSize(new Dimension(subIcon.getIconWidth(), subIcon.getIconHeight()));
		quantityAddButton.setBounds(202, 148, 19, 20);
		quantityAddButton.setFocusPainted(false);
		contentPane.add(quantityAddButton);
		
		JLabel maxQuantityLabel = new JLabel("≤");
		maxQuantityLabel.setVisible(false);
		maxQuantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		maxQuantityLabel.setBounds(250, 151, 31, 15);
		maxQuantityLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(maxQuantityLabel);
		
		maxQuantityTextField = new JTextField();
		maxQuantityTextField.setColumns(10);
		maxQuantityTextField.setEditable(false);
		maxQuantityTextField.setOpaque(false);
		maxQuantityTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		maxQuantityTextField.setFont(UIFont.DEFAULT_FONT);
		maxQuantityTextField.setBounds(295, 148, 66, 21);
		maxQuantityTextField.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(maxQuantityTextField);
		
		
		JLabel priceLabel = new JLabel("价格");
		priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		priceLabel.setBounds(50, 182, 54, 15);
		priceLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(priceLabel);
		
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
		priceTextField.setBounds(100, 180, 85, 21);
		priceTextField.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(priceTextField);
		
		
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
		priceSubButton.setPreferredSize(new Dimension(subIcon.getIconWidth(), subIcon.getIconHeight()));
		priceSubButton.setBounds(184, 180, 19, 20);
		priceSubButton.setFocusPainted(false);
		contentPane.add(priceSubButton);
		
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
		priceAddButton.setPreferredSize(new Dimension(addIcon.getIconWidth(), addIcon.getIconHeight()));
		priceAddButton.setBounds(202, 180, 19, 20);
		priceAddButton.setFocusPainted(false);
		contentPane.add(priceAddButton);
		
		JLabel maxPriceLabel = new JLabel("涨板");
		maxPriceLabel.setForeground(new Color(255, 0, 0));
		maxPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		maxPriceLabel.setBounds(250, 182, 36, 15);
		maxPriceLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(maxPriceLabel);
		
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
		maxPriceTextField.setBounds(295, 180, 66, 21);
		maxPriceTextField.setFont(UIFont.DEFAULT_FONT);
		maxPriceTextField.setEditable(false);
		maxPriceTextField.setOpaque(false);
		maxPriceTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		maxPriceTextField.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(maxPriceTextField);
		
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
				}
				if (quoteFactorCheck && 
						(maxLotsContent != null && maxLotsContent.length() > 0) && 
						(minPriceContent != null && minPriceContent.length() > 0)) {
					lots = ArithmeticUtil.removeDecimalZero(lots);
					price = ArithmeticUtil.removeDecimalZero(price);
					String orderInfo = "确定向市场发出该报单请求：\n" + instrumentID + ", " + tradeSideButtonContext + ", " + openFlagButtonContext + ", " + lots + "手@" + price;
					Object[] operation ={ "确定", "取消" };
					operationResult = JOptionPane.showOptionDialog(null, orderInfo, "报单确认",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, operation, operation[0]);
					System.out.println("operationResult = " + operationResult);
				}
				else if (!(maxLotsContent != null && maxLotsContent.length() > 0)) {
//					quoteOrderButton.setBackground(Color.gray);
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
					quoteOrderButton.setEnabled(false);
				}
				else {
					System.out.println("Special Condition--[" + instrumentComboBox.getEditor().getItem().toString().trim().toLowerCase() + "]");
				}
			}
		});
		quoteOrderButton.setFont(UIFont.DEFAULT_FONT);
		quoteOrderButton.setFocusPainted(false);
		quoteOrderButton.setEnabled(false);
		quoteOrderButton.setBounds(60, 215, 115, 23);
//		quoteOrderButton.setBackground(UIColor.DEFAULT_RED);
		quoteOrderButton.setForeground(Color.black);
//		quoteOrderButton.setBackground(Color.gray);
		quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		contentPane.add(quoteOrderButton);
		
		JButton resetButton = new JButton("复位");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//reset quantity with 1
				quantityTextField.setText("1");
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
		resetButton.setBounds(180, 215, 60, 23);
		resetButton.setBackground(UIColor.DEFAULT_COLOR);
		resetButton.setFont(UIFont.DEFAULT_FONT);
		resetButton.setFocusPainted(false);
		resetButton.setForeground(Color.black);
		contentPane.add(resetButton);
		
		JLabel minPriceLabel = new JLabel("跌板");
		minPriceLabel.setForeground(new Color(50, 205, 50));
		minPriceLabel.setBounds(250, 219, 36, 15);
		minPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		minPriceLabel.setFont(UIFont.DEFAULT_FONT);
		contentPane.add(minPriceLabel);
		
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
		minPriceTextField.setBounds(295, 215, 66, 21);
		minPriceTextField.setFont(UIFont.DEFAULT_FONT);
		minPriceTextField.setEditable(false);
		minPriceTextField.setOpaque(false);
		minPriceTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		minPriceTextField.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(minPriceTextField);
		
		instrumentComboBox = new JComboBox<>(instrumentIDArray);
//		instrumentComboBox.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				String currentContent = instrumentComboBox.getEditor().getItem().toString().trim().toLowerCase();
//				if (currentContent != null && currentContent.length() > 0) {
//					contentEmpty = false;
//				}
//				else {
//					contentEmpty = true;
//				} 
//			}
//		});
//		instrumentComboBox = new SpecialJcombobox(instrumentIDArray);
//      Component component = instrumentComboBox.getEditor().getEditorComponent();
		JTextField component = (JTextField) instrumentComboBox.getEditor().getEditorComponent();
		instrumentComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					instrumentSelected = arg0.getItem().toString();
					System.out.println("selected = " + instrumentSelected);
					if ((instrumentSelected != null && instrumentSelected.length() > 0)) {
						if (SearchUtil.loopSearch(instrumentIDArray, instrumentSelected)) {
							instrumentEntry = instrumentSelected;
							if (quoteOrderButtonContext.contains("买")) {
//								quoteOrderButton.setBackground(UIColor.DEFAULT_RED);
								quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
							}
							else if (quoteOrderButtonContext.contains("卖")) {
//								quoteOrderButton.setBackground(UIColor.DEFAULT_GREEN);
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
									}
									else {
										priceTextField.setText(settlementPrice);
										quantityTextField.setText("1");
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
							maxQuantityLabel.setVisible(false);
							quoteOrderButton.setEnabled(false);
//							quoteOrderButton.setBackground(Color.GRAY);
							quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
						}
					}
				}
			}
		});
        component.addKeyListener(new KeyListener() {
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
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				String string = instrumentComboBox.getEditor().getItem().toString().trim().toLowerCase();
//				System.out.println("entry = " + string);
				instrumentEntry = string;
				if (string != null && string.length() > 0) {
					//right instrument
					if (SearchUtil.loopSearch(instrumentIDArray, string)) {
						if (quoteOrderButtonContext.contains("买")) {
//							quoteOrderButton.setBackground(UIColor.DEFAULT_RED);
							quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
						}
						else if (quoteOrderButtonContext.contains("卖")) {
//							quoteOrderButton.setBackground(UIColor.DEFAULT_GREEN);
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
								}
								else {
									priceTextField.setText(settlementPrice);
									quantityTextField.setText("1");
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
//						Object[] filterInstrumentID = baseDataServiceOperation.getInstrumentIDArrayByCondition(string);	//get data from database in-time
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
						maxQuantityLabel.setVisible(false);
						quoteOrderButton.setEnabled(false);
//						quoteOrderButton.setBackground(Color.GRAY);
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
					maxQuantityLabel.setVisible(false);
					quoteOrderButton.setEnabled(false);
//					quoteOrderButton.setBackground(Color.GRAY);
					quoteOrderButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        instrumentComboBox.setSelectedItem(null);
		instrumentComboBox.setEditable(true);
        instrumentComboBox.setBounds(100, 56, 120, 21);
		contentPane.add(instrumentComboBox);
	}
}
