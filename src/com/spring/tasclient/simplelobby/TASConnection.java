package com.spring.tasclient.simplelobby;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class TASConnection extends AConnection {
	private static final int PING_INTERVAL = 5000;
	private long mStart;
	
	private IChatIn mChatIn;
	
	public TASConnection() {
		super();
		mStart = System.currentTimeMillis();
	}
	
	public void AttachChatInterface(IChatIn chatInterface) {
		mChatIn = chatInterface;
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
		
		// Chat related commands
		if (cmd.equals("SAID")) {
			mChatIn.Said(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
		} else
		if (cmd.equals("SAIDPRIVATE")) {
			mChatIn.SaidPrivate(splitted[1], splitted[2]);
		} else
		if (cmd.equals("SERVERMSG")) {
			mChatIn.ServerMsg(splitted[1]);
		} else
		if (cmd.equals("CHANNELMSG")) {
			mChatIn.ChannelMsg(splitted[1], data.substring(GetStartIdx(splitted,2)));
		} else
		if (cmd.equals("FORCELEAVECHANNEL")) {
			mChatIn.ForceLeaveChannel(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
		} else
		if (cmd.equals("LEFT")) {
			mChatIn.Left(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
		} else
		if (cmd.equals("JOINED")) {
			mChatIn.Joined(splitted[1], splitted[2]);
		} else
		if (cmd.equals("JOIN")) {
			mChatIn.JoinSucceeded(splitted[1]);
		} else
		if (cmd.equals("JOINFAILED")) {
			mChatIn.JoinFailed(splitted[1], data.substring(GetStartIdx(splitted, 2)));
		} else
		if (cmd.equals("SAIDEX")) {
			mChatIn.SaidEx(splitted[1], splitted[2], data.substring(GetStartIdx(splitted,3)));
		}
		if (cmd.equals("CHANNELTOPIC")) {
			mChatIn.ChannelTopic(splitted[1], splitted[2], splitted[3], data.substring(GetStartIdx(splitted,4)));
		} else
		if (cmd.equals("MOTD")) {
			if (data.length() == 4)
				mChatIn.Motd("");
			else
				mChatIn.Motd(data.substring(5));
		}
	}
	
	@Override
	protected void Update() {
		long stop = System.currentTimeMillis();
		if (stop - mStart > PING_INTERVAL) {
			Send("PING");
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
		// TODO Auto-generated method stub
		
	}

	public void ForceLeaveChannel(String channel, String username, String reason) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	public void SayEx(String channel, String msg) {
		Send("SAYEX " + channel + " " + msg);
	}
}