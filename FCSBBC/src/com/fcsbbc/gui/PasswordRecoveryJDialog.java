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
import java.io.File;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.common.service.UserServiceOperation;
import com.fcsbbc.utils.ArithmeticUtil;
import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.IdentityCodeMessage;
import com.fcsbbc.utils.LocalizeConfig;
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
public class PasswordRecoveryJDialog extends JDialog implements FocusListener, KeyListener, ActionListener{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(UserRegisterJDialog.class.getName());
	private final JPanel contentPanel = new JPanel();
	private static final int itemPosition_X = 60, itemPosition_Y = 85, itemInterval = 35, adjustInterval = 3, stepPosition_X = 110, stepPosition_Y = 30;
	private static final int labelWidth = 60, filedWidth = 170, buttonWidth = 100, starLabelWidth = 13, commonComponentWidth = 90;
	private static final int actualWidth = 500, actualHeight = 400;
	private final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	private final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	private final static String userCodeTextFieldMessage = "身份证号或社会统一信用码";
	private final static String userCATextFieldMessage = "证书";
	public int operationResult = -2;
	private int countDownTime = ConfigMessage.IDENTITY_CODE_RESENT_TIME_COUNT_DOWN;
	private JTextField userCodeTextField, userCATextField, telephoneTextField, identifyCodeTextField;
	private JButton userCAScanButton, identifyCodeButton, nextStepButton;
	private int userPasswordResetResult = -2;
	UserServiceOperation userServiceOperation = new UserServiceOperation();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PasswordRecoveryJDialog dialog = new PasswordRecoveryJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int passwordRecoveryResult() {
		return operationResult;
	}
	
	public void closeCurrentWindow() {
		int operationResult = passwordRecoveryResult();
		if (operationResult == 0 || operationResult == 1) {
			this.dispose();
		}
		else {
			System.out.println("重置密码失败");
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public PasswordRecoveryJDialog() {
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
		stepOneLabel.setFont(UIFont.MESSAGE_FONT);
		stepOneLabel.setForeground(Color.blue);
		stepOneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stepOneLabel.setBounds(stepPosition_X, stepPosition_Y, commonComponentWidth, 15);
		contentPanel.add(stepOneLabel);
		
		JLabel stepInterval = new JLabel("-----");
		stepInterval.setFont(UIFont.MESSAGE_FONT);
		stepInterval.setBounds(stepPosition_X + commonComponentWidth + 10, stepPosition_Y, 30, 15);
		contentPanel.add(stepInterval);
		
		JLabel stepTwoLabel = new JLabel("步骤2:重置密码");
		stepTwoLabel.setEnabled(false);
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
		userCodeTextField.setText(userCodeTextFieldMessage);
		userCodeTextField.setFont(UIFont.MESSAGE_FONT);
		userCodeTextField.setHorizontalAlignment(SwingConstants.LEFT);
		userCodeTextField.setBounds(itemPosition_X + starLabelWidth + labelWidth + 1, itemPosition_Y - 2 * adjustInterval, filedWidth, 21);
		userCodeTextField.addFocusListener(this);
		userCodeTextField.addKeyListener(this);
		contentPanel.add(userCodeTextField);
		
		JLabel userCAStarLabel = new JLabel("*");
		userCAStarLabel.setForeground(Color.RED);
		userCAStarLabel.setFont(UIFont.MESSAGE_FONT);
		userCAStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userCAStarLabel.setBounds(itemPosition_X, itemPosition_Y + itemInterval, starLabelWidth, 13);
		contentPanel.add(userCAStarLabel);
		
		JLabel userCALabel = new JLabel("用户证书:");
		userCALabel.setFont(UIFont.DEFAULT_FONT);
		userCALabel.setHorizontalAlignment(SwingConstants.LEFT);
		userCALabel.setBounds(itemPosition_X + starLabelWidth, itemPosition_Y + itemInterval - adjustInterval, labelWidth, 15);
		contentPanel.add(userCALabel);
		
		userCATextField = new JTextField();
		userCATextField.setText(userCATextFieldMessage);
		userCATextField.setFont(UIFont.MESSAGE_FONT);
		userCATextField.setHorizontalAlignment(SwingConstants.LEFT);
		userCATextField.setBounds(itemPosition_X + starLabelWidth + labelWidth + 1, itemPosition_Y + itemInterval - 2 * adjustInterval, filedWidth, 21);
		userCATextField.addFocusListener(this);
		userCATextField.addKeyListener(this);
		contentPanel.add(userCATextField);
		
		userCAScanButton = new JButton("浏览...");
		userCAScanButton.setHorizontalAlignment(SwingConstants.LEFT);
		userCAScanButton.setBounds(itemPosition_X + starLabelWidth + labelWidth + filedWidth + 1, itemPosition_Y + itemInterval - 2 * adjustInterval - 1, buttonWidth, 23);
		userCAScanButton.setFocusPainted(false);
		userCAScanButton.setFont(UIFont.DEFAULT_FONT);
		userCAScanButton.setToolTipText("请选择账户对应的CA证书!");
		userCAScanButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		userCAScanButton.addActionListener(this);
		contentPanel.add(userCAScanButton);
		
		JLabel telephoneStarLabel = new JLabel("*");
		telephoneStarLabel.setForeground(Color.RED);
		telephoneStarLabel.setFont(UIFont.MESSAGE_FONT);
		telephoneStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		telephoneStarLabel.setBounds(itemPosition_X, itemPosition_Y + 2 * itemInterval, starLabelWidth, 13);
		contentPanel.add(telephoneStarLabel);
		
		JLabel telephoneLabel = new JLabel("手机号:");
		telephoneLabel.setFont(UIFont.DEFAULT_FONT);
		telephoneLabel.setHorizontalAlignment(SwingConstants.LEFT);
		telephoneLabel.setBounds(itemPosition_X + starLabelWidth, itemPosition_Y + 2 * itemInterval - adjustInterval, labelWidth, 15);
		contentPanel.add(telephoneLabel);
		
		telephoneTextField = new JTextField();
		telephoneTextField.setFont(UIFont.MESSAGE_FONT);
		telephoneTextField.setHorizontalAlignment(SwingConstants.LEFT);
		telephoneTextField.setBounds(itemPosition_X + starLabelWidth + labelWidth + 1, itemPosition_Y + 2 * itemInterval - 2 * adjustInterval, filedWidth, 21);
		telephoneTextField.addKeyListener(this);
		contentPanel.add(telephoneTextField);
		
		identifyCodeButton = new JButton("获取验证码");
		identifyCodeButton.setHorizontalAlignment(SwingConstants.LEFT);
		identifyCodeButton.setFocusPainted(false);
		identifyCodeButton.setFont(UIFont.DEFAULT_FONT);
		identifyCodeButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		identifyCodeButton.setBounds(itemPosition_X + starLabelWidth + labelWidth + filedWidth + 1, itemPosition_Y + 2 * itemInterval - 2 * adjustInterval - 1, buttonWidth, 23);
		identifyCodeButton.addActionListener(this);
		contentPanel.add(identifyCodeButton);
		
		JLabel identifyCodeStarLabel = new JLabel("*");
		identifyCodeStarLabel.setForeground(Color.RED);
		identifyCodeStarLabel.setFont(UIFont.MESSAGE_FONT);
		identifyCodeStarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		identifyCodeStarLabel.setBounds(itemPosition_X, itemPosition_Y + 3 * itemInterval, starLabelWidth, 13);
		contentPanel.add(identifyCodeStarLabel);
		
		JLabel identifyCodeLabel = new JLabel("验证码:");
		identifyCodeLabel.setFont(UIFont.DEFAULT_FONT);
		identifyCodeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		identifyCodeLabel.setBounds(itemPosition_X + starLabelWidth, itemPosition_Y + 3 * itemInterval - adjustInterval, labelWidth, 15);
		contentPanel.add(identifyCodeLabel);
		
		identifyCodeTextField = new JTextField();
		identifyCodeTextField.setFont(UIFont.MESSAGE_FONT);
		identifyCodeTextField.setHorizontalAlignment(SwingConstants.LEFT);
		identifyCodeTextField.setBounds(itemPosition_X + starLabelWidth + labelWidth + 1, itemPosition_Y + 3 * itemInterval - 2 * adjustInterval, filedWidth, 21);
		identifyCodeTextField.addKeyListener(this);
		contentPanel.add(identifyCodeTextField);
		
		nextStepButton = new JButton("下一步");
		nextStepButton.setEnabled(false);
		nextStepButton.setFocusPainted(false);
		nextStepButton.setFont(UIFont.Button_FONT);
		nextStepButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		nextStepButton.setBounds(itemPosition_X + starLabelWidth + labelWidth + labelWidth / 2, itemPosition_Y + 4 * itemInterval, commonComponentWidth, 30);
		nextStepButton.addActionListener(this);
		contentPanel.add(nextStepButton);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String userCode = userCodeTextField.getText().trim();
		String caFilePath = userCATextField.getText().trim();
		String telephone = telephoneTextField.getText().trim();
		String identityCode = identifyCodeTextField.getText().trim();
		boolean infoValidCheck = true;
		boolean checkExist = false;
		boolean multipleRecords = false;
		List<Object[]> userGeneralInfoList = null;
		UserBaseBean userBaseBean = null;
		int operationResultCode = -1;
		int identityFunction = 2;
		if (userCode != null && userCode.length() > 0 && !userCodeTextFieldMessage.equals(userCode)) {
			Object[] objects = {userCode};
			userGeneralInfoList = userServiceOperation.query(objects, 0, 4, 1);
			operationResultCode = userServiceOperation.getOperationCode();
			if (userGeneralInfoList != null && !userGeneralInfoList.isEmpty() && userGeneralInfoList.size() > 0) {
				if (userGeneralInfoList.size() == 1) {
					checkExist = true;
					//user_id, user_role, user_telephone, user_ca_path, is_active, user_system_id
					userBaseBean = new UserBaseBean(
							(String)((Object[]) userGeneralInfoList.get(0))[0], 
							(String)((Object[]) userGeneralInfoList.get(0))[1], 
							(int)((Object[]) userGeneralInfoList.get(0))[2], 
							(String)((Object[]) userGeneralInfoList.get(0))[3], 
							(int)((Object[]) userGeneralInfoList.get(0))[4], 
							(String)((Object[]) userGeneralInfoList.get(0))[5]);
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
		//证书文件
		if (arg0.getSource() == userCAScanButton) {
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
		//验证码
		else if (arg0.getSource() == identifyCodeButton) {
			//验证用户账号
			infoValidCheck = checkUserCode(userCode, checkExist, userBaseBean, operationResultCode, multipleRecords);
			//验证手机号
			if (infoValidCheck) {
				infoValidCheck = checkTelephone(userCode, identityFunction, telephone, userBaseBean, operationResultCode, multipleRecords, true);
			}
		}
		else if (arg0.getSource() == nextStepButton) {
			//验证用户账号
			infoValidCheck = checkUserCode(userCode, checkExist, userBaseBean, operationResultCode, multipleRecords);
			//验证证书文件
			if (infoValidCheck) {
				infoValidCheck = checkCAFile(caFilePath);
			}
			//验证手机号
			if (infoValidCheck) {
				infoValidCheck = checkTelephone(userCode, identityFunction, telephone, userBaseBean, operationResultCode, multipleRecords, false);
			}
			//验证验证码
			if (infoValidCheck) {
				infoValidCheck = checkIdentityCode(identityCode, userCode, telephone, identityFunction);
			}
			//信息校验成功
			if (infoValidCheck) {
				PasswordResetJDialog passwordReset = new PasswordResetJDialog(userCode);
				passwordReset.setModal(true);
				passwordReset.setVisible(true);
				userPasswordResetResult = ((PasswordResetJDialog)passwordReset).userPasswordResetResult();
				if (userPasswordResetResult == 0) {
					operationResult = 0;
					closeCurrentWindow();
				}
			}
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
		StringBuffer stringBuffer;
		if (arg0.getSource() == userCodeTextField) {
			stringBuffer = new StringBuffer(userCodeTextField.getText().trim());
			if (stringBuffer != null && stringBuffer.length() > 0 && !userCodeTextFieldMessage.equals(stringBuffer.toString())) {
				nextStepButton.setEnabled(true);
				nextStepButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
			}
			else {
				nextStepButton.setEnabled(false);
				nextStepButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
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
			//	limit entry length
			if (stringBuffer != null && stringBuffer.length() > 0 && userCodeTextFieldMessage.equals(stringBuffer.toString())) {
				if (stringBuffer.length() >= 20) {
					arg0.consume();
				}
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
					
				}
				else {
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
		else if (arg0.getSource() == userCATextField) {
			if (userCATextFieldMessage.equals(userCATextField.getText().trim())) {
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
			if ("".equals(userCodeTextField.getText().trim())) {
				userCodeTextField.setText(userCodeTextFieldMessage);
			}
		}
		else if (arg0.getSource() == userCATextField) {
			if ("".equals(userCATextField.getText().trim())) {
				userCATextField.setText(userCATextFieldMessage);
			}
		}
	}
	
	public boolean checkUserCode(String userCode, boolean checkExist, UserBaseBean userBaseBean, int operationResultCode, boolean multipleRecords) {
		boolean infoValidCheck = true;
		if ((userCode != null && userCode.length() > 0) && !userCodeTextFieldMessage.equals(userCode)) {
			//用户账号有效
			if (checkExist && userBaseBean != null) {
				if (userBaseBean.getIs_active() != 1) {
					infoValidCheck = false;
					JOptionPane.showMessageDialog(null, "用户账号失活, 请联系监管机构激活用户账号!", "用户账号失活", JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				infoValidCheck = false;
				if (operationResultCode == 1 || multipleRecords) {
					JOptionPane.showMessageDialog(null, "用户账号不存在, 请联系监管机构确认用户账号是否已注册并激活!", "用户账号不存在", JOptionPane.ERROR_MESSAGE);
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
	
	public boolean checkTelephone(String userCode, int identityFunction, String telephone, UserBaseBean userBaseBean, int operationResultCode, boolean multipleRecords, boolean verifyProcess) {
		boolean infoValidCheck = true;
		//验证手机号码
		if (telephone != null && telephone.length() >0) {
			//验证手机号码格式是否正确
			if (UtilTool.isChinaPhoneLegal(telephone)) {
				if (userBaseBean != null) {
					String telePhoneDB = userBaseBean.getUser_telephone();
					if (telePhoneDB != null && telePhoneDB.length() > 0) {
						if (telephone.equals(telePhoneDB)) {
							//发送验证码
							if (verifyProcess) {
								Object[] identityInfoObjects = {userCode, identityFunction};
								List<Object[]> identityInfoList = userServiceOperation.query(identityInfoObjects, 3, 0, 1);
								int queryIdentityInfoResultCode = userServiceOperation.getOperationCode();
								if (identityInfoList != null && identityInfoList.size() > 0) {
									if (identityInfoList.size() == 1) {
//										IdentityBean identityBean = new IdentityBean(
//												(int) ((Object[]) identityInfoList.get(0))[0], 
//												(String) ((Object[]) identityInfoList.get(0))[1], 
//												(String) ((Object[]) identityInfoList.get(0))[2], 
//												(int) ((Object[]) identityInfoList.get(0))[3], 
//												String.valueOf(((Object[]) identityInfoList.get(0))[4]), 
//												(int) ((Object[]) identityInfoList.get(0))[5]);
										//调用发送验证码接口
										int randomIdentityCode = UtilTool.generateRandomIdentityCode();
										String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
										if (randomIdentityCode >= 0 && currentUTCTimeStamp != null && currentUTCTimeStamp.length() > 0) {
											SendSmsResponse response = IdentityCodeMessage.sendSms(telephone, ConfigMessage.SHORT_MESSAGE_MODEL_PASSWORD_RECOVERY, randomIdentityCode);
											if (null != response && !response.getCode().equals("")) {
												if ("OK".equals(response.getCode())) {
													int updateResultCode = -1;
													Object[] updateObjects = {telephone, randomIdentityCode, currentUTCTimeStamp, 0, userCode, identityFunction};
													//update identity info
													boolean updateResult = userServiceOperation.updateInfo(updateObjects, 3, 0, 1);
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
									}
									else {
										infoValidCheck = false;
										logger.info("Get more than one full Identity info by user_id, please check it!");
									}
								}
								//找回密码功能从未使用
								else {
									if (queryIdentityInfoResultCode == 1) {
										//调用发送验证码接口
										int randomIdentityCode = UtilTool.generateRandomIdentityCode();
										String currentUTCTimeStamp = String.valueOf(UtilTool.getUTCTimeStamp());
										if (randomIdentityCode >= 0 && currentUTCTimeStamp != null && currentUTCTimeStamp.length() > 0) {
											SendSmsResponse response = IdentityCodeMessage.sendSms(telephone, ConfigMessage.SHORT_MESSAGE_MODEL_PASSWORD_RECOVERY, randomIdentityCode);
											if (null != response && !response.getCode().equals("")) {
												if ("OK".equals(response.getCode())) {
													//insert identity info into database
													int insertResultCode = -1;
													Object[] insertObjects = {null, userCode, telephone, randomIdentityCode, currentUTCTimeStamp, 0, identityFunction};
													boolean insertResult = userServiceOperation.insertInfo(insertObjects, 3, 0, 1);
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
									else if (queryIdentityInfoResultCode == 10) {
										infoValidCheck = false;
										JOptionPane.showMessageDialog(null, "数据查询参数错误，请升级软件至最新版本!", "参数输入错误", JOptionPane.ERROR_MESSAGE);
									}
									else if (queryIdentityInfoResultCode == 11 || queryIdentityInfoResultCode == 12) {
										infoValidCheck = false;
										JOptionPane.showMessageDialog(null, "数据查询异常，请确认网络及系统配置信息是否正确!", "数据查询异常", JOptionPane.ERROR_MESSAGE);
									}
									else {
										infoValidCheck = false;
										JOptionPane.showMessageDialog(null, "未知错误，请更新软件或重新配置软件信息!", "未知错误", JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						}
						else {
							infoValidCheck = false;
							JOptionPane.showMessageDialog(null, "与用户账号预留的手机号码不符, 请重新输入!", "手机号校验错误", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						infoValidCheck = false;
						Object[] operation ={"是", "否"};
						int operationChoice = JOptionPane.showOptionDialog(null, "用户账号未绑定有效手机号码, 是否进行手机号码密保认证?", "用户账号无手机密保认证",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, operation, operation[0]);
						if (operationChoice == 0) {
							operationResult = 1;
						}
					}
				}
				else {
					infoValidCheck = false;
					if (operationResultCode == 1 || multipleRecords) {
						JOptionPane.showMessageDialog(null, "用户账号不存在, 请联系监管机构确认用户账号是否已注册并激活!", "用户账号不存在", JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "用户手机号无效, 请重新输入!");
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "用户手机号不能为空, 请重新输入!");
		}
		return infoValidCheck;
	}
	
	public boolean checkCAFile(String caFilePath) {
		boolean infoValidCheck = true;
		if (caFilePath != null && caFilePath.length() > 0 && !userCATextFieldMessage.equals(caFilePath)) {
			int fileCheckResult = UtilTool.fileValidCheck(caFilePath);
			if (fileCheckResult == 0) {
				if ("cer".equals(UtilTool.getFileSuffix(caFilePath))) {
					//验证证书文件是否有效
					
				}
				else {
					infoValidCheck = false;
					JOptionPane.showMessageDialog(null, "您选择或输入的用户证书文件格式不正确, 请重新选择或输入!");
				}
			}
			else {
				infoValidCheck = false;
				if (fileCheckResult == 1) {
					JOptionPane.showMessageDialog(null, "您选择或输入的用户证书文件不存在, 请重新选择或输入!");
				}
				else if (fileCheckResult == 2) {
					JOptionPane.showMessageDialog(null, "请选择或输入用户账户对应的证书文件, 而非文件目录!");
				}
				else {
					JOptionPane.showMessageDialog(null, "请选择或输入用户账户对应的证书文件!");
				}
			}
		}
		else {
			infoValidCheck = false;
			JOptionPane.showMessageDialog(null, "用户证书不能为空, 请重新输入或选择证书文件!");
		}
		return infoValidCheck;
	}
	
	public boolean checkIdentityCode(String identityCode, String userCode, String telephone, int identityFunction) {
		boolean infoValidCheck = true;
		if (identityCode != null && identityCode.length() > 0) {
			//校验验证码
			Object[] objects = {userCode, telephone, identityFunction};
			List<Object[]> identityList = userServiceOperation.query(objects, 3, 4, 1);
			int queryResultCode = userServiceOperation.getOperationCode();
			//通过用户账号、手机号、功能代码查询认证信息
			if (identityList != null && identityList.size() > 0) {
				if (identityList.size() == 1) {
					int identityCodeDB = (int) ((Object[]) identityList.get(0))[0];
					String identityCodeTimeDB = String.valueOf(((Object[]) identityList.get(0))[1]);
					int identityStatusDB = (int) ((Object[]) identityList.get(0))[2];
					//校验该验证码是否已用过重置密码
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
							JOptionPane.showMessageDialog(null, "该验证码已失效，请重新获取短信验证码!", "验证码失效", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						infoValidCheck = false;
						JOptionPane.showMessageDialog(null, "该验证码已失效, 请重新获取短信验证码!", "验证码失效", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					infoValidCheck = false;
					logger.error("Get more than one Identity info by user_id, telephone, identityFunction, please check it!");
				}
			}
			else {
				infoValidCheck = false;
				if (queryResultCode == 1) {
					JOptionPane.showMessageDialog(null, "无验证码信息, 请先获取短信验证码!", "无验证码信息", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "验证码不能为空, 请输入!");
		}
		return infoValidCheck;
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
