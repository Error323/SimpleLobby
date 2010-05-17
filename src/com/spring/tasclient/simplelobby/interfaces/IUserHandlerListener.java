package com.spring.tasclient.simplelobby.interfaces;

public interface IUserHandlerListener {
	/**
	 * ADDUSER username country cpu
	 * 
	 * Sent by server to client telling him new user joined a server. Client 
	 * should add this user to his clients list which he must maintain while he 
	 * is connected to the server. Server will send multiple commands of this 
	 * kind once client logs in, sending him the list of all users currently 
	 * connected. 
	 * 
	 * @param username
	 * @param country
	 * @param cpu
	 */
	void AddUser(String username, String country, String cpu);
	
	/**
	 * CLIENTBATTLESTATUS username battlestatus teamcolor
	 * 
	 * Sent by server to users participating in a battle when one of the clients 
	 * changes his battle status.
	 * 
	 * @param username
	 * @param battlestatus
	 * @param color
	 */
	void ClientBattleStatus(String username, int battlestatus, int color);
	
	/**
	 * CLIENTSTATUS username status
	 * 
	 * Sent by server to all registered clients indicating that client's status
	 * changed. Note that client's status is considered 0 if not said otherwise
	 * (for example, when you logon, server sends only statuses of those 
	 * clients whose statuses differ from 0, to save the bandwidth). 
	 * 
	 * @param username
	 * @param status
	 */
	void ClientStatus(String username, int status);
	
	/**
	 * REMOVEUSER username
	 * 
	 * Sent to client telling him a user disconnected from server. Client 
	 * should remove this user from his clients list which he must maintain 
	 * while he is connected to the server. 
	 * 
	 * @param username
	 */
	void RemoveUser(String username);
}
