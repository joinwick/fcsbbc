package com.fcsbbc.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 创建图像变量
		Image image;
	// 构造函数
	public ImagePanel(Image image) {
		this.image = image;// 初始化图像变量
		// 获取当前屏幕宽高
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setSize(width, height);
	}
	// 绘制组件
	public void paintComponent(Graphics g) {
		// 继承父类方法
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
