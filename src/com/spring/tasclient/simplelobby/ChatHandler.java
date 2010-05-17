package com.spring.tasclient.simplelobby;

import java.util.HashMap;

import com.spring.tasclient.simplelobby.ChatUserModel.Column;
import com.spring.tasclient.simplelobby.UserHandler.User;
import com.spring.tasclient.simplelobby.interfaces.IChatListener;
import com.spring.tasclient.simplelobby.interfaces.IChatUserListener;
import com.spring.tasclient.simplelobby.ui.ChatWindow;

public class ChatHandler implements IChatListener, IChatUserListener {
	TASConnection mConn;
	UserHandler mUserHandler;
	HashMap<String, ChatUserModel> mChannels;
	
	private ChatWindow mChatWin;
	
	public ChatHandler(TASConnection conn, UserHandler userhandler) {
		mChannels = new HashMap<String, ChatUserModel>();
		mConn = conn;
		mUserHandler = userhandler;
	}
	
	public void AttachChatWinInterface(ChatWindow chatwin) {
		mChatWin = chatwin;
	}

	@Override
	public void Channel(String channel, String usercount) {
		mChatWin.Channel(channel, usercount);
	}

	@Override
	public void ChannelMsg(String channel, String msg) {
		mChatWin.ChannelMsg(channel, msg);
	}

	@Override
	public void ChannelTopic(String channel, String author, String changedtime,
			String topic) {
		mChatWin.ChannelTopic(channel, author, Long.parseLong(changedtime), topic);
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
		mChatWin.ForceLeaveChannel(channel, username, reason);
	}

	@Override
	public void JoinFailed(String channel, String reason) {
		mChatWin.JoinFailed(channel, reason);
	}

	@Override
	public void JoinSucceeded(String channel) {
		ChatUserModel cum = new ChatUserModel();
		mChannels.put(channel, cum);
		mChatWin.CreateChannel(channel, cum);
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
		mChatWin.Motd(msg);
	}

	@Override
	public void Said(String channel, String username, String msg) {
		mChatWin.Said(channel, username, msg);
	}

	@Override
	public void SaidEx(String channel, String username, String msg) {
		mChatWin.SaidEx(channel, username, msg);
	}

	@Override
	public void SaidPrivate(String username, String msg) {
		mChatWin.SaidPrivate(username, msg);
	}

	@Override
	public void SayPrivate(String username, String msg) {
		mChatWin.SayPrivate(username, msg);
	}

	@Override
	public void ServerMsg(String msg) {
		mChatWin.ServerMsg(msg);
	}

	@Override
	public void Away(String username, boolean away) {
		User user = mUserHandler.GetUser(username);
		if (user == null) return;
		for (String channel : user.mChannels) {
			ChatUserModel cum = mChannels.get(channel); 
			cum.setValueAt(user.GetStatus(), username, Column.Status.ordinal());
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
			cum.setValueAt(user.GetStatus(), username, Column.Status.ordinal());
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
			cum.setValueAt(user.GetStatus(), username, Column.Rank.ordinal());
		}

	}

	public void Say(String text, String channel) {
		if (text.length() > 0 && text.charAt(0) == '/') {
			String splitted[] = text.substring(1).split(" ");
			String cmd = splitted[0];
			if (cmd.equals("join") || cmd.equals("j")) {
				if (splitted.length > 1) {
					splitted[1].replace("#", "");
					if (splitted.length == 2)
						mConn.Join(splitted[1], "");
					else
					if (splitted.length == 3)
						mConn.Join(splitted[1], splitted[2]);
				}
			} else

			if (cmd.equals("leave")) {
				mConn.Leave(channel);
				mChatWin.Leave(channel);
			} else

			if (cmd.equals("me")) {
				mConn.SayEx(channel, text.substring(4));
			}
		}
		else {
			mConn.Say(channel, text);
		}
	}
}
