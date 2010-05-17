package com.spring.tasclient.simplelobby;

import java.util.HashMap;

import com.spring.tasclient.simplelobby.UserHandler.User;
import com.spring.tasclient.simplelobby.interfaces.IChatListener;
import com.spring.tasclient.simplelobby.interfaces.IChatUserListener;
import com.spring.tasclient.simplelobby.interfaces.IChatWinInterface;
import com.spring.tasclient.simplelobby.ui.ChatUserModel;
import com.spring.tasclient.simplelobby.ui.ChatUserModel.Column;

public class ChatHandler implements IChatListener, IChatUserListener {
	TASConnection mConn;
	UserHandler mUserHandler;
	HashMap<String, ChatUserModel> mChannels;
	
	private IChatWinInterface mChatWinInterface;
	
	public ChatHandler(TASConnection conn, UserHandler userhandler) {
		mChannels = new HashMap<String, ChatUserModel>();
		mConn = conn;
		mUserHandler = userhandler;
	}
	
	public void AttachChatWinInterface(IChatWinInterface icwi) {
		mChatWinInterface = icwi;
	}

	@Override
	public void Channel(String channel, String usercount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ChannelMsg(String channel, String msg) {
		
	}

	@Override
	public void ChannelTopic(String channel, String author, String changedtime,
			String topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Clients(String channel, String clients) {
		ChatUserModel cum = mChannels.get(channel);
		for (String username : clients.split(" ")) {
			User user = mUserHandler.GetUser(username);
			cum.Insert(user.GetStatus(), user.mCountry, user.mRank, user.mName);
			user.AddChannel(channel);
		}
	}

	@Override
	public void EndOfChannels() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ForceLeaveChannel(String channel, String username, String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void JoinFailed(String channel, String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void JoinSucceeded(String channel) {
		ChatUserModel cum = new ChatUserModel();
		mChannels.put(channel, cum);
		mChatWinInterface.CreateChannel(channel, cum);
	}

	@Override
	public void Joined(String channel, String username) {
		ChatUserModel cum = mChannels.get(channel);
		User user = mUserHandler.GetUser(username);
		cum.Insert(user.GetStatus(), user.mCountry, user.mRank, user.mName);
		user.AddChannel(channel);
	}

	@Override
	public void Left(String channel, String username, String reason) {
		ChatUserModel cum = mChannels.get(channel);
		cum.Delete(username);
		User user = mUserHandler.GetUser(username);
		user.RemoveChannel(channel);
	}

	@Override
	public void Motd(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Said(String channel, String username, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SaidEx(String channel, String username, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SaidPrivate(String username, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SayPrivate(String username, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ServerMsg(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Away(String username, boolean away) {
		User user = mUserHandler.GetUser(username);
		if (user == null) return;
		for (String channel : user.mChannels) {
			ChatUserModel cum = mChannels.get(channel); 
			cum.setValueAt(user.GetStatus(), username, Column.s.ordinal());
		}
	}

	@Override
	public void Bot(String username, boolean bot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void InGame(String username, boolean ingame) {
		User user = mUserHandler.GetUser(username);
		if (user == null) return;
		for (String channel : user.mChannels) {
			ChatUserModel cum = mChannels.get(channel); 
			cum.setValueAt(user.GetStatus(), username, Column.s.ordinal());
		}

	}

	@Override
	public void Moderator(String username, boolean moderator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Rank(String username,
			com.spring.tasclient.simplelobby.UserHandler.Rank rank) {
		User user = mUserHandler.GetUser(username);
		if (user == null) return;
		for (String channel : user.mChannels) {
			ChatUserModel cum = mChannels.get(channel); 
			cum.setValueAt(user.GetStatus(), username, Column.r.ordinal());
		}

	}
}
