/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.common.service.UserServiceOperation;
import com.fcsbbc.cryptology.HashModule;
import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.UIFont;
import com.fcsbbc.utils.UtilTool;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * @author luo.changshu
 *
 */
public class PeerLoginJFrame extends JFrame implements FocusListener, MouseListener, KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(PeerLoginJFrame.class.getName());
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
//	private static final int actualWidth = width / 2, actualHeight = height / 2;
	private static final int actualWidth = 680, actualHeight = 450;
	private JTextField userCodeTextField, userCATextField;
	private JPasswordField userPasswordField;
	private JButton loginConfirmButton;
	private JTextField userCodeSplitLine, userPasswordSplitLine, userCASplitLine;
	private JLabel userDefineImageLabel, userCodeImageLabel, userPasswordImageLabel, userCAImageLabel;
	private JLabel userRegisterLabel, userPasswordFindLabel;
	private static final int imageWidth = 21, imageHeight = 21, defineImageWidth = 70, defineImageHeight = 70;
	private static final int textFieldWidth = 180, textFieldHeight = 30;
	ImagePanel backgroundImagePanel = null;
	private JButton fileChooseButton;
	private int itemPosition_X = 46, itemPosition_Y = 50;
	private int userRegisterResult = -2, passwordRecoveryResult = -2;
	private final static String userCodeTextFieldMessage = "用户账号/手机号";
	private final static String userPasswordFieldMessage = "密码";
	private final static String userCATextFieldMessage = "证书";
	/**
	 * Create the frame.
	 */
	public PeerLoginJFrame() {
		new WindowFade(this);
		Image uiBackgroundImage = null;
		try {
			uiBackgroundImage = ImageIO.read(new File("image/loginbackground.png"));
			Image image = ImageIO.read(new File("image/logo.png"));
			this.setIconImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Image File not exist in <PeerLoginJFrame:PeerLoginJFrame>", e.getMessage());
		}
		backgroundImagePanel = new ImagePanel(uiBackgroundImage);
		backgroundImagePanel.setLayout(null);
		getContentPane().add(backgroundImagePanel);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBackground(new Color(255, 255, 255));
		loginPanel.setForeground(Color.WHITE);
		loginPanel.setBounds(250, 0, 424, 422);
//		loginPanel.setBounds((int)0.4 * actualWidth, 0, (int)0.6 * actualWidth, (int)0.936 * actualHeight);
		loginPanel.setLayout(null);
		backgroundImagePanel.add(loginPanel);
		
		ImageIcon userDefineImage = new ImageIcon("image/logo_default_blue.png");
		userDefineImage.setImage(userDefineImage.getImage().getScaledInstance(defineImageWidth, defineImageHeight,Image.SCALE_DEFAULT ));
		userDefineImageLabel = new JLabel();
		userDefineImageLabel.setBounds(itemPosition_X + 80, itemPosition_Y, defineImageWidth, defineImageHeight);
		userDefineImageLabel.setIcon(userDefineImage);
		loginPanel.add(userDefineImageLabel);
		
		userCodeTextField = new JTextField();
		userCodeTextField.setText(userCodeTextFieldMessage);
		userCodeTextField.setBounds(itemPosition_X + 40, itemPosition_Y + 106, textFieldWidth, textFieldHeight);
		userCodeTextField.setFont(UIFont.DEFAULT_FONT);
		userCodeTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		userCodeTextField.setOpaque(false);
		userCodeTextField.addFocusListener(this);
		userCodeTextField.addKeyListener(this);
		loginPanel.add(userCodeTextField);
		
		ImageIcon userCodeImage = new ImageIcon("image/login_user_blue.png");
		userCodeImage.setImage(userCodeImage.getImage().getScaledInstance(imageWidth, imageHeight,Image.SCALE_DEFAULT ));
		userCodeImageLabel = new JLabel();
		userCodeImageLabel.setIcon(userCodeImage);
		userCodeImageLabel.setBounds(itemPosition_X, itemPosition_Y + 112, imageWidth, imageHeight);
		loginPanel.add(userCodeImageLabel);
		
		userCodeSplitLine = new JTextField();
		userCodeSplitLine.setBounds(itemPosition_X, itemPosition_Y + 136, 220, 1);
		userCodeSplitLine.setEditable(false);
		userCodeSplitLine.setBackground(Color.LIGHT_GRAY);
		loginPanel.add(userCodeSplitLine);
		
		userPasswordField = new JPasswordField();
		userPasswordField.setText(userPasswordFieldMessage);
		userPasswordField.setBounds(itemPosition_X + 40, itemPosition_Y + 137, textFieldWidth, textFieldHeight);
		userPasswordField.setFont(UIFont.DEFAULT_FONT);
		userPasswordField.setEchoChar('\0');
		userPasswordField.setOpaque(false);
		userPasswordField.setBorder(new EmptyBorder(0, 0, 0, 0));
		userPasswordField.addFocusListener(this);
		loginPanel.add(userPasswordField);
		
		ImageIcon userPasswordImage = new ImageIcon("image/login_password_blue.png");
		userPasswordImage.setImage(userPasswordImage.getImage().getScaledInstance(imageWidth, imageHeight,Image.SCALE_DEFAULT ));
		userPasswordImageLabel = new JLabel();
		userPasswordImageLabel.setBounds(itemPosition_X, itemPosition_Y + 143, imageWidth, imageHeight);
		userPasswordImageLabel.setIcon(userPasswordImage);
		loginPanel.add(userPasswordImageLabel);
		
		userPasswordSplitLine = new JTextField();
		userPasswordSplitLine.setBounds(itemPosition_X, itemPosition_Y + 167, 220, 1);
		userPasswordSplitLine.setBackground(Color.LIGHT_GRAY);
		userPasswordSplitLine.setEditable(false);
		loginPanel.add(userPasswordSplitLine);
		
		userCATextField = new JTextField();
		userCATextField.setText(userCATextFieldMessage);
		userCATextField.setBounds(itemPosition_X + 40, itemPosition_Y + 168, textFieldWidth, textFieldHeight);
		userCATextField.setFont(UIFont.DEFAULT_FONT);
		userCATextField.setOpaque(false);
		userCATextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		userCATextField.addFocusListener(this);
		loginPanel.add(userCATextField);
		
		ImageIcon userCAImage = new ImageIcon("image/login_ca_blue.png");
		userCAImage.setImage(userCAImage.getImage().getScaledInstance(imageWidth, imageHeight,Image.SCALE_DEFAULT ));
		userCAImageLabel = new JLabel();
		userCAImageLabel.setBounds(itemPosition_X, itemPosition_Y + 174, imageWidth, imageHeight);
		userCAImageLabel.setIcon(userCAImage);
		loginPanel.add(userCAImageLabel);
		
		userCASplitLine = new JTextField();
		userCASplitLine.setBackground(Color.LIGHT_GRAY);
		userCASplitLine.setEditable(false);
		userCASplitLine.setBounds(itemPosition_X, itemPosition_Y + 198, 220, 1);
		userCASplitLine.setColumns(10);
		loginPanel.add(userCASplitLine);
		
		loginConfirmButton = new JButton("登 录");
		loginConfirmButton.setBounds(itemPosition_X + 10, itemPosition_Y + 210, 200, 36);
		loginConfirmButton.setFont(UIFont.Button_FONT);
		loginConfirmButton.setEnabled(false);
		loginConfirmButton.setFocusPainted(false);
		loginConfirmButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		loginConfirmButton.addActionListener(this);
		loginPanel.add(loginConfirmButton);
		
		userRegisterLabel = new JLabel("注册账号");
		userRegisterLabel.setBounds(itemPosition_X + 40, itemPosition_Y + 280, 54, 15);
		userRegisterLabel.setFont(UIFont.MESSAGE_FONT);
		userRegisterLabel.setForeground(Color.LIGHT_GRAY);
		userRegisterLabel.addMouseListener(this);
		loginPanel.add(userRegisterLabel);
		
		userPasswordFindLabel = new JLabel("找回密码");
		userPasswordFindLabel.setBounds(itemPosition_X + 120, itemPosition_Y + 280, 54, 15);
		userPasswordFindLabel.setFont(UIFont.MESSAGE_FONT);
		userPasswordFindLabel.setForeground(Color.LIGHT_GRAY);
		userPasswordFindLabel.addMouseListener(this);
		loginPanel.add(userPasswordFindLabel);
		
		fileChooseButton = new JButton("浏览...");
		fileChooseButton.setBounds(itemPosition_X + 220, itemPosition_Y + 172, 75, 23);
		fileChooseButton.setHorizontalAlignment(SwingConstants.LEFT);
		fileChooseButton.setToolTipText("请选择账户对应的CA证书!");
		fileChooseButton.setFocusPainted(false);
		fileChooseButton.addActionListener(this);
		fileChooseButton.setFont(UIFont.DEFAULT_FONT);
		loginPanel.add(fileChooseButton);
		
		this.setBounds((width - actualWidth) / 2, (height - actualHeight) / 2, actualWidth, actualHeight);
		this.setResizable(false);
		this.setTitle("基于区块链的期货结算系统");
		this.setVisible(true);
		this.requestFocus();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userCodeTextField) {
			if (userCodeTextFieldMessage.equals(userCodeTextField.getText().trim())) {
				userCodeTextField.setText("");
			}
		}
		else if (arg0.getSource() == userPasswordField) {
			if (userPasswordFieldMessage.equals(String.valueOf(userPasswordField.getPassword()))) {
				userPasswordField.setText("");
				userPasswordField.setEchoChar('*');
			}
		}
		else if (arg0.getSource() == userCATextField) {
			if (userCATextFieldMessage.equals(userCATextField.getText())) {
				userCATextField.setText("");
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userCodeTextField) {
			if ("".equals(userCodeTextField.getText())) {
				userCodeTextField.setText(userCodeTextFieldMessage);
			}
		}
		else if (arg0.getSource() == userPasswordField) {
			if ("".equals(String.valueOf(userPasswordField.getPassword()))) {
				userPasswordField.setText(userPasswordFieldMessage);
				userPasswordField.setEchoChar('\0');
			}
		}
		else if (arg0.getSource() == userCATextField) {
			if ("".equals(userCATextField.getText())) {
				userCATextField.setText(userCATextFieldMessage);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//用户账号注册
		if (arg0.getSource() == userRegisterLabel) {
			UserRegisterJDialog userRegisterJDialog = new UserRegisterJDialog();
			userRegisterJDialog.setModal(true);
			userRegisterJDialog.setVisible(true);
			userRegisterResult = ((UserRegisterJDialog)userRegisterJDialog).userRegisterResult();
			if (userRegisterResult == 0) {
				System.out.println("user general info insert database");
			}
		}
		//用户账号密码重置
		else if (arg0.getSource() == userPasswordFindLabel) {
			PasswordRecoveryJDialog passwordRecoveryJDialog = new PasswordRecoveryJDialog();
			passwordRecoveryJDialog.setModal(true);
			passwordRecoveryJDialog.setVisible(true);
			passwordRecoveryResult = ((PasswordRecoveryJDialog)passwordRecoveryJDialog).passwordRecoveryResult();
			if (passwordRecoveryResult == 0) {
				System.out.println("user general info insert database");
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//用户注册
		if (arg0.getSource() == userRegisterLabel) {
			userRegisterLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			userRegisterLabel.setForeground(new Color(96, 187, 252));
			
		}
		//密码找回
		else if (arg0.getSource() == userPasswordFindLabel) {
			userPasswordFindLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			userPasswordFindLabel.setForeground(new Color(96, 187, 252));
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userRegisterLabel) {
			setCursor(Cursor.getDefaultCursor());
			userRegisterLabel.setForeground(Color.LIGHT_GRAY);
		}
		else if (arg0.getSource() == userPasswordFindLabel) {
			setCursor(Cursor.getDefaultCursor());
			userPasswordFindLabel.setForeground(Color.LIGHT_GRAY);
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

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userCodeTextField) {
			String userCode = userCodeTextField.getText().trim();
			if (null != userCode && !userCode.equals("")) {
				loginConfirmButton.setEnabled(true);
				loginConfirmButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
			}
			else {
				loginConfirmButton.setEnabled(false);
				loginConfirmButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userCodeTextField) {
			String userCode = userCodeTextField.getText().trim();
//			StringBuffer stringBuffer = new StringBuffer(userCodeTextField.getText().trim());
			int keyValue = arg0.getKeyChar();
			char entryChar = arg0.getKeyChar();
			if ((keyValue >= KeyEvent.VK_0 && keyValue <= KeyEvent.VK_9) || 
					(entryChar >= 'a' && entryChar <= 'z') || 
					(entryChar >= 'A' && entryChar <= 'Z')) {
			}
			else {
				arg0.consume();
			}
			//	limit entry length
			if (null != userCode && !userCode.equals("")) {
				if (userCode.length() >= 20) {
					arg0.consume();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == fileChooseButton) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
			jFileChooser.showDialog(new JLabel(), "选择");
	        File file = jFileChooser.getSelectedFile();
	        if (file != null) {
	        	if(file.isFile()) {
	        		StringBuffer stringBuffer = new StringBuffer(file.getAbsolutePath());
	                userCATextField.setText(stringBuffer.toString());
	            }
	        	else {
	        		userCATextField.setText("证书");
				}
			}
	        else {
	        	userCATextField.setText("证书");
			}
		}
		else if (arg0.getSource() == loginConfirmButton) {
			String userCode = userCodeTextField.getText().trim();
			String password = String.valueOf(userPasswordField.getPassword());
			String caFilePath = userCATextField.getText().trim();
			UserServiceOperation userServiceOperation = new UserServiceOperation();
			UserBaseBean userBaseBean = null;
			boolean infoValidCheck = true;
			//验证是否有未填信息
			infoValidCheck = checkInfoEmpty(userCode, password, caFilePath);
			//验证用户账号/手机号是否有效
			if (infoValidCheck) {
				Object[] queryObjects = {userCode, userCode};
				List<Object[]> queryResult = userServiceOperation.query(queryObjects, 0, 5, ConfigMessage.DEFAULT_DATABASE_TYPE);
				int queryResultCode = userServiceOperation.getOperationCode();
				if (null != queryResult && !queryResult.isEmpty()) {
					if (queryResult.size() == 1) {
						userBaseBean = new UserBaseBean(
								(String) ((Object[]) queryResult.get(0))[0], 
								(String) ((Object[]) queryResult.get(0))[1], 
								(String) ((Object[]) queryResult.get(0))[2], 
								(String) ((Object[]) queryResult.get(0))[3], 
								(int) ((Object[]) queryResult.get(0))[4], 
								(String) ((Object[]) queryResult.get(0))[5], 
								(int) ((Object[]) queryResult.get(0))[6], 
								(String) ((Object[]) queryResult.get(0))[7], 
								String.valueOf(((Object[]) queryResult.get(0))[8]));
						if (null != userBaseBean) {
							if (userBaseBean.getIs_active() != 1) {
								infoValidCheck = false;
								JOptionPane.showMessageDialog(null, "用户账号已被注销, 如需激活，请联系监管机构!");
							}
							
						}
					}
					else {
						infoValidCheck = false;
						logger.error("Get more than one User General info, please check it!");
					}
				}
				else {
					infoValidCheck = false;
					if (queryResultCode == 1) {
						if (userCode.length() > 11) {
							JOptionPane.showMessageDialog(null, "用户账号不存在, 请重新输入!", "数据查询错误", JOptionPane.ERROR_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog(null, "用户手机号不存在, 请重新输入!", "数据查询错误", JOptionPane.ERROR_MESSAGE);
						}
					}
					else if (queryResultCode == 10) {
						JOptionPane.showMessageDialog(null, "数据查询参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
					}
					else if (queryResultCode == 11 || queryResultCode == 12) {
						JOptionPane.showMessageDialog(null, "数据查询异常，请确认网络及系统配置信息是否正确!", "数据查询异常", JOptionPane.ERROR_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			//验证密码是否正确
			if (infoValidCheck) {
				String encryptPassword = HashModule.sha256(password, true);
				if (null != encryptPassword && !encryptPassword.equals("")) {
					if (!encryptPassword.equals(userBaseBean.getUser_password())) {
						infoValidCheck = false;
						JOptionPane.showMessageDialog(null, "用户密码有误, 请重新输入!");
					}
				}
				else {
					infoValidCheck = false;
					logger.error("Generate encrypt info failed in function<HashMoudle:sha256>, please check it!");
				}
			}
			//验证证书
			if (infoValidCheck) {
				//校验数字证书是否有效
				boolean checkCAValid = true;
				
			}
			//校验通过
			if (infoValidCheck) {
				this.setVisible(false);
				new IndexJFrame(userBaseBean);
			}
		}
	}
	
	public boolean checkInfoEmpty(String userCode, String password, String caFilePath) {
		boolean infoValidCheck = true;
		if (null != userCode && !userCode.equals("") && !userCodeTextFieldMessage.equals(userCode)) {
			if (null != password && !password.equals("") && !userPasswordFieldMessage.equals(password)) {
				if (null != caFilePath && !caFilePath.equals("") && !userCATextFieldMessage.equals(caFilePath)) {
					int fileCheckResult = UtilTool.fileValidCheck(caFilePath);
					if (fileCheckResult == 0) {
						if (!"cer".equals(UtilTool.getFileSuffix(caFilePath))) {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "您选择或输入的用户证书文件格式不正确, 请重新选择或输入!");
						}
					}
					else {
						if (fileCheckResult == 1) {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "您选择或输入的用户证书文件不存在, 请重新选择或输入!");
						}
						else if (fileCheckResult == 2) {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "请选择或输入用户账户对应的证书文件, 而非文件目录!");
						}
						else {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "请选择或输入用户账户对应的证书文件!");
						}
					}
				}
				else {
					infoValidCheck = false;
					JOptionPane.showMessageDialog(null, "用户证书不能为空");
				}
			}
			else {
				infoValidCheck = false;
				JOptionPane.showMessageDialog(null, "用户密码不能为空");
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "用户账号不能为空");
		}
		return infoValidCheck;
	}
}

class DrawLinePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.setColor(Color.RED);
		graphics.drawLine(56, 186, 276, 186);
	}
}
