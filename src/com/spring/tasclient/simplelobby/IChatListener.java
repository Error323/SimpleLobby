package com.spring.tasclient.simplelobby;

/**
 * The Chat interface handles incoming server messages related to chat
 */
public interface IChatListener {
	
	/**
	 * CHANNEL channame usercount
	 * 
	 * Sent by server to client who requested channel list via CHANNELS command. 
	 * A series of these commands will be sent to user, one for each open 
	 * channel. Topic parameter may be omited if topic is not set for the channel. 
	 * A series of CHANNEL commands is ended by ENDOFCHANNELS command. 
	 * 
	 * @param channel
	 * @param usercount
	 */
	public void Channel(String channel, String usercount);
	
	/**
	 * CHANNELMESSAGE channame message
	 * 
	 * Sent by server to all clients in a channel. Used to broadcast messages 
	 * in the channel. 
	 * 
	 * @param channel
	 * @param msg
	 */
	public void ChannelMsg(String channel, String msg);
	
	/**
	 * CHANNELTOPIC channame author changedtime {topic}
	 * 
	 * Sent to client who just joined the channel, if some topic is set for 
	 * this channel.
	 * 
	 * @param channel
	 * @param author
	 * @param changedtime
	 * @param topic
	 */
	public void ChannelTopic(String channel, String author, String changedtime, String topic);
	
	/**
	 * CLIENTS channame {clients}
	 * 
	 * Sent to a client who just joined the channel. Note: Multiple commands of
	 * this kind can be sent in a row. Server takes the list of clients in a 
	 * channel and separates it into several lines and sends each line 
	 * seperately. This ensures no line is too long, because client may have 
	 * some limit set on the maximum length of the line and it could ignore it 
	 * if it was too long. Also note that the client itself (his username) is 
	 * sent in this list too! So when client receives JOIN command he should 
	 * not add his name in the clients list of the channel - he should wait for 
	 * CLIENTS command which will contain his name too and he will add himself 
	 * then automatically. 
	 * 
	 * @param channel
	 * @param clients
	 */
	public void Clients(String channel, String clients);
	
	/**
	 * ENDOFCHANNELS
	 * 
	 * Sent to client who previously requested channel list, after a series of 
	 * CHANNEL commands (one for each channel). 
	 */
	public void EndOfChannels();
	
	/**
	 * FORCELEAVECHANNEL channame username [{reason}]
	 * 
	 * Sent to user who has just been kicked from the channel #channame by user 
	 * "username". (lobby client should now internally close/detach from the 
	 * channel as he was removed from the clients list of #channame on server 
	 * side) 
	 * @param channel
	 * @param username
	 * @param reason
	 */
	public void ForceLeaveChannel(String channel, String username, String reason);
	
	/**
	 * JOINED channame username
	 * 
	 * Sent to all clients in a channel (except the new client) when a new 
	 * client joins the channel. 
	 * 
	 * @param channel
	 * @param username
	 */
	public void Joined(String channel, String username);
	
	/**
	 * JOINFAILED channame {reason}
	 * 
	 * Sent if joining a channel failed for some reason.
	 * reason: Always provided. 
	 * 
	 * @param channel
	 * @param reason
	 */
	public void JoinFailed(String channel, String reason);
	
	/**
	 * JOIN channame
	 * 
	 * Sent to a client who has successfully joined a channel. 
	 * channame: Name of the channel to which client has just joined 
	 * (by previously sending the JOIN command).
	 *  
	 * @param channel
	 */
	public void JoinSucceeded(String channel);
	
	/**
	 * LEFT channame username [{reason}]
	 * 
	 * Sent to user who has just been kicked from the channel #channame by user
	 * "username". (lobby client should now internally close/detach from the 
	 * channel as he was removed from the clients list of #channame on server 
	 * side) 
	 * 
	 * @param channel
	 * @param username
	 * @param reason
	 */
	public void Left(String channel, String username, String reason);
	
	/** 
	 * MOTD {message}
	 * 
	 * Sent by server after client has successfully logged in. Server can send
	 * multiple MOTD commands (each MOTD corresponds to one line, for example).
	 *  
	 * @param msg
	 */
	public void Motd(String msg);
	
	/**
	 * SAID channame userame {message}
	 * 
	 * Sent by server to all clients participating in this channel when one 
	 * of the clients sent a message to it (including the author of this 
	 * message).
	 * 
	 * @param channel
	 * @param username
	 * @param msg
	 */
	public void Said(String channel, String username, String msg);
	
	/**
	 * SAIDEX channame userame {message}
	 * 
	 * Sent by server when client said something using SAYEX command.
	 *  
	 * @param channel
	 * @param username
	 * @param msg
	 */
	public void SaidEx(String channel, String username, String msg);
	
	/**
	 * SAIDPRIVATE username {message}
	 * 
	 * Sent by server when some client sent this client a private message.
	 * 
	 * @param username
	 * @param msg
	 */
	public void SaidPrivate(String username, String msg);
	
	/**
	 * SAYPRIVATE username {message}
	 * 
	 * Sent by server to a client who just sent SAYPRIVATE command to server. 
	 * This way client can verify that server did get his message and that 
	 * receiver will get it too. 
	 * 
	 * @param username
	 * @param msg
	 */
	public void SayPrivate(String username, String msg);
	
	/**
	 * SERVERMSG {message}
	 * 
	 * A general purpose message sent by the server. Lobby program should 
	 * display it either in the chat log or in some kind of system log, where 
	 * client will notice it. Also see SERVERMSGBOX command. 
	 * 
	 * @param msg
	 */
	public void ServerMsg(String msg);
}
