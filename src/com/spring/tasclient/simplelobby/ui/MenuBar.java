package com.spring.tasclient.simplelobby.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener {
	ConnectWindow mConnectWindow;
	
	public MenuBar(ConnectWindow cw) {
		mConnectWindow = cw;
		JMenu server = new JMenu("Simplelobby");
		JMenu options = new JMenu("Options");
		JMenu window = new JMenu("Window");
		JMenu look = new JMenu("Look and feel");
		JMenu help = new JMenu("Help");

		CreateMenuItem("Connect", server);
		CreateMenuItem("Exit", server);
		
		CreateMenuItem("Simplelobby", options);
		CreateMenuItem("Spring", options);
		
	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            CreateMenuItem(info.getName(), look);
		
		CreateMenuItem("About", help);
		CreateMenuItem("Legend", help);
		
		add(server);
		add(options);
		add(window);
		add(look);
		add(help);
	}
	
	private void CreateMenuItem(String name, JMenu parent) {
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(this);
		parent.add(item);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Exit")) {
			System.exit(0);
		} else
		if (e.getActionCommand().equals("Connect")) {
			mConnectWindow.SetActive(true);
			return;
		}
	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (e.getActionCommand().equals(info.getName())) {
            	try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
            	Frame frames[] = JFrame.getFrames();
				for (int i = 0; i < frames.length; i++)
		        	SwingUtilities.updateComponentTreeUI(frames[i]);
				break;
            }
        }
	}
}
