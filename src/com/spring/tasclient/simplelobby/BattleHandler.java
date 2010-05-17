package com.spring.tasclient.simplelobby;

import com.spring.tasclient.simplelobby.interfaces.IBattleUserListener;

public class BattleHandler implements IBattleUserListener {
	private TASConnection mConn;

	public BattleHandler(TASConnection conn) {
		mConn = conn;
	}
	@Override
	public void Ally(String username, int ally) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Color(String username, int color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Handicap(String username, int handicap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ready(String username, boolean ready) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Side(String username, int side) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Spectator(String username, boolean spectator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Sync(String username,
			com.spring.tasclient.simplelobby.UserHandler.Sync sync) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Team(String username, int team) {
		// TODO Auto-generated method stub
		
	}

}
