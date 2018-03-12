/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.fcsbbc.common.domain.AuditBean;
import com.fcsbbc.common.domain.IdentityBean;
import com.fcsbbc.common.service.UserServiceOperation;
import com.fcsbbc.cryptology.HashModule;
import com.fcsbbc.utils.ArithmeticUtil;
import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.IdentityCodeMessage;
import com.fcsbbc.utils.LocalizeConfig;
import com.fcsbbc.utils.UIFont;
import com.fcsbbc.utils.UtilTool;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;

/**
 * @author luo.changshu
 *
 */
public class UserRegisterJDialog extends JDialog implements FocusListener, KeyListener, ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(UserRegisterJDialog.class.getName());
	private final JPanel contentPanel = new JPanel();
	private JTextField userCodeTextField;
	private JTextField telephoneTextField;
	private JTextField identifyCodeTextField;
	private JTextField identifyKeyTextField;
	private JPasswordField userPasswordField;
	private JPasswordField userPasswordConfirmField;
	private JComboBox<Object> memberTypeJComboBox;
	private JButton identifyCodeButton, infoConfirmButton;
	private static final int itemPosition_X = 70, itemPosition_Y = 60, itemInterval = 35, adjustInterval = 3;
	private static final int labelWidth = 60, filedWidth = 170, buttonWidth = 100, starLabelWidth = 13;
	private static final int actualWidth = 500, actualHeight = 400;
	private final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	private final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	private final static String userCodeTextFieldMessage = "身份证号或社会统一信用码";
	private final static String passwordFieldMessage = "字母或数字组合";
	private final static String passwordConfirmFieldMessage = "请确认密码";
	private final static String telephoneTextFieldMessage = "11位手机号码";
	private final static String identityCodeTextFieldMessage = "6位数字验证码";
	private final static String identifyKeyTextFieldMessage = "输入密钥信封中四位校验码";
	private final static Object[] userRoleList = {LocalizeConfig.USER_ROLE_CUSTOMER, LocalizeConfig.USER_ROLE_MEMBER, LocalizeConfig.USER_ROLE_REGULATOR};
	private String userRoleSelected = userRoleList[0].toString();
	private int operationResultCode = -1;
	public int operationResult = -2;
	private int countDownTime = ConfigMessage.IDENTITY_CODE_RESENT_TIME_COUNT_DOWN;
	UserServiceOperation userServiceOperation = new UserServiceOperation();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UserRegisterJDialog dialog = new UserRegisterJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int userRegisterResult() {
		return operationResult;
	}
	
	public void closeCurrentWindow() {
		int operationResult = userRegisterResult();
		if (operationResult == 0) {
			this.dispose();
		}
		else {
			System.out.println("注册失败");
		}
	}

	/**
	 * Create the dialog.
	 */
	public UserRegisterJDialog() {
		if (width > actualWidth && height > actualHeight) {
			this.setBounds((width - actualWidth) / 2, (height - actualHeight) / 2, actualWidth, actualHeight);
		}
		else {
			this.setBounds(0, 0, actualWidth, actualHeight);
		}
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		setTitle("用户账户注册");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel memberTypeLabel = new JLabel("会员类型:");
		memberTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		memberTypeLabel.setBounds(itemPosition_X, itemPosition_Y - itemInterval + adjustInterval, labelWidth, 15);
		memberTypeLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(memberTypeLabel);
		
		memberTypeJComboBox = new JComboBox<>(userRoleList);
		memberTypeJComboBox.setBounds(itemPosition_X + (labelWidth + 1), itemPosition_Y - itemInterval, filedWidth, 21);
		memberTypeJComboBox.setFont(UIFont.DEFAULT_FONT);
		memberTypeJComboBox.addItemListener(this);
		contentPanel.add(memberTypeJComboBox);
		
		JLabel userCodeLabel = new JLabel("用户账号:");
		userCodeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		userCodeLabel.setBounds(itemPosition_X, itemPosition_Y, labelWidth, 15);
		userCodeLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(userCodeLabel);
		
		userCodeTextField = new JTextField();
		userCodeTextField.setText(userCodeTextFieldMessage);
		userCodeTextField.setHorizontalAlignment(SwingConstants.LEFT);
		userCodeTextField.setBounds(itemPosition_X + (labelWidth + 1), itemPosition_Y - adjustInterval, filedWidth, 21);
		userCodeTextField.setColumns(10);
		userCodeTextField.setFont(UIFont.MESSAGE_FONT);
		userCodeTextField.addFocusListener(this);
		userCodeTextField.addKeyListener(this);
		contentPanel.add(userCodeTextField);
		
		JLabel userPasswordLabel = new JLabel("密码:");
		userPasswordLabel.setHorizontalAlignment(SwingConstants.LEFT);
		userPasswordLabel.setBounds(itemPosition_X, itemPosition_Y + itemInterval, labelWidth, 15);
		userPasswordLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(userPasswordLabel);
		
		userPasswordField = new JPasswordField(passwordFieldMessage);
		userPasswordField.setHorizontalAlignment(SwingConstants.LEFT);
		userPasswordField.setBounds(itemPosition_X + (labelWidth + 1), itemPosition_Y - adjustInterval + itemInterval, filedWidth, 21);
		userPasswordField.setFont(UIFont.MESSAGE_FONT);
		userPasswordField.setEchoChar('\0');
		userPasswordField.addFocusListener(this);
		userPasswordField.addKeyListener(this);
		contentPanel.add(userPasswordField);
		
		JLabel userPasswordConfirmLabel = new JLabel("确认密码:");
		userPasswordConfirmLabel.setHorizontalAlignment(SwingConstants.LEFT);
		userPasswordConfirmLabel.setBounds(itemPosition_X, itemPosition_Y + 2 * itemInterval, labelWidth, 15);
		userPasswordConfirmLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(userPasswordConfirmLabel);
		
		userPasswordConfirmField = new JPasswordField(passwordConfirmFieldMessage);
		userPasswordConfirmField.setHorizontalAlignment(SwingConstants.LEFT);
		userPasswordConfirmField.setBounds(itemPosition_X + (labelWidth + 1), itemPosition_Y - adjustInterval + 2 * itemInterval, filedWidth, 21);
		userPasswordConfirmField.setFont(UIFont.MESSAGE_FONT);
		userPasswordConfirmField.setEchoChar('\0');
		userPasswordConfirmField.addFocusListener(this);
		userPasswordConfirmField.addKeyListener(this);
		contentPanel.add(userPasswordConfirmField);
		
		JLabel telephoneLabel = new JLabel("手机号:");
		telephoneLabel.setHorizontalAlignment(SwingConstants.LEFT);
		telephoneLabel.setBounds(itemPosition_X, itemPosition_Y + adjustInterval * itemInterval, labelWidth, 15);
		telephoneLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(telephoneLabel);
		
		telephoneTextField = new JTextField();
		telephoneTextField.setText(telephoneTextFieldMessage);
		telephoneTextField.setBounds(itemPosition_X + (labelWidth + 1), itemPosition_Y - adjustInterval + 3 * itemInterval, filedWidth, 21);
		telephoneTextField.setFont(UIFont.MESSAGE_FONT);
		telephoneTextField.addFocusListener(this);
		telephoneTextField.addKeyListener(this);
		contentPanel.add(telephoneTextField);
		
		JLabel identifyCodeLabel = new JLabel("验证码:");
		identifyCodeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		identifyCodeLabel.setBounds(itemPosition_X, itemPosition_Y + 4 * itemInterval, labelWidth, 15);
		identifyCodeLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(identifyCodeLabel);
		
		identifyCodeTextField = new JTextField();
		identifyCodeTextField.setText(identityCodeTextFieldMessage);
		identifyCodeTextField.setBounds(itemPosition_X + (labelWidth + 1), itemPosition_Y - adjustInterval + 4 * itemInterval, filedWidth, 21);
		identifyCodeTextField.setFont(UIFont.MESSAGE_FONT);
		identifyCodeTextField.addFocusListener(this);
		identifyCodeTextField.addKeyListener(this);
		contentPanel.add(identifyCodeTextField);
		
		identifyCodeButton = new JButton("获取验证码");
		identifyCodeButton.setBounds(itemPosition_X + (labelWidth + 1) + filedWidth, itemPosition_Y - (adjustInterval + 1) + 4 * itemInterval, buttonWidth, 23);
		identifyCodeButton.setFont(UIFont.DEFAULT_FONT);
		identifyCodeButton.setFocusPainted(false);
		identifyCodeButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		identifyCodeButton.addActionListener(this);
		contentPanel.add(identifyCodeButton);
		
		JLabel identifyKeyLabel = new JLabel("校验密钥:");
		identifyKeyLabel.setHorizontalAlignment(SwingConstants.LEFT);
		identifyKeyLabel.setBounds(itemPosition_X, itemPosition_Y + 5 * itemInterval, labelWidth, 15);
		identifyKeyLabel.setFont(UIFont.DEFAULT_FONT);
		contentPanel.add(identifyKeyLabel);
		
		identifyKeyTextField = new JTextField(identifyKeyTextFieldMessage);
		identifyKeyTextField.setBounds(itemPosition_X + (labelWidth + 1), itemPosition_Y - adjustInterval + 5 * itemInterval, filedWidth, 21);
		identifyKeyTextField.setColumns(10);
		identifyKeyTextField.setFont(UIFont.MESSAGE_FONT);
		identifyKeyTextField.addFocusListener(this);
		identifyKeyTextField.addKeyListener(this);
		contentPanel.add(identifyKeyTextField);
		
		infoConfirmButton = new JButton("提交注册");
		infoConfirmButton.setBounds(itemPosition_X + (labelWidth + 1) + 20, itemPosition_Y - adjustInterval + 6 * itemInterval, 100, 30);
		infoConfirmButton.setFont(UIFont.Button_FONT);
		infoConfirmButton.setFocusPainted(false);
		infoConfirmButton.setEnabled(false);
		infoConfirmButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		infoConfirmButton.addActionListener(this);
		contentPanel.add(infoConfirmButton);
		
		JLabel memberTypeStarLabel = new JLabel("*");
		memberTypeStarLabel.setForeground(Color.RED);
		memberTypeStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		memberTypeStarLabel.setBounds(itemPosition_X - starLabelWidth, itemPosition_Y + adjustInterval - itemInterval, starLabelWidth, 13);
		memberTypeStarLabel.setFont(UIFont.MESSAGE_FONT);
		contentPanel.add(memberTypeStarLabel);
		
		JLabel userCodeStarLabel = new JLabel("*");
		userCodeStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userCodeStarLabel.setForeground(Color.RED);
		userCodeStarLabel.setBounds(itemPosition_X - starLabelWidth, itemPosition_Y + adjustInterval, starLabelWidth, 13);
		userCodeStarLabel.setFont(UIFont.MESSAGE_FONT);
		contentPanel.add(userCodeStarLabel);
		
		JLabel userPasswordStarLabel = new JLabel("*");
		userPasswordStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userPasswordStarLabel.setForeground(Color.RED);
		userPasswordStarLabel.setBounds(itemPosition_X - starLabelWidth, itemPosition_Y + adjustInterval + itemInterval, starLabelWidth, 13);
		userPasswordStarLabel.setFont(UIFont.MESSAGE_FONT);
		contentPanel.add(userPasswordStarLabel);
		
		JLabel userPasswordConfirmStarLabel = new JLabel("*");
		userPasswordConfirmStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userPasswordConfirmStarLabel.setForeground(Color.RED);
		userPasswordConfirmStarLabel.setBounds(itemPosition_X - starLabelWidth, itemPosition_Y + adjustInterval + 2 * itemInterval, starLabelWidth, 13);
		userPasswordConfirmStarLabel.setFont(UIFont.MESSAGE_FONT);
		contentPanel.add(userPasswordConfirmStarLabel);
		
		JLabel telephoneStarLabel = new JLabel("*");
		telephoneStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		telephoneStarLabel.setForeground(Color.RED);
		telephoneStarLabel.setBounds(itemPosition_X - starLabelWidth, itemPosition_Y + adjustInterval + 3 * itemInterval, starLabelWidth, 13);
		telephoneStarLabel.setFont(UIFont.MESSAGE_FONT);
		contentPanel.add(telephoneStarLabel);
		
		JLabel identifyCodeStarLabel = new JLabel("*");
		identifyCodeStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		identifyCodeStarLabel.setForeground(Color.RED);
		identifyCodeStarLabel.setBounds(itemPosition_X - starLabelWidth, itemPosition_Y + adjustInterval + 4 * itemInterval, starLabelWidth, 13);
		identifyCodeStarLabel.setFont(UIFont.MESSAGE_FONT);
		contentPanel.add(identifyCodeStarLabel);
		
		JLabel identifyKeyStarLabel = new JLabel("*");
		identifyKeyStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		identifyKeyStarLabel.setForeground(Color.RED);
		identifyKeyStarLabel.setBounds(itemPosition_X - starLabelWidth, itemPosition_Y + adjustInterval + 5 * itemInterval, starLabelWidth, 13);
		identifyKeyStarLabel.setFont(UIFont.MESSAGE_FONT);
		contentPanel.add(identifyKeyStarLabel);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == memberTypeJComboBox) {
			if (arg0.getStateChange() == ItemEvent.SELECTED) {
				userRoleSelected = arg0.getItem().toString();
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String userRole = userRoleSelected;
		String userCode = userCodeTextField.getText().trim();
		String password = String.valueOf(userPasswordField.getPassword()).trim();
		String passwordConfirm = String.valueOf(userPasswordConfirmField.getPassword()).trim();
		String telephone = telephoneTextField.getText().trim();
		String identityCode = identifyCodeTextField.getText().trim();
		String identityKey = identifyKeyTextField.getText().trim();
		int identityFunction = 1;
		boolean infoValidCheck = true;
		boolean checkExist = false;
		boolean multipleRecords = false;
		List<Object[]> auditInfoList = null;
		AuditBean auditBean = null;
		//初始验证用户账号
		if (null != userCode && !userCode.equals("") && !userCodeTextFieldMessage.equals(userCode)) {
			Object[] objects = {userCode};
			auditInfoList = userServiceOperation.query(objects, 2, 0, ConfigMessage.DEFAULT_DATABASE_TYPE);
			operationResultCode = userServiceOperation.getOperationCode();
			if (null != auditInfoList && !auditInfoList.isEmpty()) {
				if (auditInfoList.size() == 1) {
					checkExist = true;
					auditBean = new AuditBean(
							(int) ((Object[]) auditInfoList.get(0))[0], 
							(String)((Object[]) auditInfoList.get(0))[1], 
							(String)((Object[]) auditInfoList.get(0))[2], 
							(int)((Object[]) auditInfoList.get(0))[3], 
							String.valueOf(((Object[]) auditInfoList.get(0))[4]));
				}
				else {
					multipleRecords = true;
					logger.error("Get more than one audit info in function<UserRegisterJDialog:actionPerformed>, please check it!");
				}
			}
			else {
				logger.info("User Code does not exist, please check it!");
			}
		}
		//获取验证码功能验证
		if (arg0.getSource() == identifyCodeButton) {
			//验证用户角色
			infoValidCheck = checkUserRole(userRole);
			//验证用户账号
			if (infoValidCheck) {
				infoValidCheck = checkUserCode(userRole, userCode, checkExist, auditBean, multipleRecords);
			}
			//验证手机号码
			if (infoValidCheck) {
				infoValidCheck = checkTelephone(telephone, userCode, identityFunction);
			}
		}
		//提交按钮
		else if (arg0.getSource() == infoConfirmButton) {
			//验证用户角色
			infoValidCheck = checkUserRole(userRole);
			//验证用户账号
			if (infoValidCheck) {
				infoValidCheck = checkUserCode(userRole, userCode, checkExist, auditBean, multipleRecords);
			}
			//验证密码
			if (infoValidCheck) {
				infoValidCheck = checkPassword(password);
			}
			//验证确认密码
			if (infoValidCheck) {
				infoValidCheck = checkPassword(password, passwordConfirm);
			}
			//验证手机号码
			if (infoValidCheck) {
				infoValidCheck = checkTelephone(telephone);
			}
			//验证验证码
			if (infoValidCheck) {
				infoValidCheck = checkIdentityCode(identityCode, userCode, telephone, identityFunction);
			}
			//验证备案校验码
			if (infoValidCheck) {
				infoValidCheck = checkIdentityKey(identityKey, auditBean);
			}
			//更新用户基础信息
			if (infoValidCheck) {
				Object[] insertUserGeneralInfoObjects = generateUserGeneralInfo(userCode, telephone, userRole, passwordConfirm);
				infoValidCheck = insertInfo(insertUserGeneralInfoObjects);
			}
			//更新用户验证结果信息
			if (infoValidCheck) {
				Object[] updateIdentityStatusObjects = {1, userCode, telephone, identityFunction};
				infoValidCheck = updateInfo(updateIdentityStatusObjects);
			}
			//返回用户注册结果代码
			if (infoValidCheck) {
				operationResult = 0;
				JOptionPane jOptionPane = new JOptionPane("3秒后自动关闭",JOptionPane.INFORMATION_MESSAGE);  
		        final JDialog jDialog = jOptionPane.createDialog("用户账号注册成功!");
		        Timer timer = new Timer();
		        timer.schedule(new TimerTask() {  
		            public void run() {  
		            	jDialog.setVisible(false);  
		            	jDialog.dispose();  
		            }  
		        }, 3000);
		        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  
		        jDialog.setAlwaysOnTop(true);  
		        jDialog.setModal(false);  
		        jDialog.setVisible(true);
			}
			closeCurrentWindow();
		}
	}

	public boolean checkUserRole(String userRole) {
		boolean infoValidCheck = true;
		if (null != userRole && !userRole.equals("")) {
			if (!userRole.equals(LocalizeConfig.USER_ROLE_CUSTOMER) && !userRole.equals(LocalizeConfig.USER_ROLE_MEMBER) && !userRole.equals(LocalizeConfig.USER_ROLE_REGULATOR)) {
				infoValidCheck = false;
				JOptionPane.showMessageDialog(null, "请更新软件或重新配置软件信息!", "用户角色不存在", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "请选择合适的用户角色进行注册!", "用户角色选取错误", JOptionPane.ERROR_MESSAGE);
		}
		return infoValidCheck;
	}
	
	public boolean checkUserCode(String userRole, String userCode, boolean checkExist, AuditBean auditBean, boolean multipleRecords) {
		boolean infoValidCheck = true;
		if ((null != userCode && !userCode.equals("")) && !userCodeTextFieldMessage.equals(userCode)) {
			//用户账号有效
			if (checkExist && auditBean != null) {
				if (!UtilTool.isMatchedUserRole(userRole, auditBean.getUser_role())) {
					infoValidCheck = false;
					JOptionPane.showMessageDialog(null, "请检查与用户账号对应的角色是否正确!", "用户角色选取错误", JOptionPane.ERROR_MESSAGE);
				}
			}
			//用户账号无效
			else {
				infoValidCheck = false;
				if (operationResultCode == 1 || multipleRecords) {
					JOptionPane.showMessageDialog(null, "请确认输入的用户账号是否已提交监管机构备案!", "用户账号未备案", JOptionPane.ERROR_MESSAGE);
				}
				else if (operationResultCode == 10) {
					JOptionPane.showMessageDialog(null, "数据查询参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
				}
				else if (operationResultCode == 11 || operationResultCode == 12) {
					JOptionPane.showMessageDialog(null, "数据查询异常，请确认网络及系统配置信息是否正确!", "数据查询异常", JOptionPane.ERROR_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "用户账号不能为空, 请重新输入!", "用户账号错误", JOptionPane.ERROR_MESSAGE);
		}
		return infoValidCheck;
	}
	
	public boolean checkPassword(String userPassword) {
		boolean infoValidCheck = true;
		if (null != userPassword && !userPassword.equals("") && !passwordFieldMessage.equals(userPassword)) {
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
		if (null != userPasswordConfirm && !userPasswordConfirm.equals("") && !passwordConfirmFieldMessage.equals(userPasswordConfirm)) {
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
	
	public boolean checkTelephone(String telephone) {
		boolean infoValidCheck = true;
		if (null != telephone && !telephone.equals("") && !telephoneTextFieldMessage.equals(telephone)) {
			//验证手机号码格式是否正确
			if (!UtilTool.isChinaPhoneLegal(telephone)) {
				infoValidCheck = false;
				JOptionPane.showMessageDialog(null, "用户手机号无效, 请重新输入!");
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "用户手机号不能为空, 请重新输入!");
		}
		return infoValidCheck;
	}
	
	public boolean checkTelephone(String telephone, String userCode, int identityFunction) {
		boolean infoValidCheck = true;
		String userID = null;
		int identityStatus = -1;
		int queryDetailResultCode = -1, queryKeyResultCode = -1;
		IdentityBean identityBean = null;
		if (null != telephone && !telephone.equals("") && !telephoneTextFieldMessage.equals(telephone)) {
			if (null != userCode && !userCode.equals("") && !userCodeTextFieldMessage.equals(userCode) && identityFunction > 0) {
				//验证手机号码格式是否正确
				if (UtilTool.isChinaPhoneLegal(telephone)) {
					Object[] objects = {userCode, identityFunction};
					List<Object[]> identityList = userServiceOperation.query(objects, 3, 0, ConfigMessage.DEFAULT_DATABASE_TYPE);
					queryDetailResultCode = userServiceOperation.getOperationCode();
					//通过用户账号查询认证信息
					if (null != identityList && !identityList.isEmpty()) {
						if (identityList.size() == 1) {
							identityBean = new IdentityBean(
									(int) ((Object[]) identityList.get(0))[0], 
									(String) ((Object[]) identityList.get(0))[1], 
									(String) ((Object[]) identityList.get(0))[2], 
									(int) ((Object[]) identityList.get(0))[3], 
									String.valueOf(((Object[]) identityList.get(0))[4]), 
									(int) ((Object[]) identityList.get(0))[5]);
						}
						else {
							infoValidCheck = false;
							logger.info("Get more than one full Identity info by user_id, please check it!");
						}
						//check identity result
						if (infoValidCheck && identityBean != null) {
							//用户账号已被成功激活
							if (identityBean.getIdentity_status() == 1) {
								infoValidCheck = false;
								if (telephone.equals(identityBean.getUser_telephone())) {
									JOptionPane.showMessageDialog(null, "用户账号已激活, 无需再次验证手机号激活!");
								}
								else {
									JOptionPane.showMessageDialog(null, "用户账号已被手机号[" + identityBean.getUser_telephone() + "]成功激活, 如需更换手机号码，请至个人信息-手机号更换!");
								}
							}
							else {
								//调用发送验证码接口
								int randomIdentityCode = UtilTool.generateRandomIdentityCode();
								String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
								if (randomIdentityCode >= 0 && (null != currentUTCTimeStamp && !currentUTCTimeStamp.equals(""))) {
									SendSmsResponse response = IdentityCodeMessage.sendSms(telephone, ConfigMessage.SHORT_MESSAGE_MODEL_USER_GEGISTER, randomIdentityCode);
									if (null != response && !response.getCode().equals("")) {
										if ("OK".equals(response.getCode())) {
											int updateResultCode = -1;
											Object[] updateObjects = {telephone, randomIdentityCode, currentUTCTimeStamp, 0, userCode, identityFunction};
											//update identity info
											boolean updateResult = userServiceOperation.updateInfo(updateObjects, 3, 0, ConfigMessage.DEFAULT_DATABASE_TYPE);
											updateResultCode = userServiceOperation.getOperationCode();
											if (updateResult) {
												new Thread(new TimeThread()).start();
												logger.info("Identity info update successfully!");
											}
											else {
												infoValidCheck = false;
												if (updateResultCode == 3) {
													JOptionPane.showMessageDialog(null, "数据更新失败，请确认网络及系统配置信息是否正确!", "数据更新失败", JOptionPane.ERROR_MESSAGE);
												}
												else if (updateResultCode == 10) {
													JOptionPane.showMessageDialog(null, "数据更新参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
												}
												else if (updateResultCode == 11) {
													JOptionPane.showMessageDialog(null, "数据查询异常，请确认网络及系统配置信息是否正确!", "数据查询异常", JOptionPane.ERROR_MESSAGE);
												}
												else {
													JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
												}
											}
										}
										else {
											infoValidCheck = false;
											JOptionPane.showMessageDialog(null, "验证码获取失败, 请联系监管机构处理!");
										}
									}
									//网络不通
									else {
										infoValidCheck = false;
										JOptionPane.showMessageDialog(null, "验证码获取失败, 请检查网络是否畅通!");
									}
								}
								else {
									infoValidCheck = false;
									logger.error("Generate Identity Code failed, please check it!");
								}
							}
						}
					}
					//用户账号尚未经过认证
					else {
						//通过用户账号查询无结果
						if (queryDetailResultCode == 1) {
							//验证手机号码是否已经被使用
							List<Object[]> identityInfoList = userServiceOperation.query(objects, 3, 1, ConfigMessage.DEFAULT_DATABASE_TYPE);
							queryKeyResultCode = userServiceOperation.getOperationCode();
							if (null != identityInfoList && !identityInfoList.isEmpty()) {
								if (identityInfoList.size() == 1) {
									userID = (String) ((Object[]) identityInfoList.get(0))[0];
									identityStatus = (int) ((Object[]) identityInfoList.get(0))[1];
									if (null != userID && !userID.equals("")) {
										//手机号码已被其他用户使用
										if (!userID.equals(userCode)) {
											//号码被使用，但是未被激活，即未成功绑定
											if (identityStatus !=1) {
												//调用发送验证码接口
												int randomIdentityCode = UtilTool.generateRandomIdentityCode();
												String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
												if (randomIdentityCode >= 0 && currentUTCTimeStamp != null && currentUTCTimeStamp.length() > 0) {
													SendSmsResponse response = IdentityCodeMessage.sendSms(telephone, ConfigMessage.SHORT_MESSAGE_MODEL_USER_GEGISTER, randomIdentityCode);
													if (null != response && !response.getCode().equals("")) {
														if ("OK".equals(response.getCode())) {
															int updateResultCode = -1;
															Object[] updateObjects = {telephone, randomIdentityCode, currentUTCTimeStamp, 0, userCode, identityFunction};
															//update identity info
															boolean updateResult = userServiceOperation.updateInfo(updateObjects, 3, 0, ConfigMessage.DEFAULT_DATABASE_TYPE);
															updateResultCode = userServiceOperation.getOperationCode();
															if (updateResult) {
																new Thread(new TimeThread()).start();
																logger.info("Identity info update successfully!");
															}
															else {
																infoValidCheck = false;
																if (updateResultCode == 3) {
																	JOptionPane.showMessageDialog(null, "数据更新失败，请确认网络及系统配置信息是否正确!", "数据更新失败", JOptionPane.ERROR_MESSAGE);
																}
																else if (updateResultCode == 10) {
																	JOptionPane.showMessageDialog(null, "数据更新参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
																}
																else {
																	JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
																}
															}
														}
														else {
															infoValidCheck = false;
															JOptionPane.showMessageDialog(null, "验证码获取失败, 请联系监管机构处理!");
														}
													}
													//网络不通
													else {
														infoValidCheck = false;
														JOptionPane.showMessageDialog(null, "验证码获取失败, 请检查网络是否畅通!");
													}
												}
												else {
													infoValidCheck = false;
													logger.error("Generate Identity Code failed, please check it!");
												}
											}
											else {
												infoValidCheck = false;
												JOptionPane.showMessageDialog(null, "用户手机号已被其他账户使用, 可通过申诉渠道处理或使用其它手机号码!");
											}
										}
										else {
											infoValidCheck = false;
											logger.info("Get Abnormal identity info by telephone, please check it!");
										}
									}
								}
								else {
									infoValidCheck = false;
									logger.info("Get more than one key Identity info by telephone, please check it!");
								}
							}
							//手机号码未被任何账号使用
							else {
								if (queryKeyResultCode == 1) {
									//调用发送验证码接口
									int randomIdentityCode = UtilTool.generateRandomIdentityCode();
									String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
									if (randomIdentityCode >= 0 && currentUTCTimeStamp != null && currentUTCTimeStamp.length() > 0) {
										SendSmsResponse response = IdentityCodeMessage.sendSms(telephone, ConfigMessage.SHORT_MESSAGE_MODEL_USER_GEGISTER, randomIdentityCode);
										if (null != response && !response.getCode().equals("")) {
											if ("OK".equals(response.getCode())) {
												//insert identity info into database
												int insertResultCode = -1;
												Object[] insertObjects = {null, userCode, telephone, randomIdentityCode, currentUTCTimeStamp, 0, identityFunction};
												boolean insertResult = userServiceOperation.insertInfo(insertObjects, 3, 0, ConfigMessage.DEFAULT_DATABASE_TYPE);
												insertResultCode = userServiceOperation.getOperationCode();
												if (insertResult) {
													new Thread(new TimeThread()).start();
													logger.info("Identity info update successfully!");
												}
												else {
													infoValidCheck = false;
													if (insertResultCode == 2) {
														JOptionPane.showMessageDialog(null, "数据插入失败，请确认网络及系统配置信息是否正确!", "数据插入失败", JOptionPane.ERROR_MESSAGE);
													}
													else if (insertResultCode == 10) {
														JOptionPane.showMessageDialog(null, "数据插入参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
													}
													else if (insertResultCode == 11) {
														JOptionPane.showMessageDialog(null, "数据插入异常，请确认网络及系统配置信息是否正确!", "数据插入异常", JOptionPane.ERROR_MESSAGE);
													}
													else {
														JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
													}
												}
											}
											else {
												infoValidCheck = false;
												JOptionPane.showMessageDialog(null, "验证码获取失败, 请联系监管机构处理!");
											}
										}
										//网络不通
										else {
											infoValidCheck = false;
											JOptionPane.showMessageDialog(null, "验证码获取失败, 请检查网络是否畅通!");
										}
									}
									else {
										infoValidCheck = false;
										logger.error("Generate Identity Code failed, please check it!");
									}
								}
								else if (queryKeyResultCode == 10) {
									infoValidCheck = false;
									JOptionPane.showMessageDialog(null, "数据查询参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
								}
								else if (queryKeyResultCode == 11 || queryKeyResultCode == 12) {
									infoValidCheck = false;
									JOptionPane.showMessageDialog(null, "数据查询异常，请确认网络及系统配置信息是否正确!", "数据查询异常", JOptionPane.ERROR_MESSAGE);
								}
								else {
									infoValidCheck = false;
									JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
						else if (queryDetailResultCode == 10) {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "数据查询参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
						}
						else if (queryDetailResultCode == 11 || queryDetailResultCode == 12) {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "数据查询异常，请确认网络及系统配置信息是否正确!", "数据查询异常", JOptionPane.ERROR_MESSAGE);
						}
						else {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				//手机号码格式不正确
				else {
					infoValidCheck = false;
					JOptionPane.showMessageDialog(null, "用户手机号无效, 请重新输入!");
				}
			}
			else {
				infoValidCheck = false;
				logger.error("用户账号不能为空, 请重新输入!");
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "用户手机号不能为空, 请重新输入!");
		}
		return infoValidCheck ;
	}
	
	public boolean checkIdentityCode(String identityCode, String userCode, String telephone, int identityFunction) {
		boolean infoValidCheck = true;
		if (null != identityCode && !identityCode.equals("") && !identityCodeTextFieldMessage.equals(identityCode)) {
			if ((null != userCode && !userCode.equals("") && !userCodeTextFieldMessage.equals(userCode)) && 
					(null != telephone && !telephone.equals("") && !telephoneTextFieldMessage.equals(telephone)) && 
					identityFunction > 0) {
				//校验验证码
				Object[] objects = {userCode, telephone, identityFunction};
				List<Object[]> identityList = userServiceOperation.query(objects, 3, 4, ConfigMessage.DEFAULT_DATABASE_TYPE);
				int queryResultCode = userServiceOperation.getOperationCode();
				//通过用户账号、手机号、功能代号查询认证信息
				if (null != identityList && !identityList.isEmpty()) {
					if (identityList.size() == 1) {
						int identityCodeDB = (int) ((Object[]) identityList.get(0))[0];
						String identityCodeTimeDB = String.valueOf(((Object[]) identityList.get(0))[1]);
						int identityStatusDB = (int) ((Object[]) identityList.get(0))[2];
						//校验用户账号是否激活(即成功绑定手机号)
						if (identityStatusDB != 1) {
							//校验验证码是否过期
							String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
							String validTimeStamp = ArithmeticUtil.add(identityCodeTimeDB, String.valueOf(ConfigMessage.IDENTITY_CODE_TIME_COUNT_DOWN * 60000)).toString();
							if (ArithmeticUtil.compareDetails(validTimeStamp, currentUTCTimeStamp) >= 0) {
								//校验验证码是否正确
								if (Integer.parseInt(identityCode) != identityCodeDB) {
									infoValidCheck = false;
									JOptionPane.showMessageDialog(null, "验证码错误，请重新输入!", "验证码错误", JOptionPane.ERROR_MESSAGE);
								}
							}
							else {
								infoValidCheck = false;
								JOptionPane.showMessageDialog(null, "验证码已失效，请重新获取!", "验证码失效", JOptionPane.ERROR_MESSAGE);
							}
						}
						else {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "用户账号已激活，无需重复注册!", "重复注册", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						infoValidCheck = false;
						logger.error("Get more than one Identity info by user_id, please check it!");
					}
				}
				else {
					infoValidCheck = false;
					if (queryResultCode == 1) {
						JOptionPane.showMessageDialog(null, "请先获取手机短信验证码!", "数据查询错误", JOptionPane.ERROR_MESSAGE);
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
			else {
				infoValidCheck = false;
				logger.error("用户账号或手机号码不能为空, 请重新输入!");
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "验证码不能为空, 请输入!");
		}
		return infoValidCheck;
	}
	
	public boolean checkIdentityKey(String identityKey, AuditBean auditBean) {
		boolean infoValidCheck = true;
		if (null != identityKey && !identityKey.equals("") && !identifyKeyTextFieldMessage.equals(identityKey)) {
			String cryptologyString = HashModule.sha256(identityKey, true);
			if (null != cryptologyString && !cryptologyString.equals("")) {
				if (!cryptologyString.equals(auditBean.getAudit_number())) {
					infoValidCheck = false;
					JOptionPane.showMessageDialog(null, "校验密钥错误, 请重新输入! 如已遗失, 请联系监管机构处理!");
				}
			}
			else {
				infoValidCheck = false;
				logger.error("Generate Hash info failed in function <HashModule:sha256>");
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "备案校验密钥不能为空, 请输入! 如已遗失, 请联系监管机构处理!");
		}
		return infoValidCheck;
	}
	
	public boolean updateInfo(Object[] updateInfoObjects) {
		boolean infoValidCheck = true;
		//更新用户账户验证结果
		boolean updateInfoResult = userServiceOperation.updateInfo(updateInfoObjects, 3, 1, ConfigMessage.DEFAULT_DATABASE_TYPE);
		int updateInfoResultCode = userServiceOperation.getOperationCode();
		if (updateInfoResult) {
			logger.info("Identity info update successfully!");
		}
		else {
			infoValidCheck = false;
			if (updateInfoResultCode == 3) {
				JOptionPane.showMessageDialog(null, "数据更新失败, 请确认网络及系统配置信息是否正确!", "数据更新失败", JOptionPane.ERROR_MESSAGE);
			}
			else if (updateInfoResultCode == 10) {
				JOptionPane.showMessageDialog(null, "数据更新参数错误, 请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
			}
			else if (updateInfoResultCode == 11) {
				JOptionPane.showMessageDialog(null, "数据库操作异常, 请初始化数据库配置信息!", "数据库操作异常", JOptionPane.ERROR_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(null, "未知错误, 请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
			}
		}
		return infoValidCheck;
	}
	
	public boolean insertInfo(Object[] insertInfoObjects) {
		boolean infoValidCheck = true;
		if (null != insertInfoObjects[0] && !insertInfoObjects[0].equals("")) {
			boolean insertInfoResult = userServiceOperation.insertInfo(insertInfoObjects, 0, 1, ConfigMessage.DEFAULT_DATABASE_TYPE);
			int insertInfoResultCode = userServiceOperation.getOperationCode();
			if (insertInfoResult) {
				logger.info("用户基础信息成功入库!");
			}
			else {
				infoValidCheck = false;
				if (insertInfoResultCode == 2) {
					JOptionPane.showMessageDialog(null, "用户注册失败, 请确认网络及系统配置信息是否正确!", "数据插入失败", JOptionPane.ERROR_MESSAGE);
					logger.error("用户基础信息插入失败, 请确认用户基础信息是否有效!");
				}
				else if (insertInfoResultCode == 10) {
					JOptionPane.showMessageDialog(null, "数据插入参数错误, 请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
					logger.error("用户基础信息入库参数错误, 请重新输入相应参数!");
				}
				else if (insertInfoResultCode == 11) {
					JOptionPane.showMessageDialog(null, "数据库操作异常, 请初始化数据库配置信息!", "数据库操作异常", JOptionPane.ERROR_MESSAGE);
					logger.error("数据插入异常，请确认网络及系统配置信息是否正确!");
				}
				else {
					JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
					logger.error("未知错误, 请更新软件或重新配置软件信息!");
				}
			}
		}
		return infoValidCheck;
	}
	
	public Object[] generateUserGeneralInfo(String userCode, String telephone, String userRole, String password) {
		Object[] insertUserGeneralInfoObjects = new Object[8];
		if ((null != userCode && !userCode.equals("")) && (null != telephone && !telephone.equals("")) && (null != userRole && !userRole.equals("")) && (null != password && !password.equals(""))) {
			//生成系统用户编码
			StringBuilder stringBuilder = new StringBuilder().append(userCode).append(telephone);
			String userSystemID = HashModule.sha256(stringBuilder.toString(), true);
			//生成加密密码
			String userPassword = HashModule.sha256(password, true);
			//生成用户角色转换码
			int userRoleDB = UtilTool.convertUserRole(userRole);
			//生成时间戳
			String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
			if ((null != userSystemID && !userSystemID.equals("")) && 
					(null != userPassword && !userPassword.equals("")) && 
					userRoleDB > 0 && 
					(null != currentUTCTimeStamp && !currentUTCTimeStamp.equals(""))) {
				//生成证书并获取证书路径
				String userCAPath = new StringBuilder().append("ca/").append(userSystemID).append(".cer").toString();
				insertUserGeneralInfoObjects[0] = userCode;
				insertUserGeneralInfoObjects[1] = userPassword;
				insertUserGeneralInfoObjects[2] = telephone;
				insertUserGeneralInfoObjects[3] = userRoleDB;
				insertUserGeneralInfoObjects[4] = userCAPath;
				insertUserGeneralInfoObjects[5] = 1;
				insertUserGeneralInfoObjects[6] = userSystemID;
				insertUserGeneralInfoObjects[7] = currentUTCTimeStamp;
			}
		}
		return insertUserGeneralInfoObjects;
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
		StringBuffer stringBuffer;
		if (arg0.getSource() == userCodeTextField) {
			stringBuffer = new StringBuffer(userCodeTextField.getText().toString().trim());
			if (stringBuffer != null && stringBuffer.length() > 0) {
				infoConfirmButton.setEnabled(true);
				infoConfirmButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
			}
			else {
				infoConfirmButton.setEnabled(false);
				infoConfirmButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		StringBuffer stringBuffer = null;
		if (arg0.getSource() == userCodeTextField) {
			stringBuffer = new StringBuffer(userCodeTextField.getText().toString().trim());
			int keyValue = arg0.getKeyChar();
			char entryChar = arg0.getKeyChar();
			if ((keyValue >= KeyEvent.VK_0 && keyValue <= KeyEvent.VK_9) || 
					(entryChar >= 'a' && entryChar <= 'z') || 
					(entryChar >= 'A' && entryChar <= 'Z')) {
			}
			else {
				arg0.consume();
			}
			if (stringBuffer != null && stringBuffer.length() > 0) {
				if (stringBuffer.length() >= 20) {
					arg0.consume();
				}
			}
		}
		else if (arg0.getSource() == userPasswordField) {
			stringBuffer = new StringBuffer(String.valueOf(userPasswordField.getPassword()).trim());
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
			stringBuffer = new StringBuffer(String.valueOf(userPasswordConfirmField.getPassword()).trim());
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
		else if (arg0.getSource() == telephoneTextField) {
			stringBuffer = new StringBuffer(telephoneTextField.getText().toString().trim());
			int keyValue = arg0.getKeyChar();
			if ((keyValue >= KeyEvent.VK_0 && keyValue <= KeyEvent.VK_9)) {
				
			}
			else {
				arg0.consume();
			}
			if (stringBuffer != null && stringBuffer.length() > 0) {
				if (keyValue == KeyEvent.VK_ENTER || keyValue == KeyEvent.VK_DELETE || keyValue == KeyEvent.VK_BACK_SPACE) {
					
				}else {
					if (stringBuffer.length() >= 11) {
						arg0.consume();
					}
				}
			}
		}
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
		else if (arg0.getSource() == identifyKeyTextField) {
			if (identifyKeyTextFieldMessage.equals(identifyKeyTextField.getText().trim())) {
				identifyKeyTextField.setText("");
//				userPasswordField.setEchoChar('*');
			}
		}
		else if (arg0.getSource() == userPasswordField) {
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
		else if (arg0.getSource() == telephoneTextField) {
			if (telephoneTextFieldMessage.equals(telephoneTextField.getText().trim())) {
				telephoneTextField.setText("");
			}
		}
		else if (arg0.getSource() == identifyCodeTextField) {
			if (identityCodeTextFieldMessage.equals(identifyCodeTextField.getText().trim())) {
				identifyCodeTextField.setText("");
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
			if ("".equals(userCodeTextField.getText().trim())) {
				userCodeTextField.setText(userCodeTextFieldMessage);
			}
		}
		else if (arg0.getSource() == identifyKeyTextField) {
			if ("".equals(identifyKeyTextField.getText().trim())) {
				identifyKeyTextField.setText(identifyKeyTextFieldMessage);
			}
		}
		else if (arg0.getSource() == userPasswordField) {
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
		else if (arg0.getSource() == telephoneTextField) {
			if ("".equals(telephoneTextField.getText().trim())) {
				telephoneTextField.setText(telephoneTextFieldMessage);
			}
		}
		else if (arg0.getSource() == identifyCodeTextField) {
			if ("".equals(identifyCodeTextField.getText().trim())) {
				identifyCodeTextField.setText(identityCodeTextFieldMessage);
			}
		}
	}
	
	class TimeThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (countDownTime > 1) {
				countDownTime --;
				identifyCodeButton.setEnabled(false);
				identifyCodeButton.setText(countDownTime + LocalizeConfig.RESENT_MESSAGE_TIME_UNIT);
				try {
					//间隔1s
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error("TimeThread exception!", e.getMessage());
				}
			}
			if (countDownTime == 1) {
				identifyCodeButton.setEnabled(true);
				identifyCodeButton.setText(LocalizeConfig.RESENT_MESSAGE);
				countDownTime = ConfigMessage.IDENTITY_CODE_RESENT_TIME_COUNT_DOWN;
			}
		}
	};
}
