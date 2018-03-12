/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.common.service.UserServiceOperation;
import com.fcsbbc.cryptology.HashModule;
import com.fcsbbc.utils.UIFont;
import com.fcsbbc.utils.UtilTool;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * @author luo.changshu
 *
 */
public class PasswordResetJDialog extends JDialog implements FocusListener, KeyListener, ActionListener{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(PasswordResetJDialog.class.getName());
	private final JPanel contentPanel = new JPanel();
	private static final int itemPosition_X = 60, itemPosition_Y = 85, itemInterval = 35, adjustInterval = 3, stepPosition_X = 110, stepPosition_Y = 30;
	private static final int labelWidth = 60, filedWidth = 170, starLabelWidth = 13, commonComponentWidth = 90;
	private static final int actualWidth = 500, actualHeight = 400;
	private final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	private final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	private final static String passwordFieldMessage = "字母或数字组合";
	private final static String passwordConfirmFieldMessage = "请确认密码";
	public int operationResult = -2;
	private JTextField userCodeTextField;
	private JPasswordField userPasswordField, userPasswordConfirmField;
	private JButton submitButton;
	UserServiceOperation userServiceOperation = new UserServiceOperation();
//	UserBaseBean userBaseBean;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			PasswordReset dialog = new PasswordReset();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public int userPasswordResetResult() {
		return operationResult;
	}
	
	public void closeCurrentWindow() {
		int operationResult = userPasswordResetResult();
		if (operationResult == 0) {
			this.dispose();
		}
		else {
			System.out.println("重置密码失败");
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public PasswordResetJDialog(String userCode) {
//		this.userBaseBean = userBaseBean;
		if (width > actualWidth && height > actualHeight) {
			this.setBounds((width - actualWidth) / 2, (height - actualHeight) / 2, actualWidth, actualHeight);
		}
		else {
			this.setBounds(0, 0, actualWidth, actualHeight);
		}
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		setTitle("用户密码重置");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel stepOneLabel = new JLabel("步骤1:验证账号");
		stepOneLabel.setEnabled(false);
		stepOneLabel.setFont(UIFont.MESSAGE_FONT);
		stepOneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stepOneLabel.setBounds(stepPosition_X, stepPosition_Y, commonComponentWidth, 15);
		contentPanel.add(stepOneLabel);
		
		JLabel stepInterval = new JLabel("-----");
		stepInterval.setFont(UIFont.MESSAGE_FONT);
		stepInterval.setBounds(stepPosition_X + commonComponentWidth + 10, stepPosition_Y, 30, 15);
		contentPanel.add(stepInterval);
		
		JLabel stepTwoLabel = new JLabel("步骤2:重置密码");
		stepTwoLabel.setForeground(Color.blue);
		stepTwoLabel.setFont(UIFont.MESSAGE_FONT);
		stepTwoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stepTwoLabel.setBounds(stepPosition_X + commonComponentWidth + 30 + 20, stepPosition_Y, commonComponentWidth, 15);
		contentPanel.add(stepTwoLabel);
		
		JLabel userCodeStarLabel = new JLabel("*");
		userCodeStarLabel.setForeground(Color.RED);
		userCodeStarLabel.setFont(UIFont.MESSAGE_FONT);
		userCodeStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userCodeStarLabel.setBounds(itemPosition_X, itemPosition_Y, starLabelWidth, 13);
		contentPanel.add(userCodeStarLabel);
		
		JLabel userCodeLabel = new JLabel("用户账号:");
		userCodeLabel.setFont(UIFont.DEFAULT_FONT);
		userCodeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		userCodeLabel.setBounds(itemPosition_X + starLabelWidth, itemPosition_Y - adjustInterval, labelWidth, 15);
		contentPanel.add(userCodeLabel);
		
		userCodeTextField = new JTextField();
		userCodeTextField.setEditable(false);
		userCodeTextField.setText(userCode);
		userCodeTextField.setFont(UIFont.MESSAGE_FONT);
		userCodeTextField.setHorizontalAlignment(SwingConstants.LEFT);
		userCodeTextField.setBounds(itemPosition_X + starLabelWidth + labelWidth + 1, itemPosition_Y - 2 * adjustInterval, filedWidth, 21);
		userCodeTextField.addFocusListener(this);
		userCodeTextField.addKeyListener(this);
		contentPanel.add(userCodeTextField);
		
		JLabel userPasswordStarLabel = new JLabel("*");
		userPasswordStarLabel.setForeground(Color.RED);
		userPasswordStarLabel.setFont(UIFont.MESSAGE_FONT);
		userPasswordStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userPasswordStarLabel.setBounds(itemPosition_X, itemPosition_Y + itemInterval, starLabelWidth, 13);
		contentPanel.add(userPasswordStarLabel);
		
		JLabel userPasswordLabel = new JLabel("新密码:");
		userPasswordLabel.setFont(UIFont.DEFAULT_FONT);
		userPasswordLabel.setHorizontalAlignment(SwingConstants.LEFT);
		userPasswordLabel.setBounds(itemPosition_X + starLabelWidth, itemPosition_Y + itemInterval - adjustInterval, labelWidth, 15);
		contentPanel.add(userPasswordLabel);
		
		userPasswordField = new JPasswordField();
		userPasswordField.setText(passwordFieldMessage);
		userPasswordField.setFont(UIFont.MESSAGE_FONT);
		userPasswordField.setHorizontalAlignment(SwingConstants.LEFT);
		userPasswordField.setBounds(itemPosition_X + starLabelWidth + labelWidth + 1, itemPosition_Y + itemInterval - 2 * adjustInterval, filedWidth, 21);
		userPasswordField.setEchoChar('\0');
		userPasswordField.addFocusListener(this);
		userPasswordField.addKeyListener(this);
		contentPanel.add(userPasswordField);
		
		JLabel userPasswordConfirmStarLabel = new JLabel("*");
		userPasswordConfirmStarLabel.setForeground(Color.RED);
		userPasswordConfirmStarLabel.setFont(UIFont.MESSAGE_FONT);
		userPasswordConfirmStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userPasswordConfirmStarLabel.setBounds(itemPosition_X, itemPosition_Y + 2 * itemInterval, starLabelWidth, 13);
		contentPanel.add(userPasswordConfirmStarLabel);
		
		JLabel userPasswordConfirmLabel = new JLabel("确认密码:");
		userPasswordConfirmLabel.setFont(UIFont.DEFAULT_FONT);
		userPasswordConfirmLabel.setHorizontalAlignment(SwingConstants.LEFT);
		userPasswordConfirmLabel.setBounds(itemPosition_X + starLabelWidth, itemPosition_Y + 2 * itemInterval - adjustInterval, labelWidth, 15);
		contentPanel.add(userPasswordConfirmLabel);
		
		userPasswordConfirmField = new JPasswordField();
		userPasswordConfirmField.setText(passwordConfirmFieldMessage);
		userPasswordConfirmField.setFont(UIFont.MESSAGE_FONT);
		userPasswordConfirmField.setHorizontalAlignment(SwingConstants.LEFT);
		userPasswordConfirmField.setBounds(itemPosition_X + starLabelWidth + labelWidth + 1, itemPosition_Y + 2 * itemInterval - 2 * adjustInterval, filedWidth, 21);
		userPasswordConfirmField.setEchoChar('\0');
		userPasswordConfirmField.addFocusListener(this);
		userPasswordConfirmField.addKeyListener(this);
		contentPanel.add(userPasswordConfirmField);
		
		submitButton = new JButton("确认");
		submitButton.setFocusPainted(false);
		submitButton.setFont(UIFont.Button_FONT);
		submitButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		submitButton.setBounds(itemPosition_X + starLabelWidth + labelWidth + labelWidth / 2, itemPosition_Y + 3 * itemInterval, commonComponentWidth, 30);
		submitButton.addActionListener(this);
		contentPanel.add(submitButton);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String userCode = userCodeTextField.getText().trim();
		String userPassword = String.valueOf(userPasswordField.getPassword());
		String userPasswordConfirm = String.valueOf(userPasswordConfirmField.getPassword());
		boolean infoValidCheck = true;
		if (arg0.getSource() == submitButton) {
			//验证用户账号
			infoValidCheck = checkUserCode(userCode);
			//验证密码
			if (infoValidCheck) {
				infoValidCheck = checkPassword(userPassword);
			}
			//验证确认密码
			if (infoValidCheck) {
				infoValidCheck = checkPassword(userPassword, userPasswordConfirm);
			}
			if (infoValidCheck) {
				//数据更新
				int updateResultCode = -1;
				//密码加密
				String cryptologyPassword = HashModule.sha256(userPassword, true);
				String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
				Object[] updateObjects = {cryptologyPassword, currentUTCTimeStamp, userCode};
				//update info
				boolean updateResult = userServiceOperation.updateInfo(updateObjects, 0, 2, 1);
				updateResultCode = userServiceOperation.getOperationCode();
				if (updateResult) {
					logger.info("Identity info insert successfully!");
					operationResult = 0;
				}
				else {
					infoValidCheck = false;
					if (updateResultCode == 2) {
						JOptionPane.showMessageDialog(null, "数据插入失败，请确认网络及系统配置信息是否正确!", "数据插入失败", JOptionPane.ERROR_MESSAGE);
					}
					else if (updateResultCode == 10) {
						JOptionPane.showMessageDialog(null, "数据插入参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
					}
					else if (updateResultCode == 11) {
						JOptionPane.showMessageDialog(null, "数据插入异常，请确认网络及系统配置信息是否正确!", "数据插入异常", JOptionPane.ERROR_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
					}
				}
//				closeCurrentWindow();
			}
			closeCurrentWindow();
		}
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
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userPasswordField) {
			int keyValue = arg0.getKeyChar();
			char entryChar = arg0.getKeyChar();
			if ((keyValue >= KeyEvent.VK_0 && keyValue <= KeyEvent.VK_9) || 
					(entryChar >= 'a' && entryChar <= 'z') || 
					(entryChar >= 'A' && entryChar <= 'Z')) {
				
			}
			else {
				arg0.consume();
			}
		}
		else if (arg0.getSource() == userPasswordConfirmField) {
			int keyValue = arg0.getKeyChar();
			char entryChar = arg0.getKeyChar();
			if ((keyValue >= KeyEvent.VK_0 && keyValue <= KeyEvent.VK_9) || 
					(entryChar >= 'a' && entryChar <= 'z') || 
					(entryChar >= 'A' && entryChar <= 'Z')) {
				
			}
			else {
				arg0.consume();
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userPasswordField) {
			if (passwordFieldMessage.equals(String.valueOf(userPasswordField.getPassword()))) {
				userPasswordField.setText("");
				userPasswordField.setEchoChar('*');
			}
		}
		else if (arg0.getSource() == userPasswordConfirmField) {
			if (passwordConfirmFieldMessage.equals(String.valueOf(userPasswordConfirmField.getPassword()))) {
				userPasswordConfirmField.setText("");
				userPasswordConfirmField.setEchoChar('*');
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userPasswordField) {
			if ("".equals(String.valueOf(userPasswordField.getPassword()))) {
				userPasswordField.setText(passwordFieldMessage);
				userPasswordField.setEchoChar('\0');
			}
		}
		else if (arg0.getSource() == userPasswordConfirmField) {
			if ("".equals(String.valueOf(userPasswordConfirmField.getPassword()))) {
				userPasswordConfirmField.setText(passwordConfirmFieldMessage);
				userPasswordConfirmField.setEchoChar('\0');
			}
		}
	}
	
	public boolean checkUserCode(String userCode) {
		boolean infoValidCheck = true;
		int operationResultCode = -1;
		boolean multipleRecords = false;
		if (userCode != null && userCode.length() > 0) {
			Object[] objects = {userCode};
			boolean checkExist = false;
			List<Object[]> userGeneralInfoList = userServiceOperation.query(objects, 0, 4, 1);
			operationResultCode = userServiceOperation.getOperationCode();
			if (userGeneralInfoList != null && !userGeneralInfoList.isEmpty() && userGeneralInfoList.size() > 0) {
				if (userGeneralInfoList.size() == 1) {
					checkExist = true;
					//user_id, user_role, user_telephone, user_ca_path, is_active, user_system_id
					UserBaseBean queryUserBaseBean = new UserBaseBean(
							(String)((Object[]) userGeneralInfoList.get(0))[0], 
							(String)((Object[]) userGeneralInfoList.get(0))[1], 
							(int)((Object[]) userGeneralInfoList.get(0))[2], 
							(String)((Object[]) userGeneralInfoList.get(0))[3], 
							(int)((Object[]) userGeneralInfoList.get(0))[4], 
							(String)((Object[]) userGeneralInfoList.get(0))[5]);
					if (checkExist && queryUserBaseBean != null) {
						if (queryUserBaseBean.getIs_active() != 1) {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "用户账号失活, 请联系监管机构激活用户账号!", "用户账号失活", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else {
					infoValidCheck = false;
					multipleRecords = true;
					logger.error("Get more than one audit info in function<UserRegisterJDialog:actionPerformed>, please check it!");
				}
			}
			else {
				infoValidCheck = false;
				if (operationResultCode == 1 || multipleRecords) {
					JOptionPane.showMessageDialog(null, "用户账号不存在, 请联系监管机构确认用户账号是否已注册并激活!", "用户账号不存在", JOptionPane.ERROR_MESSAGE);
				}
				else if (operationResultCode == 10) {
					JOptionPane.showMessageDialog(null, "数据查询参数错误, 请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
				}
				else if (operationResultCode == 11 || operationResultCode == 12) {
					JOptionPane.showMessageDialog(null, "数据查询异常, 请确认网络及系统配置信息是否正确!", "数据查询异常", JOptionPane.ERROR_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "未知错误, 请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "用户账号为空, 请保持网络畅通并重新启动软件!", "用户账号获取异常", JOptionPane.ERROR_MESSAGE);
		}
		return infoValidCheck;
	}
	
	public boolean checkPassword(String userPassword) {
		boolean infoValidCheck = true;
		if (userPassword != null && userPassword.length() > 0 && !passwordFieldMessage.equals(userPassword)) {
			boolean passwordValidCheck = UtilTool.isValidPassword(userPassword);
			if (!passwordValidCheck) {
				infoValidCheck = false;
				JOptionPane.showMessageDialog(null, "密码应同时包含数字和字母, 密码长度为[6, 16], 请重新输入!", "密码输入错误", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "密码不能为空，请重新输入密码!", "密码为空", JOptionPane.ERROR_MESSAGE);
		}
		return infoValidCheck;
	}
	
	public boolean checkPassword(String userPassword, String userPasswordConfirm) {
		boolean infoValidCheck = true;
		if (userPasswordConfirm != null && userPasswordConfirm.length() > 0 && !passwordConfirmFieldMessage.equals(userPasswordConfirm)) {
			if (!userPasswordConfirm.equals(userPassword)) {
				infoValidCheck = false;
				JOptionPane.showMessageDialog(null, "两次输入的密码不一致, 请重新输入!", "密码校验错误", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "确认密码不能为空，请重新输入确认密码!", "确认密码为空", JOptionPane.ERROR_MESSAGE);
		}
		return infoValidCheck;
	}
}
