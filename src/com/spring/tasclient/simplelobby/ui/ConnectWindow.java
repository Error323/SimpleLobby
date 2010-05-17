package com.spring.tasclient.simplelobby.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.spring.tasclient.simplelobby.interfaces.IConnWinListener;

@SuppressWarnings("serial")
public class ConnectWindow extends JPanel implements ActionListener {
	private JButton mConnect, mRegister;
	private JTextField mName, mServer, mPort;
	private JPasswordField mPassword;
	private IConnWinListener mConnWinInterface;
	private JFrame mFrame;
	private JFrame mMain;
	
	public ConnectWindow(IConnWinListener iwl, JFrame main) {
		mMain = main;
		mConnWinInterface = iwl;
		mConnect = new JButton("Login");
		mRegister = new JButton("Register");
		mServer = new JTextField("taspringmaster.clan-sy.com");
		mName = new JTextField("Testaccount");
		mPort = new JTextField("8200");
		mPassword = new JPasswordField("test");
		int colums = 17;
		mServer.setColumns(colums);
		mName.setColumns(colums);
		mPort.setColumns(colums);
		mPassword.setColumns(colums);
		mConnect.addActionListener(this);
		mRegister.addActionListener(this);
		
		
		add(mServer);
		add(mPort);
		add(mName);
		add(mPassword);
		add(mConnect);
		add(mRegister);
		Dimension size = new Dimension(220, 200);
		setSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		mFrame = new JFrame("Connect");
		mFrame.add(this);
		mFrame.setPreferredSize(size);
		mFrame.pack();
		mFrame.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String password = String.valueOf(mPassword.getPassword());
		String username = mName.getText();
		String server = mServer.getText();
		int port = Integer.parseInt(mPort.getText());

		if (e.getActionCommand().equals("Login")) {
			mConnWinInterface.Login(server, port, username, password);
		} else
		if (e.getActionCommand().equals("Register")) {
			mConnWinInterface.Register(server, port, username, password);
		}
	}

	public void SetActive(boolean b) {
		if (b) {
		mFrame.setLocation(mMain.getLocation().x+mMain.getSize().width/2-getWidth()/2,
				mMain.getLocation().y+mMain.getSize().height/2-getHeight()/2);
		}
		mFrame.setVisible(b);
	}
}
