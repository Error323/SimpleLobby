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
		String sentences[] = data.split("\t");
		String words[] = sentences[0].split(" ");
		String cmd = words[0];
		
		// User related commands
		if (cmd.equals("CLIENTSTATUS")) {
			mUserHandlerListener.ClientStatus(words[1], Integer.parseInt(words[2]));
		} else
		if (cmd.equals("CLIENTBATTLESTATUS")) {
			mUserHandlerListener.ClientBattleStatus(words[1], Integer.parseInt(words[2]), Integer.parseInt(words[3]));
		} else
		if (cmd.equals("ADDUSER")) {
			mUserHandlerListener.AddUser(words[1], words[2], words[3]);
		} else
		if (cmd.equals("REMOVEUSER")) {
			mUserHandlerListener.RemoveUser(words[1]);
		} else
			
		// Chat related commands
		if (cmd.equals("CLIENTS")) {
			mChatListener.Clients(words[1], data.substring(GetStartIdx(words, 2)));
		}
		if (cmd.equals("SAID")) {
			mChatListener.Said(words[1], words[2], data.substring(GetStartIdx(words,3)));
		} else
		if (cmd.equals("SAIDPRIVATE")) {
			mChatListener.SaidPrivate(words[1], words[2]);
		} else
		if (cmd.equals("SERVERMSG")) {
			mChatListener.ServerMsg(words[1]);
		} else
		if (cmd.equals("CHANNELMSG")) {
			mChatListener.ChannelMsg(words[1], data.substring(GetStartIdx(words,2)));
		} else
		if (cmd.equals("FORCELEAVECHANNEL")) {
			mChatListener.ForceLeaveChannel(words[1], words[2], data.substring(GetStartIdx(words,3)));
		} else
		if (cmd.equals("LEFT")) {
			if (words.length == 4)
				mChatListener.Left(words[1], words[2], data.substring(GetStartIdx(words,3)));
			else
				mChatListener.Left(words[1], words[2], "");
		} else
		if (cmd.equals("JOINED")) {
			mChatListener.Joined(words[1], words[2]);
		} else
		if (cmd.equals("JOIN")) {
			mChatListener.JoinSucceeded(words[1]);
		} else
		if (cmd.equals("JOINFAILED")) {
			mChatListener.JoinFailed(words[1], data.substring(GetStartIdx(words, 2)));
		} else
		if (cmd.equals("SAIDEX")) {
			mChatListener.SaidEx(words[1], words[2], data.substring(GetStartIdx(words,3)));
		}
		if (cmd.equals("CHANNELTOPIC")) {
			mChatListener.ChannelTopic(words[1], words[2], words[3], data.substring(GetStartIdx(words,4)));
		} else
		if (cmd.equals("MOTD")) {
			if (data.length() == 4)
				mChatListener.Motd("");
			else
				mChatListener.Motd(data.substring(5));
		} else
		
		// Battle related commands
		if (cmd.equals("BATTLEOPENED")) {
			switch (sentences.length) {
			case 1: mBattleListener.BattleOpened(words[1], words[2], words[3], 
					words[4], words[5], words[6], words[7], words[8], words[9], 
					words[10], "", "", ""); 
			break;
			case 2: mBattleListener.BattleOpened(words[1], words[2], words[3], 
					words[4], words[5], words[6], words[7], words[8], words[9], 
					words[10], sentences[1], "", ""); 
			break;
			case 3: mBattleListener.BattleOpened(words[1], words[2], words[3], 
					words[4], words[5], words[6], words[7], words[8], words[9], 
					words[10], sentences[1], sentences[2], ""); 
			break;
			default: mBattleListener.BattleOpened(words[1], words[2], words[3], 
					words[4], words[5], words[6], words[7], words[8], words[9], 
					words[10], sentences[1], sentences[2], sentences[3]);
			}
		} else
		if (cmd.equals("BATTLECLOSED")) {
			mBattleListener.BattleClosed(words[1]);
		} else
		if (cmd.equals("JOINBATTLE")) {
			mBattleListener.JoinBattle(words[1]);
		} else
		if (cmd.equals("JOINBATTLEFAILED")) {
			mBattleListener.JoinBattleFailed(data.substring(GetStartIdx(words, 0)));
		} else
		if (cmd.equals("JOINEDBATTLE")) {
			mBattleListener.JoinedBattle(words[1], words[2]);
		} else
		if (cmd.equals("LEFTBATTLE")) {
			mBattleListener.LeftBattle(words[1], words[2]);
		} else
		if (cmd.equals("UPDATEBATTLEINFO")) {
			mBattleListener.UpdateBattleInfo(words[1], words[2], words[3], 
					words[4], words[5]);
		} else
		
		// Connection related commands
		if (cmd.equals("TASServer")) {
			mConnListener.Connected(words[1], words[2], words[3], words[4]);
		} else
		if (cmd.equals("ACCEPTED")) {
			mConnListener.LoginSucceeded(words[1]);
		} else
		if (cmd.equals("DENIED")) {
			mConnListener.LoginFailed(data.substring(GetStartIdx(words, 1)));
		} else
		if (cmd.equals("SERVERMSGBOX")) {
			switch (sentences.length) {
				case 1: mConnListener.ServerMsgBox(words[1], "");
				default: mConnListener.ServerMsgBox(words[1], sentences[1]);
			}
		} else
		if (cmd.equals("PONG")) {
			mPongReceived = true;
			mPingTime = System.currentTimeMillis() - mStart;
			mConnListener.Pong(mPingTime);
		} else
		if (cmd.equals("AGREEMENT")) {
			mConnListener.Agreement(data.substring(GetStartIdx(words, 1)));
		} else
		if (cmd.equals("AGREEMENTEND")) {
			mConnListener.AgreementEnd();
		} else
		if (cmd.equals("REGISTRATIONDENIED")) {
			mConnListener.RegistrationDenied(data.substring(GetStartIdx(words, 1)));
		} else
		if (cmd.equals("REGISTRATIONACCEPTED")) {
			mConnListener.RegistrationAccepted();
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