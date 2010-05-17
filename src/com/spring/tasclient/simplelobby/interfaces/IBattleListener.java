package com.spring.tasclient.simplelobby.interfaces;

public interface IBattleListener {
	/**
	 * BATTLEOPENED BATTLE_ID type natType founder IP port maxplayers 
	 * passworded rank maphash {map} {title} {modname}
	 * 
	 * Sent by server to all registered users, when a new battle has been 
	 * opened. Series of BATTLEOPENED commands are sent to user when he logs 
	 * in (1 command for each battle). Use Battle.createBattleOpenedCommand 
	 * method to create this command in a String (when modifying TASServer 
	 * source). 
	 * 
	 * @param battleid
	 * @param type Can be 0 or 1 (0 = normal battle, 1 = battle replay) 
	 * @param nattype NAT traversal method used by the host. Must be a number (0 means no NAT traversal technique should be applied). 
	 * @param founder Username of the client who started this battle.
	 * @param ip
	 * @param port
	 * @param maxplayers
	 * @param passworded A boolean - must be "0" or "1" and not "true" or "false" as it is default in Java!
	 * @param rank
	 * @param maphash A signed 32-bit integer as returned from unitsync.dll.
	 * @param mapname
	 * @param title
	 * @param modname
	 */
	public void BattleOpened(String battleid, String type, String nattype, 
			String founder, String ip, String port, String maxplayers, 
			String passworded, String rank, String maphash, String mapname,
			String title, String modname);
	
	/**
	 * BATTLECLOSED BATTLE_ID
	 * 
	 * Sent when founder has closed a battle (or if he was disconnected). 
	 * 
	 * @param battleid
	 */
	public void BattleClosed(String battleid);
	
	/**
	 * JOINBATTLE BATTLE_ID hashcode
	 * 
	 * Sent by server telling the client that he has just joined the battle 
	 * successfully. Server will also send a series of CLIENTBATTLESTATUS 
	 * commands after this command, so that user will get the battle statuses
	 * of all the clients in the battle. 
	 * 
	 * @param battleid
	 */
	public void JoinBattle(String battleid);
	
	/**
	 * JOINBATTLEFAILED {reason}
	 * 
	 * Sent by server to user who just tried to join a battle but has been 
	 * rejected by server. 
	 * 
	 * @param reason
	 */
	public void JoinBattleFailed(String reason);
	
	/**
	 * JOINEDBATTLE BATTLE_ID username
	 * 
	 * Sent by server to all clients when a new client joins the battle. 
	 * 
	 * @param battleid
	 * @param username
	 */
	public void JoinedBattle(String battleid, String username);
	
	/**
	 * LEFTBATTLE BATTLE_ID username
	 * 
	 * Sent by server to all users when client left a battle (or got 
	 * disconnected from the server). 
	 * 
	 * @param battleid
	 * @param username
	 */
	public void LeftBattle(String battleid, String username);

	/**
	 * UPDATEBATTLEINFO BATTLE_ID SpectatorCount locked maphash {mapname}
	 * 
	 * Sent by server to all registered clients telling them some of the parameters 
	 * of the battle changed. Battle's inside changes, like starting metal, energy, 
	 * starting position etc., are sent only to clients participating in the battle 
	 * via SETSCRIPTTAGS command. 
	 *
	 * @param battleid
	 * @param specs Assume that spectator count is 0 if battle type is 0 (normal battle) and 1 if battle type is 1 (battle replay), as founder of the battle is automatically set as a spectator in that case.
	 * @param locked A boolean (0 or 1). Note that when client creates a battle, server assumes it is unlocked (by default). Client must make sure it actually is.
	 * @param maphash A signed 32-bit integer. See OPENBATTLE command for more info.
	 * @param mapname
	 */
	public void UpdateBattleInfo(String battleid, String specs, String locked, 
			String maphash, String mapname);
}
