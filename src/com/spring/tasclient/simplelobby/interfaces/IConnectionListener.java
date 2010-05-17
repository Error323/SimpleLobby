package com.spring.tasclient.simplelobby.interfaces;

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
	public void Connected(String serverVersion, String springVersion, 
			String udpport, String servermode);
	
	/**
	 * If connection is lost to the server
	 * 
	 * @param reason
	 */
	public void Disconnected(String reason);
	
	/**
	 * DENIED {reason}
	 * 
	 * Sent as a response to a failed LOGIN command. 
	 * 
	 * @param reason
	 */
	public void LoginFailed(String reason);
	
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
	 * PONG
	 * 
	 * Returns ping time
	 * 
	 * @param ping
	 */
	public void Pong(long ping);
	
	/**
	 * REGISTRATIONACCEPTED
	 * 
	 * Sent to client who has just sent REGISTER command, if registration 
	 * has been accepted. 
	 */
	public void RegistrationAccepted();
	
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
	 * AGREEMENT {agreement}
	 * 
	 * Sent by server upon receiving LOGIN command, if client has not yet 
	 * agreed to server's "terms-of-use" agreement. Server may send multiple 
	 * AGREEMENT commands (which corresponds to multiple new lines in 
	 * agreement), finishing it by AGREEMENTEND command. Client should send 
	 * CONFIRMAGREEMENT and then resend LOGIN command, or disconnect from 
	 * the server if he has chosen to refuse the agreement. 
	 * 
	 * @param agreement
	 */
	public void Agreement(String agreement);

	/**
	 * AGREEMENTEND
	 * 
	 * Sent by server after multiple AGREEMENT commands. This way server tells 
	 * the client that he has finished sending the agreement (this is the time 
	 * when lobby should popup the "agreement" screen and wait for user to 
	 * accept/reject it). 
	 */
	public void AgreementEnd();
}
