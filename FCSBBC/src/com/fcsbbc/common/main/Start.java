/**
 * 
 */
package com.fcsbbc.common.main;

import javax.swing.UIManager;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.gui.PeerLoginJFrame;

/**
 * @author luo.changshu
 *
 */
public class Start {
	private static Logger logger = LoggerFactory.getLogger(Start.class.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 设置窗口边框样式
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("System Start Failed!", e.getMessage());
		}
		UIManager.put("RootPane.setupButtonVisible", false);
		// 初始化登陆窗口
		new PeerLoginJFrame();
	}
}
