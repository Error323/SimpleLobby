package com.spring.tasclient.simplelobby.ui;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ConnectWindow extends JPanel {
	private JButton mConnect, mRegister;
	private JTextField mName, mServer, mPort;
	private JPasswordField mPassword;
	private JLabel mStatus;
	
	public ConnectWindow(ActionListener listener) {
		mConnect = new JButton("Login");
		mRegister = new JButton("Register");
		mServer = new JTextField("taspringmaster.clan-sy.com");
		mName = new JTextField("[CoW]Error323");
		mPort = new JTextField("8200");
		mPassword = new JPasswordField("edamcm23");
		Dimension d = new Dimension(200, 20);
		mServer.setPreferredSize(d);
		mPort.setPreferredSize(d);
		mName.setPreferredSize(d);
		mPassword.setPreferredSize(d);
		mConnect.setActionCommand("Serverconnect");
		mConnect.addActionListener(listener);
		mRegister.setActionCommand("Serverregister");
		mRegister.addActionListener(listener);
		mStatus = new JLabel("Disconnected");
		
		
		add(mServer);
		add(mPort);
		add(mName);
		add(mPassword);
		add(mConnect);
		add(mRegister);
		add(mStatus);
		setSize(220, 180);
	}
	
	public void SetStatus(String status) {
		mStatus.setText(status);
	}

	public String GetUsername() {
		return mName.getText();
	}

	public String GetPassword() {
		return String.valueOf(mPassword.getPassword());
	}

	public int GetPort() {
		return Integer.parseInt(mPort.getText());
	}

	public String GetServer() {
		return mServer.getText();
	}
}
