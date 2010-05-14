package com.spring.tasclient.simplelobby;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.spring.tasclient.simplelobby.ui.ConnectWindow;
import com.spring.tasclient.simplelobby.ui.MainWindow;
import com.spring.tasclient.simplelobby.ui.MenuBar;


public class SimpleLobby implements ActionListener, IConnectionListener {
	public static final String NAME    = "SimpleLobby";
	public static final String VERSION = "1.0.0";

	private static JFrame mPopup;
	private static JFrame mRoot;
	private static ConnectWindow mConnectWin;
	
	private TASConnection mConn;

	public SimpleLobby() {
		mConn = new TASConnection();
		mConn.AttachConnectionInterface(this);
	}
	
	private static void createAndShowGUI(SimpleLobby sl) {
        mRoot = new JFrame(NAME + " v" + VERSION);
        mRoot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        MenuBar menu = new MenuBar(sl);
        mRoot.setJMenuBar(menu);
        
        //Instantiate the controlling class.
        MainWindow main = new MainWindow(mRoot);
        mRoot.getContentPane().add(main);
        
        //Display the window.
        mRoot.setVisible(true);
        mRoot.setSize(1024, 768);
		mRoot.setLocation(100+1920, 100);
        
        //Popup window
		mPopup = new JFrame();
		mPopup.setVisible(false);
		
		mConnectWin = new ConnectWindow(sl);
	}
	
	public static void main(String[] args) {
		final SimpleLobby sl = new SimpleLobby();
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	// ActionListener must be registered in EDT
                createAndShowGUI(sl);
            }
        });
	}
	
	private void ActivatePopup(JComponent c) {
		mPopup.add(c);
		mPopup.setLocation(mRoot.getLocation().x+mRoot.getSize().width/2-c.getWidth()/2,
				mRoot.getLocation().y+mRoot.getSize().height/2-c.getHeight()/2);
		mPopup.setSize(c.getSize());
		mPopup.setVisible(true);		
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
		if (e.getActionCommand().equals("Connect")) {
			ActivatePopup(mConnectWin);
		} else
		if (e.getActionCommand().equals("Serverconnect")) {
			if (!mConn.IsConnected()) {
				String server = mConnectWin.GetServer();
				int port = mConnectWin.GetPort();
				mConn.Connect(server, port);
			}
			else {
				String username = mConnectWin.GetUsername();
				String password = mConnectWin.GetPassword();
				mConn.Login(username, password);
			}
		} else
		if (e.getActionCommand().equals("System")) {
	        try {
	        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        	SwingUtilities.updateComponentTreeUI(mRoot);
	        	SwingUtilities.updateComponentTreeUI(mPopup);
	        } catch (Exception e1) {
	        	e1.printStackTrace();
	        }
		} else
		if (e.getActionCommand().equals("Java")) {
	        try {
	        	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	        	SwingUtilities.updateComponentTreeUI(mRoot);
	        	SwingUtilities.updateComponentTreeUI(mPopup);
	        } catch (Exception e1) {
	        	e1.printStackTrace();
	        }
		}
	}

	public void Connected(String serverVersion, String springVersion,
			String udpport, String servermode) {
		mConnectWin.SetStatus("Connected");
		String username = mConnectWin.GetUsername();
		String password = mConnectWin.GetPassword();
		mConn.Login(username, password);
	}

	public void Disconnected(String reason) {
		System.err.println("Disconnected: " + reason);
		mConnectWin.SetStatus(reason);
		JOptionPane.showMessageDialog(null, reason, "Disconnected", JOptionPane.WARNING_MESSAGE);
	}

	public void LoginFailed(String reason) {
		mConnectWin.SetStatus(reason);
	}

	public void LoginSucceeded(String username) {
		mConnectWin.SetStatus("Logged in as " + username);
		mPopup.setVisible(false);
	}

	public void RegistrationAccepted() {
		
	}

	public void RegistrationDenied(String reason) {
		
	}

	public void ServerMsgBox(String msg, String url) {
		
	}

	@Override
	public void Pong(long ping) {
		System.out.println("ping: " + ping);
	}
}
