/**
 * 
 */
package com.fcsbbc.gui;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.fcsbbc.utils.UIFont;

/**
 * @author luo.changshu
 *
 */
public class JMenuItemDefine {
	JMenuItem menuItem;
	public JMenuItemDefine(String name, String mnemonic, Icon icon, KeyStroke keyStroke) {
		menuItem = new JMenuItem(name, icon);
        menuItem.setFont(UIFont.DEFAULT_FONT);
        menuItem.setHorizontalAlignment(SwingConstants.LEFT);
        if (mnemonic != null)
            menuItem.setMnemonic(mnemonic.toCharArray()[0]);
        if (keyStroke != null)
            menuItem.setAccelerator(keyStroke);
	}
}
