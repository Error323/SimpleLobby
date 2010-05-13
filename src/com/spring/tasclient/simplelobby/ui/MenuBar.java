package com.spring.tasclient.simplelobby.ui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	private ActionListener listener;
	
	public MenuBar(ActionListener listener) {
		this.listener = listener;
		
		JMenu server = new JMenu("Server");
		JMenu options = new JMenu("Options");
		JMenu look = new JMenu("Look and feel");
		JMenu help = new JMenu("Help");

		CreateMenuItem("Connect", server);
		CreateMenuItem("Disconnect", server);
		
		CreateMenuItem("Simplelobby", options);
		CreateMenuItem("Spring", options);
		
		CreateMenuItem("Java", look);
		CreateMenuItem("System", look);
		
		CreateMenuItem("About", help);
		
		add(server);
		add(options);
		add(look);
		add(help);
	}
	
	private void CreateMenuItem(String name, JMenu parent) {
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(listener);
		parent.add(item);
	}
}
