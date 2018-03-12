/**
 * 
 */
package com.fcsbbc.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.fcsbbc.common.domain.UserBaseBean;

/**
 * @author luo.changshu
 *
 */
public class UserDetailJPanel {

	JPanel backgroundPanel;
	UserBaseBean userBaseBean;
	public UserDetailJPanel(UserBaseBean userBaseBean) {
		this.userBaseBean = userBaseBean;
		backgroundPanel = new JPanel(new BorderLayout());
//		initTopPanel();
//		initTablePanel();
	}
}
