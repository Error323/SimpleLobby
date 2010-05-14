package com.spring.tasclient.simplelobby;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class TASConnection extends AConnection {
	private static final int PING_INTERVAL = 1000;
	
	private long mPingTime;
	private long mStart;
	private boolean mPongReceived;
	
	private IChatListener mChatListener;
	private IConnectionListener mConnListener;
	
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
	
	private int GetStartIdx(String[] data, int N) {
		int idx = 0;
		for (int i = 0; i < N && i < data.length; i++)
			idx += data[i].length()+1;
		return idx;
	}
	
	@Override
	protected void Received(String data) {
		System.out.println(data);
		String splitted[] = data.split(" ");
		String cmd = splitted[0];
		
		// Connection related commands
		if (mConnListener == null) {
			return;
		} else
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
		}
		else
		if (cmd.equals("PONG")) {
			mPongReceived = true;
			mPingTime = System.currentTimeMillis() - mStart;
			mConnListener.Pong(mPingTime);
		}
			
		// Chat related commands
		if (mChatListener == null) {
			return;
		} else
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
			mChatListener.Left(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
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
		}
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
	
	public void Say(String channel, String msg) {
		Send("SAY " + channel + " " + msg);
	}
	
	public void ChannelTopic(String channel, String topic) {
		
	}

	public void ForceLeaveChannel(String channel, String username, String reason) {
		
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

	public void SayPrivate(String username, String msg) {
		
	}

	public void SayEx(String channel, String msg) {
		Send("SAYEX " + channel + " " + msg);
	}
	
	public long GetPing() {
		return mPingTime;
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
	protected void UnknownHost(Exception e) {
		mConnListener.Disconnected("Unknown Host");
	}

	@Override
	protected void Connecting() {
		mStart = System.currentTimeMillis();
		mPongReceived = true;
	}
}