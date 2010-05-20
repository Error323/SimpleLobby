package com.spring.tasclient.simplelobby;

import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.spring.tasclient.simplelobby.interfaces.IConnWinListener;
import com.spring.tasclient.simplelobby.interfaces.IConnectionListener;
import com.spring.tasclient.simplelobby.ui.ChatWindow;
import com.spring.tasclient.simplelobby.ui.ConnectWindow;
import com.spring.tasclient.simplelobby.ui.MainWindow;
import com.spring.tasclient.simplelobby.ui.MenuBar;

public class SimpleLobby implements IConnectionListener, IConnWinListener {
	public static final String NAME       = "SimpleLobby";
	public static final String VERSION    = "1.0.0";
	public static final String HUMAN_NAME = NAME + " v" + VERSION;

	private String mUsername, mPassword;
	private boolean mLogin;
	
	private static ConnectWindow mConnectWin;
	private static ChatWindow mChatWin;
	
	public static void main(String[] args) {
		final SimpleLobby sl = new SimpleLobby();
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	// ActionListener must be registered in EDT
                createAndShowGUI(sl);
            }
        });
	}
	private static void createAndShowGUI(SimpleLobby sl) {
        JFrame root = new JFrame(HUMAN_NAME);
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mConnectWin = new ConnectWindow(sl, root);
        
        // Instantiate the controlling class.
        MainWindow mainWin = new MainWindow(root);
        root.getContentPane().add(mainWin);
		mChatWin = new ChatWindow();
		mChatWin.LinkHandler(mChatHandler);
		mainWin.AddDockable("Chat", mChatWin);
		
        // Display the window.
        root.setVisible(true);
        root.setSize(1024, 768);
		root.setLocation(100, 100);
		
        MenuBar menu = new MenuBar(mConnectWin);
        root.setJMenuBar(menu);        
	}
	
	private TASConnection mConn;
	private UserHandler mUserHandler;
	private static BattleHandler mBattleHandler;	
	private static ChatHandler mChatHandler;
	
	private String mAgreement;
	
	public SimpleLobby() {
		mConn = new TASConnection();
		mBattleHandler = new BattleHandler(mConn);
		mUserHandler = new UserHandler(mConn);
		mChatHandler = new ChatHandler(mConn, mUserHandler);
		mUserHandler.AttachChatUserInterface(mChatHandler);
//		mUserHandler.AttachBattleUserInterface(mBattleHandler);
		mConn.AttachUserHandlerInterface(mUserHandler);
		mConn.AttachChatInterface(mChatHandler);
		mConn.AttachConnectionInterface(this);
//		mUnitSync.Init(false, 0);
	}

	@Override
	public void Agreement(String agreement) {
		mAgreement += agreement;
	}
	
	@Override
	public void AgreementEnd() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	JEditorPane jep = new JEditorPane("text/rtf", mAgreement);
            	JScrollPane jsp = new JScrollPane(jep);
            	jsp.setPreferredSize(new Dimension(400,300));
            	if (JOptionPane.showConfirmDialog(null, jsp, "User Agreement", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            		mConn.ConfirmAgreement();
            		mConn.Login(mUsername, mPassword);
            	}
            	else {
            		mConn.Disconnect();
            	}
            	mAgreement = "";
            }
        });
	}

	@Override
	public void Connected(String serverVersion, String springVersion,
			String udpport, String servermode) {
		if (mLogin)
			mConn.Login(mUsername, mPassword);
		else
			mConn.Register(mUsername, mPassword);
	}

	@Override
	public void Disconnected(String reason) {
		MsgBox("Disconnected", reason, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void Login(String server, int port, String username, String password) {
		mLogin = true;
		mUsername = username;
		mPassword = password;
		if (mConn.IsConnected())
			mConn.Disconnect();
		mConn.Connect(server, port);
	}

	@Override
	public void LoginFailed(String reason) {
		MsgBox("Login Failed", reason, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void LoginSucceeded(String username) {
		mConnectWin.SetActive(false);
		mConn.Join("main", "");
		mConn.Join("xta", "");
		mConn.Join("springlobby", "");
	}

	public void MsgBox(final String title, final String msg, final int option) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		JOptionPane.showMessageDialog(null, msg, title, option);
            }
        });
	}

	@Override
	public void Pong(long ping) {
	}

	@Override
	public void Register(String server, int port, String username,
			String password) {
		mLogin = false;
		mUsername = username;
		mPassword = password;
		if (mConn.IsConnected())
			mConn.Disconnect();		
		mConn.Connect(server, port);
	}

	@Override
	public void RegistrationAccepted() {
		mConn.Login(mUsername, mPassword);
	}

	@Override
	public void RegistrationDenied(String reason) {
		MsgBox("Registration Failed", reason, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void ServerMsgBox(String msg, String url) {
		MsgBox("Server Message", msg, JOptionPane.INFORMATION_MESSAGE);
	}
}
