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
public class SystemUIJPanel {

	JPanel backgroundPanel;
	UserBaseBean userBaseBean;
	public SystemUIJPanel(UserBaseBean userBaseBean) {
		this.userBaseBean = userBaseBean;
		backgroundPanel = new JPanel(new BorderLayout());
//		initTopPanel();
//		initTablePanel();
	}
}