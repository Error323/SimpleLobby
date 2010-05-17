package com.spring.tasclient.simplelobby;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.spring.tasclient.simplelobby.interfaces.IBattleListener;
import com.spring.tasclient.simplelobby.interfaces.IChatListener;
import com.spring.tasclient.simplelobby.interfaces.IConnectionListener;
import com.spring.tasclient.simplelobby.interfaces.IUserHandlerListener;

public class TASConnection extends AConnection {
	private static final int PING_INTERVAL = 5000;
	
	private long mPingTime;
	private long mStart;
	private boolean mPongReceived;
	
	private IChatListener mChatListener;
	private IConnectionListener mConnListener;
	private IUserHandlerListener mUserHandlerListener;
	private IBattleListener mBattleListener;
	
	public TASConnection() {
		super();
		mStart = System.currentTimeMillis();
	}
	
	public void AttachChatInterface(IChatListener chatListener) {
		mChatListener = chatListener;
	}
	
	public void AttachConnectionInterface(IConnectionListener connListener) {
		mConnListener = connListener;
	}
	
	public void AttachUserHandlerInterface(IUserHandlerListener userListener) {
		mUserHandlerListener = userListener;
	}
	
	public void AttachBattleListener(IBattleListener battleListener) {
		mBattleListener = battleListener;
	}
	
	public void ChannelTopic(String channel, String topic) {
		
	}
	
	@Override
	protected void Connecting() {
		mStart = System.currentTimeMillis();
		mPongReceived = true;
	}
	
	public void ForceLeaveChannel(String channel, String username, String reason) {
		
	}
	
	public long GetPing() {
		return mPingTime;
	}

	private int GetStartIdx(String[] data, int N) {
		int idx = 0;
		for (int i = 0; i < N && i < data.length; i++)
			idx += data[i].length()+1;
		return idx;
	}
	
	public void Join(String channel, String password) {
		if (password.length() == 0)
			Send("JOIN " + channel);
		else
			Send("JOIN " + channel + " " + password);
	}
	
	public void Leave(String channel) {
		Send("LEAVE " + channel);
	}

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public void Login(String username, String password) {
		Send("LOGIN "
			+ username + " "
			+ md5(password) + " "
			+ 0 + " "
			+ mConnection.getLocalAddress().getHostName() + " "
			+ SimpleLobby.NAME + " "
			+ SimpleLobby.VERSION);
	}

	/**
	 * 
	 * @param password
	 * @return md5hash
	 */
	private String md5(String password) {
		try {
			MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			return Base64.encodeBase64String(md5.digest()).trim();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void MyBattleStatus(String mMyUserName, int battlestatus) {
		Send("MYBATTLESTATUS " + battlestatus);
	}

	public void MyStatus(String mMyUserName, int status) {
		Send("MYSTATUS " + status);
	}

	@Override
	protected void NetworkError(Exception e) {
		mConnListener.Disconnected(e.getMessage());
	}
	
	@Override
	protected void NetworkTimeout(Exception e) {
		mConnListener.Disconnected("Network Timeout");
	}

	@Override
	protected void Received(String data) {
		System.out.println(data);
		String splitted[] = data.split(" ");
		String cmd = splitted[0];
		
		// User related commands
		if (mUserHandlerListener != null) {
		if (cmd.equals("CLIENTSTATUS")) {
			mUserHandlerListener.ClientStatus(splitted[1], Integer.parseInt(splitted[2]));
		} else
		if (cmd.equals("CLIENTBATTLESTATUS")) {
			mUserHandlerListener.ClientBattleStatus(splitted[1], Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
		} else
		if (cmd.equals("ADDUSER")) {
			mUserHandlerListener.AddUser(splitted[1], splitted[2], splitted[3]);
		} else
		if (cmd.equals("REMOVEUSER")) {
			mUserHandlerListener.RemoveUser(splitted[1]);
		}
		}
			
		// Chat related commands
		if (cmd.equals("CLIENTS")) {
			mChatListener.Clients(splitted[1], data.substring(GetStartIdx(splitted, 2)));
		}
		if (cmd.equals("SAID")) {
			mChatListener.Said(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
		} else
		if (cmd.equals("SAIDPRIVATE")) {
			mChatListener.SaidPrivate(splitted[1], splitted[2]);
		} else
		if (cmd.equals("SERVERMSG")) {
			mChatListener.ServerMsg(splitted[1]);
		} else
		if (cmd.equals("CHANNELMSG")) {
			mChatListener.ChannelMsg(splitted[1], data.substring(GetStartIdx(splitted,2)));
		} else
		if (cmd.equals("FORCELEAVECHANNEL")) {
			mChatListener.ForceLeaveChannel(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
		} else
		if (cmd.equals("LEFT")) {
			if (splitted.length == 4)
				mChatListener.Left(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
			else
				mChatListener.Left(splitted[1], splitted[2], "");
		} else
		if (cmd.equals("JOINED")) {
			mChatListener.Joined(splitted[1], splitted[2]);
		} else
		if (cmd.equals("JOIN")) {
			mChatListener.JoinSucceeded(splitted[1]);
		} else
		if (cmd.equals("JOINFAILED")) {
			mChatListener.JoinFailed(splitted[1], data.substring(GetStartIdx(splitted, 2)));
		} else
		if (cmd.equals("SAIDEX")) {
			mChatListener.SaidEx(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
		}
		if (cmd.equals("CHANNELTOPIC")) {
			mChatListener.ChannelTopic(splitted[1], splitted[2], splitted[3], data.substring(GetStartIdx(splitted,4)));
		} else
		if (cmd.equals("MOTD")) {
			if (data.length() == 4)
				mChatListener.Motd("");
			else
				mChatListener.Motd(data.substring(5));
		} else
		
		// Battle related commands
		if (mBattleListener != null) {
		if (cmd.equals("BATTLEOPENED")) {
			mBattleListener.BattleOpened(splitted[1], splitted[2], splitted[3], splitted[4], splitted[5], splitted[6], splitted[7], splitted[8], splitted[9], splitted[10], splitted[11], splitted[12], splitted[13]);
		} else
		if (cmd.equals("BATTLECLOSED")) {
			mBattleListener.BattleClosed(splitted[1]);
		} else
		if (cmd.equals("JOINBATTLE")) {
			mBattleListener.JoinBattle(splitted[1]);
		} else
		if (cmd.equals("JOINBATTLEFAILED")) {
			mBattleListener.JoinBattleFailed(data.substring(GetStartIdx(splitted, 0)));
		} else
		if (cmd.equals("JOINEDBATTLE")) {
			mBattleListener.JoinedBattle(splitted[1], splitted[2]);
		} else
		if (cmd.equals("LEFTBATTLE")) {
			mBattleListener.LeftBattle(splitted[1], splitted[2]);
		} else
		if (cmd.equals("UPDATEBATTLEINFO")) {
			mBattleListener.UpdateBattleInfo(splitted[1], splitted[2], splitted[3], splitted[4], splitted[5]);
		}
		}
		
		if (mConnListener == null) {
		// Connection related commands
		if (cmd.equals("TASServer")) {
			mConnListener.Connected(splitted[1], splitted[2], splitted[3], splitted[4]);
		} else
		if (cmd.equals("ACCEPTED")) {
			mConnListener.LoginSucceeded(splitted[1]);
		} else
		if (cmd.equals("DENIED")) {
			mConnListener.LoginFailed(data.substring(GetStartIdx(splitted, 1)));
		} else
		if (cmd.equals("SERVERMSGBOX")) {
			mConnListener.ServerMsgBox(data.substring(GetStartIdx(splitted, 1)), "");
		} else
		if (cmd.equals("PONG")) {
			mPongReceived = true;
			mPingTime = System.currentTimeMillis() - mStart;
			mConnListener.Pong(mPingTime);
		} else
		if (cmd.equals("AGREEMENT")) {
			mConnListener.Agreement(data.substring(GetStartIdx(splitted, 1)));
		} else
		if (cmd.equals("AGREEMENTEND")) {
			mConnListener.AgreementEnd();
		} else
		if (cmd.equals("REGISTRATIONDENIED")) {
			mConnListener.RegistrationDenied(data.substring(GetStartIdx(splitted, 1)));
		} else
		if (cmd.equals("REGISTRATIONACCEPTED")) {
			mConnListener.RegistrationAccepted();
		}
		}
	}

	public void Say(String channel, String msg) {
		Send("SAY " + channel + " " + msg);
	}

	public void SayEx(String channel, String msg) {
		Send("SAYEX " + channel + " " + msg);
	}

	public void SayPrivate(String username, String msg) {
		
	}

	@Override
	protected void UnknownHost(Exception e) {
		mConnListener.Disconnected("Unknown Host");
	}

	@Override
	protected void Update() {
		long stop = System.currentTimeMillis();
		long timeout = stop - mStart;
		
		if (timeout > CONNECTION_TIME_OUT) {
			mConnListener.Disconnected("Network Timeout");
			Disconnect();
		} else
		if (mPongReceived && timeout > PING_INTERVAL) {
			Send("PING");
			mPongReceived = false;
			mStart = stop;
		}		
	}

	public void Register(String username, String password) {
		Send("REGISTER " + username + " " + md5(password));
	}
	
	public void ConfirmAgreement() {
		Send("CONFIRMAGREEMENT");
	}
}