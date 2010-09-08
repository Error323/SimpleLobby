package com.spring.tasclient.simplelobby;

import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.spring.tasclient.simplelobby.UserHandler.Rank;
import com.spring.tasclient.simplelobby.UserHandler.User;
import com.spring.tasclient.simplelobby.interfaces.IBattleListener;

public class BattleHandler implements IBattleListener {
	public class Battle {
		public String mHash;
		public boolean mReplay;
		public NatType mNatType;
		public User mFounder;
		public HashMap<String, User> mParticipants;
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
		
		public Battle() {
			mParticipants = new HashMap<String, User>();
			mLocked = false;
			mSpecs = 0;
		}
		
		public void Reset(String battleid, String type, String nattype,
				String founder, String ip, String port, String maxplayers,
				String passworded, String rank, String maphash, String mapname,
				String title, String modname) {

			mHash = battleid;
			mReplay = Boolean.parseBoolean(type);
			mNatType = NatType.valueOf(nattype);
			mFounder = mUserHandler.GetUser(founder);
			mIpaddress = ip;
			mPort = Integer.parseInt(port);
			mMaxplayers = Integer.parseInt(maxplayers);
			mPassworded = Boolean.parseBoolean(passworded);
			mRank = Rank.valueOf(rank);
			mMaphash = Integer.parseInt(maphash);
			mMapname = mapname;
			mTitle = title;
			mModname = modname;
			
			mParticipants.clear();
			mLocked = false;
			mSpecs = 0;
		}
	}
	
	public static enum NatType {
		None,
		HolePunching,
		FixedSource
	}
	
	private TASConnection mConn;
	private UserHandler mUserHandler;
	private HashMap<String, Battle> mBattles;
	private LinkedList<Battle> mFreeBattles;

	public BattleHandler(TASConnection conn, UserHandler uh) {
		mConn = conn;
		mUserHandler = uh;
		mBattles = new HashMap<String, Battle>();
		mFreeBattles = new LinkedList<Battle>();
	}

	@Override
	public void BattleClosed(String battleid) {
		Battle battle = mBattles.remove(battleid);
		mFreeBattles.add(battle);
	}

	@Override
	public void BattleOpened(String battleid, String type, String nattype,
			String founder, String ip, String port, String maxplayers,
			String passworded, String rank, String maphash, String mapname,
			String title, String modname) {
		
		Battle battle;
		if (mFreeBattles.isEmpty()) {
			battle = new Battle();
			battle.mHash = battleid;
			battle.mReplay = Boolean.parseBoolean(type);
			battle.mNatType = NatType.valueOf(nattype);
			battle.mFounder = mUserHandler.GetUser(founder);
			battle.mIpaddress = ip;
			battle.mPort = Integer.parseInt(port);
			battle.mMaxplayers = Integer.parseInt(maxplayers);
			battle.mPassworded = Boolean.parseBoolean(passworded);
			battle.mRank = Rank.valueOf(rank);
			battle.mMaphash = Integer.parseInt(maphash);
			battle.mMapname = mapname;
			battle.mTitle = title;
			battle.mModname = modname;
		}
		else {
			battle = mFreeBattles.removeLast();
			battle.Reset(battleid, type, nattype, founder, ip, port, maxplayers,
			passworded, rank, maphash, mapname, title, modname);
		}
		mBattles.put(battleid, battle);
	}

	@Override
	public void JoinBattle(String battleid) {
		// TODO Open battleroom
		
	}

	@Override
	public void JoinBattleFailed(String reason) {
		SimpleLobby.MsgBox("Failed to join battle", reason, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void JoinedBattle(String battleid, String username) {
		Battle battle = mBattles.get(battleid);
		battle.mParticipants.put(username, mUserHandler.GetUser(username));
	}

	@Override
	public void LeftBattle(String battleid, String username) {
		Battle battle = mBattles.get(battleid);
		battle.mParticipants.remove(username);
	}

	@Override
	public void UpdateBattleInfo(String battleid, String specs, String locked,
			String maphash, String mapname) {
		Battle battle = mBattles.get(battleid);
		if (Integer.parseInt(specs) != battle.mSpecs) {
			
		}
		
		if (Boolean.parseBoolean(locked) != battle.mLocked) {
			
		}
		
		if (Integer.parseInt(maphash) != battle.mMaphash) {
			
		}
		
		if (!mapname.equals(battle.mMapname)) {
			
		}
	}
}
