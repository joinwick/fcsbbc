package com.fcsbbc.gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.common.domain.UserBaseBean;
import com.fcsbbc.common.service.UserServiceOperation;
import com.fcsbbc.utils.UIFont;

public class PeerLoginFrame extends JFrame implements MouseListener, FocusListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int dataBaseType = 1;
	private static Logger logger = LoggerFactory.getLogger(PeerLoginFrame.class.getName());
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	private static final int actualWidth = (int) (0.5 * width), actualHeight = (int) (0.6 * height);
	// 全局的位置变量，用于表示鼠标在窗口上的位置
	static Point origin = new Point();

	// 定义全局组件
	JTextField userCodeTextField = new JTextField(18);
	JPasswordField userPasswordField = new JPasswordField(64);
	JTextField userCAPath = new JTextField();
	ImagePanel backgroundPanel = null;
	JButton button_minimize, button_close, loginButton, resetButton, fileChooseButton;
	
	public PeerLoginFrame() {
		//	窗口淡入淡出
		new WindowFade(this);
		Image uiBackgroundImage = null;
		try {
			uiBackgroundImage = ImageIO.read(new File("image/loginbackground.png"));
			Image image = ImageIO.read(new File("image/logo.png"));
			this.setIconImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error("Image File not exist in <PeerLoginFrame:PeerLoginFrame>", e.getMessage());
		}
		//	窗口背景面板
		backgroundPanel = new ImagePanel(uiBackgroundImage);
		backgroundPanel.setLayout(null);
		System.out.println(width + ":" + height);
//		userCodeTextField.setBounds(378, 202, 173, 30);
		userCodeTextField.setBounds((int) (0.45 * actualWidth), (int) (0.35 * actualHeight), (int) (0.20 * actualWidth), 30);
		userCodeTextField.setFont(UIFont.DEFAULT_FONT);
		userCodeTextField.addFocusListener(this);
		userCodeTextField.setText("用户账号");

//		userPasswordField.setBounds(378, 240, 173, 30);
		userPasswordField.setBounds((int) (0.45 * actualWidth), (int) (0.41 * actualHeight), (int) (0.20 * actualWidth), 30);
		userPasswordField.setFont(UIFont.DEFAULT_FONT);
		userPasswordField.addFocusListener(this);
		userPasswordField.setText("密码");
		userPasswordField.setEchoChar('\0');
		
		userCAPath.setBounds((int) (0.45 * actualWidth), (int) (0.47 * actualHeight), (int) (0.20 * actualWidth), 30);
		userCAPath.setFont(UIFont.DEFAULT_FONT);
		userCAPath.setText("证书路径");
		userCAPath.addFocusListener(this);
		
		fileChooseButton = new JButton("浏览...");
		fileChooseButton.setBounds((int) (0.65 * actualWidth), (int) (0.47 * actualHeight), (int) (0.08 * actualWidth), 27);
		fileChooseButton.setFont(UIFont.DEFAULT_FONT);
		fileChooseButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		fileChooseButton.setForeground(Color.white);
		fileChooseButton.addMouseListener(this);
		
		loginButton = new JButton("登录");
//		loginButton.setBounds(380, 280, 70, 27);
		loginButton.setBounds((int) (0.47 * actualWidth), (int) (0.53 * actualHeight), (int) (0.16 * actualWidth), 30);
		loginButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		loginButton.setForeground(Color.white);
		loginButton.setFont(UIFont.DEFAULT_FONT);
		loginButton.addMouseListener(this);

//		resetButton = new JButton("重置");
//		resetButton.setBounds(480, 280, 70, 27);
//		resetButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
//		resetButton.setForeground(Color.white);
//		resetButton.setFont(UIFont.DEFAULT_FONT);
//		resetButton.addMouseListener(this);

		backgroundPanel.add(userCodeTextField);
		backgroundPanel.add(userPasswordField);
		backgroundPanel.add(userCAPath);
		backgroundPanel.add(fileChooseButton);
		backgroundPanel.add(loginButton);
//		backgroundPanel.add(resetButton);

		this.add(backgroundPanel);
		this.setTitle("基于区块链的期货结算系统");
//		this.setSize(830, 530);
		this.setBounds((width - actualWidth) / 2, (height - actualHeight) / 2, actualWidth, actualHeight);
		this.setVisible(true);
		this.requestFocus();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userCodeTextField) {
			if (userCodeTextField.getText().equals("用户账号")) {
				userCodeTextField.setText("");
			}
		}
		else if (arg0.getSource() == userPasswordField) {
			if (String.valueOf(userPasswordField.getPassword()).equals("密码")) {
				userPasswordField.setText("");
				userPasswordField.setEchoChar('*');
			}
		}
	}
	
	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == userCodeTextField) {
			if (userCodeTextField.getText().equals("")) {
				userCodeTextField.setText("用户账号");
			}
		}
		else if (arg0.getSource() == userPasswordField) {
			if (String.valueOf(userPasswordField.getPassword()).equals("")) {
				userPasswordField.setText("密码");
				userPasswordField.setEchoChar('\0');
			}
		}
	}

	//	处理鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == loginButton) {
			if ("用户账号".equals(userCodeTextField.getText())) {
				JOptionPane.showMessageDialog(null, "用户账号不能为空");
			} 
			else if ("密码".equals(String.valueOf(userPasswordField.getPassword()))) {
				JOptionPane.showMessageDialog(null, "用户密码不能为空");
			} 
			else {
				String userInfo[] = {String.valueOf(userPasswordField.getPassword()), userCodeTextField.getText()};
				UserServiceOperation userServiceOperation = new UserServiceOperation();
				int queryResult = userServiceOperation.queryVerify(userInfo, 0, 1, dataBaseType);
				if (queryResult == 5) {
					JOptionPane.showMessageDialog(null, "用户账号不存在,请重新输入");
				}
				else if (queryResult == 4) {
					JOptionPane.showMessageDialog(null, "用户密码有误，请重新输入");
					userPasswordField.setText("密码");
					userPasswordField.setEchoChar('\0');
				}
				else if (queryResult == 2) {
					JOptionPane.showMessageDialog(null, "用户账号已被注销，如需激活，请联系监管机构");
				}
				else if (queryResult == 1) {
					UserBaseBean userBaseBean = (UserBaseBean) userServiceOperation.queryBean(userInfo, 0, 1, dataBaseType);
					this.setVisible(false);
					new IndexJFrame(userBaseBean);
				}
			}
		} 
		else if (arg0.getSource() == resetButton) {
			userCodeTextField.setText("用户账号");
			userPasswordField.setText("密码");
			userPasswordField.setEchoChar('\0');
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
