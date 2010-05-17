package com.spring.tasclient.simplelobby.interfaces;

import com.spring.tasclient.simplelobby.UserHandler.Rank;

public interface IChatUserListener {
	public void Away(String username, boolean away);
	
	public void Bot(String username, boolean bot);
	
	public void InGame(String username, boolean ingame);
	
	public void Moderator(String username, boolean moderator);
	
	public void Rank(String username, Rank rank);
}
