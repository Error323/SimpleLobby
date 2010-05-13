package com.spring.tasclient.simplelobby;

/**
 * The Chat interface handles outgoing server messages related to chat
 */
public interface IChatOut {

	/**
	 * JOIN channame [key]
	 * 
	 * Sent by client trying to join a channel. key: If channel is locked, then
	 * client must supply a correct key to join the channel (clients with access 
	 * >= Account.ADMIN_ACCESS can join locked channels withouth supplying the 
	 * key - needed for ChanServ bot). 
	 * 
	 * @param channel
	 * @param password
	 */
	public void Join(String channel, String password);
	
	/**
	 * CHANNELTOPIC channame {topic}
	 * 
	 * Sent by privileged user who is trying to change channel's topic. Use * 
	 * as topic if you wish to disable it. 
	 * 
	 * @param channel
	 * @param topic
	 */
	public void ChannelTopic(String channel, String topic);
	
	/**
	 * LEAVE channame
	 * 
	 * Sent by client when he is trying to leave a channel. When client is 
	 * disconnected, he is automatically removed from all channels.
	 *  
	 * @param channel
	 */
	public void Leave(String channel);
	
	/**
	 * FORCELEAVECHANNEL channame username [{reason}]
	 * 
	 * Sent by client (moderator) requsting that the user is removed from 
	 * the channel. User will be notified with FORCELEAVECHANNEL command.
	 * 
	 * @param channel
	 * @param username
	 * @param reason
	 */
	public void ForceLeaveChannel(String channel, String username, String reason);
	
	/**
	 * 
	 * SAY channame {message}
	 * 
	 * Sent by client when he is trying to say something in a specific channel.
	 * Client must first join the channel before he can receive or send 
	 * messages to that channel. 
	 * 
	 * @param channel
	 * @param msg
	 */
	public void Say(String channel, String msg);
	
	/**
	 * SAYEX channame {message}	
	 * 
	 * Sent by any client when he is trying to say something in "/me" irc 
	 * style. Also see SAY command.
	 * 
	 * @param channel
	 * @param msg
	 */
	public void SayEx(String channel, String msg);
	
	/**
	 * SAYPRIVATE username {message}
	 * 
	 * Sent by client when he is trying to send a private message to some other
	 * client. 
	 * 
	 * @param username
	 * @param msg
	 */
	public void SayPrivate(String username, String msg);
}
