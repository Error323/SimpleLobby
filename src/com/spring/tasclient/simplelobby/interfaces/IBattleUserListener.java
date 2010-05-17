package com.spring.tasclient.simplelobby.interfaces;

import com.spring.tasclient.simplelobby.UserHandler.Sync;

public interface IBattleUserListener {
	public void Ally(String username, int ally);
	
	public void Color(String username, int color);
	
	public void Handicap(String username, int handicap);
	
	public void Ready(String username, boolean ready);
	
	public void Side(String username, int side);
	
	public void Spectator(String username, boolean spectator);
	
	public void Sync(String username, Sync sync);
	
	public void Team(String username, int team);
}
