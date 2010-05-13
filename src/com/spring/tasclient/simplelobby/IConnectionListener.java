package com.spring.tasclient.simplelobby;

public interface IConnectionListener {
	/**
	 * TASSERVER server_version spring_version udpport servermode
	 * 
	 * This is the first message (i.e. "greeting message") that client
	 * receives upon connecting to the server. 
	 * 
	 * @param serverVersion
	 * @param springVersion
	 * @param udpport
	 * @param servermode
	 */
	public void Connected(String serverVersion, String springVersion, String udpport, String servermode);
	
	/**
	 * REGISTRATIONDENIED {reason}
	 * 
	 * Sent to client who has just sent REGISTER command, if registration 
	 * has been refused. 
	 * 
	 * @param reason
	 */
	public void RegistrationDenied(String reason);
	
	/**
	 * REGISTRATIONACCEPTED
	 * 
	 * Sent to client who has just sent REGISTER command, if registration 
	 * has been accepted. 
	 */
	public void RegistrationAccepted();
	
	/**
	 * ACCEPTED username
	 * 
	 * Sent as a response to LOGIN command if it succeeded. After server has 
	 * finished sending info on clients and battles, it will send LOGININFOEND
	 * command indicating that it has finished sending the login info. 
	 * 
	 * @param username
	 */
	public void LoginSucceeded(String username);
	
	/**
	 * DENIED {reason}
	 * 
	 * Sent as a response to a failed LOGIN command. 
	 * 
	 * @param reason
	 */
	public void LoginFailed(String reason);
	
	/**
	 * SERVERMSGBOX {message} [{url}]
	 * 
	 * This is a message sent by the server that should open in a message box
	 * dialog window (and not just in the console). Also see SERVERMSG command.
	 * 
	 * @param msg
	 * @param url
	 */
	public void ServerMsgBox(String msg, String url);
	
	/**
	 * If connection is lost to the server
	 * 
	 * @param reason
	 */
	public void Disconnected(String reason);
}
