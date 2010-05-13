package com.spring.tasclient.simplelobby.ui;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar {
	private JMenu server;
	private JMenu options;
	private JMenu help;
	
	
	public Menu() {
		server = new JMenu("Server");
		server.setMnemonic(KeyEvent.VK_A);
		server.getAccessibleContext().setAccessibleDescription(
				"Server related actions");
		JMenuItem serverItem = new JMenuItem("xyz");
		server.add(serverItem);
		
		options = new JMenu("Options");
		help = new JMenu("Help");
		add(server);
		add(options);
		add(help);
	}
}
