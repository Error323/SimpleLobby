package com.spring.tasclient.simplelobby;

import java.util.Vector;

import com.spring.tasclient.simplelobby.UserHandler.Rank;
import com.spring.tasclient.simplelobby.UserHandler.User;
import com.spring.tasclient.simplelobby.interfaces.IBattleListener;

public class BattleHandler implements IBattleListener {
	public static enum NatType {
		None,
		HolePunching,
		FixedSource
	}
	
	public class Battle {
		public int mHash;
		public boolean mReplay;
		public NatType mNatType;
		public User mFounder;
		public Vector<User> mParticipants;
		public String mIpaddress;
		public int mPort;
		public int mMaxplayers;
		public boolean mPassworded;
		public Rank mRank;
		public int mMaphash;
		public String mMapname;
		public String mTitle;
		public String mModname;
		public boolean mLocked;
		public int mSpecs;
	}
	
	private TASConnection mConn;

	public BattleHandler(TASConnection conn) {
		mConn = conn;
	}

	@Override
	public void BattleClosed(String battleid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BattleOpened(String battleid, String type, String nattype,
			String founder, String ip, String port, String maxplayers,
			String passworded, String rank, String maphash, String mapname,
			String title, String modname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void JoinBattle(String battleid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void JoinBattleFailed(String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void JoinedBattle(String battleid, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LeftBattle(String battleid, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void UpdateBattleInfo(String battleid, String specs, String locked,
			String maphash, String mapname) {
		// TODO Auto-generated method stub
		
	}
}
