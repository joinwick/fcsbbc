/**
 * 
 */
package com.fcsbbc.utils;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author luo.changshu
 *
 */
public class TimeCountDownTest extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton jButton = new JButton("获取验证码");
	private int countDownTime = 5;
	
	public TimeCountDownTest() {
		setSize(500, 300);
		setLayout(new FlowLayout());
		jButton.setFocusable(false);
		jButton.setFont(UIFont.Button_FONT);
		add(jButton);
		jButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new Thread(new TimeThread()).start();
			}
		});
		setVisible(true);
	}
	
	class TimeThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (countDownTime > 1) {
				countDownTime --;
				jButton.setEnabled(false);
				
				jButton.setText(countDownTime + LocalizeConfig.RESENT_MESSAGE_TIME_UNIT);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
			//当前窗口关闭
//			TimeCountDownTest.this.dispose();
			if (countDownTime == 1) {
				jButton.setEnabled(true);
				jButton.setText(LocalizeConfig.RESENT_MESSAGE);
				countDownTime = 5;
			}
		}
	};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TimeCountDownTest();
	}
}

