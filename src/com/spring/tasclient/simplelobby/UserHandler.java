package com.spring.tasclient.simplelobby;

import java.awt.Color;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import com.spring.tasclient.simplelobby.interfaces.IBattleUserListener;
import com.spring.tasclient.simplelobby.interfaces.IChatUserListener;
import com.spring.tasclient.simplelobby.interfaces.IUserHandlerListener;

public class UserHandler implements IUserHandlerListener {
	/**
	 * Rank from TASServer
	 * 
	 * Current rank categories:
	 * < 5h = newbie
	 * 5h - 15h = beginner
	 * 15h - 30h = avarage
	 * 30h - 100h = above avarage
	 * 100h - 300h = experienced player
	 * 300h - 1000h = highly experienced player
	 * > 1000h = veteran
	 */
	public static enum Rank {
		Newbie,
		Beginner,
		Average,
		AboveAverage,
		Experienced,
		HighlyExperienced,
		Veteran
	}
	
	public static enum Sync {
		Unknown,
		Synced,
		Unsynced
	}
	
	public class User {
		public String mName;
		public String mCountry;
		public String mCpu;
		public Vector<String> mChannels;

		public Rank mRank;
		public Sync mSync;
		
		public int mColor;
		public int mTeam;
		public int mAlly;
		public int mHandicap;
		public int mSide;
		
		public boolean mAway;
		public boolean mBot;
		public boolean mInGame;
		public boolean mModerator;
		public boolean mReady;
		public boolean mSpectator;
		
		public User(String name, String country, String cpu) {
			mChannels = new Vector<String>();
			Reset(name, country, cpu);
		}
		
		public void Reset(String name, String country, String cpu) {
			mName = name;
			mCountry = country;
			mCpu = cpu;
			mChannels.clear();
			SetColor(Color.LIGHT_GRAY.getRGB());
			SetStatus(0);
			SetBattleStatus(0);
		}
		
		public void AddChannel(String channel) {
			mChannels.add(channel);
		}
		
		public void RemoveChannel(String channel) {
			for (int i = 0; i < mChannels.size(); i++) {
				if (mChannels.get(i).equals(channel)) {
					mChannels.remove(i);
					break;
				}
			}
		}
		
		/**
		 * SetBattleStatus battlestatus
		 * 
		 * battlestatus: An integer but with limited range: 0..2147483647 (use signed int and consider only positive values and zero). Number is sent as text. Each bit has its meaning:
		 * b0 = undefined (reserved for future use)
		 * b1 = ready (0=not ready, 1=ready)
		 * b2..b5 = team no. (from 0 to 15. b2 is LSB, b5 is MSB)
		 * b6..b9 = ally team no. (from 0 to 15. b6 is LSB, b9 is MSB)
		 * b10 = mode (0 = spectator, 1 = normal player)
		 * b11..b17 = handicap (7-bit number. Must be in range 0..100). Note: Only host can change handicap values of the players in the battle (with HANDICAP command). These 7 bits are always ignored in this command. They can only be changed using HANDICAP command.
		 * b18..b21 = reserved for future use (with pre 0.71 versions these bits were used for team color index)
		 * b22..b23 = sync status (0 = unknown, 1 = synced, 2 = unsynced)
		 * b24..b27 = side (e.g.: arm, core, tll, ... Side index can be between 0 and 15, inclusive)
		 * b28..b31 = undefined (reserved for future use)
		 * 
		 * @param battlestatus
		 */
		public void SetBattleStatus(int battlestatus) {
			// Extract the battlestatus information
			boolean ready     = ((battlestatus >> 1) & 1) == 1;
			boolean spectator = ((battlestatus >> 10) & 1) == 1;
			int handicap      = (battlestatus >> 11) & 127;
			int team          = (battlestatus >> 2) & 15;
			int ally          = (battlestatus >> 6) & 15;
			int side          = (battlestatus >> 24) & 15;
			
			Sync sync = Sync.Unknown;
			switch ((battlestatus >> 22)&3) {
				case 1: sync = Sync.Synced;
				case 2: sync = Sync.Unsynced;
			}
			
			// Alert the interfaces when states changed
			if (mReady != ready) {
				mReady = ready;
				mBattleUserListener.Ready(mName, ready);
			}
			if (mSpectator != spectator) {
				mSpectator = spectator;
				mBattleUserListener.Spectator(mName, spectator);
			}
			if (mHandicap != handicap) {
				mHandicap = handicap;
				mBattleUserListener.Handicap(mName, handicap);
			}
			if (mTeam != team) {
				mTeam = team;
				mBattleUserListener.Team(mName, team);
			}
			if (mAlly != ally) {
				mAlly = ally;
				mBattleUserListener.Ally(mName, ally);
			}
			if (mSide != side) {
				mSide = side;
				mBattleUserListener.Side(mName, side);
			}
			if (mSync != sync) {
				mSync = sync;
				mBattleUserListener.Sync(mName, sync);
			}
		}
		
		/**
		 * SetColor myteamcolor
		 * 
		 * myteamcolor: Should be 32-bit signed integer in decimal form (e.g. 255 and not FF) where each color channel should occupy 1 byte (e.g. in hexdecimal: $00BBGGRR, B = blue, G = green, R = red). Example: 255 stands for $000000FF.
		 *  
		 * @param color
		 */
		public void SetColor(int color) {
			if (mColor != color) {
				mColor = color;
				mBattleUserListener.Color(mName, color);
			}
		}
		
		/**
		 * SetStatus status
		 * 
		 * status: A signed integer in text form (e.g. "1234"). Each bit has its meaning:
		 * b0 = in game (0 - normal, 1 - in game)
		 * b1 = away status (0 - normal, 1 - away)
		 * b2-b4 = rank (see Account class implementation for description of rank) - client is not allowed to change rank bits himself (only server may set them).
		 * b5 = access status (tells us whether this client is a server moderator or not) - client is not allowed to change this bit himself (only server may set them).
		 * b6 = bot mode (0 - normal user, 1 - automated bot). This bit is copied from user's account and can not be changed by the client himself. Bots differ from human players in that they are fully automated and that some anti-flood limitations do not apply to them.
		 * 
		 * @param status
		 */
		public void SetStatus(int status) {
			// Extract the status information
			boolean ingame    = ((status >> 0) & 1) == 1;
			boolean away      = ((status >> 1) & 1) == 1;
			boolean moderator = ((status >> 5) & 1) == 1;
			boolean bot       = ((status >> 6) & 1) == 1;
			
			Rank rank = Rank.Newbie;
			switch ((status >> 2) & 7) {
				case 1:  rank = Rank.Beginner; break;
				case 2:  rank = Rank.Average; break;
				case 3:  rank = Rank.AboveAverage;break;
				case 4:  rank = Rank.Experienced; break;
				case 5:  rank = Rank.HighlyExperienced; break;
				case 6:  rank = Rank.Veteran; break;
			}
			
			// Alert the interfaces when states changed
			if (mAway != away) {
				mAway = away;
				mChatUserListener.Away(mName, away);
			}
			if (mBot != bot) {
				mBot = bot;
				mChatUserListener.Bot(mName, bot);
			}
			if (mInGame != ingame) {
				mInGame = ingame;
				mChatUserListener.InGame(mName, ingame);
			}
			if (mModerator != moderator) {
				mModerator = moderator;
				mChatUserListener.Moderator(mName, moderator);
			}
			if (mRank != rank) {
				mRank = rank;
				mChatUserListener.Rank(mName, rank);
			}
		}

		public String GetStatus() {
			String status = "Available";
			if (mInGame)
				status = "Gaming";
			else
			if (mAway)
				status = "Away";
			if (mModerator)
				status += "/Mod";
			if (mBot)
				status += "/Bot";
			return status;
		}
	}
	public static String mMyUserName;
	
	private TASConnection mConn;
	private IChatUserListener mChatUserListener;
	private IBattleUserListener mBattleUserListener;
	private Hashtable<String, User> mUsers;
	private LinkedList<User> mFreeUsers;

	public UserHandler(TASConnection conn) {
		mConn = conn;
		mUsers = new Hashtable<String, User>();
		mFreeUsers = new LinkedList<User>();
	}
	
	@Override
	public void AddUser(String username, String country, String cpu) {
		User user;
		if (mFreeUsers.isEmpty()) {
			user = new User(username, country, cpu);
		}
		else {
			user = mFreeUsers.removeFirst();
			user.Reset(username, country, cpu);
		}
		mUsers.put(username, user);
	}
	
	public void AttachBattleUserInterface(IBattleUserListener ibul) {
		mBattleUserListener = ibul;
	}
	
	public void AttachChatUserInterface(IChatUserListener icul) {
		mChatUserListener = icul;
	}
	
	@Override
	public void ClientBattleStatus(String username, int battlestatus, int color) {
		User user = GetUser(username);
		user.SetBattleStatus(battlestatus);
		user.SetColor(color);
	}

	@Override
	public void ClientStatus(String username, int status) {
		User user = GetUser(username);
		user.SetStatus(status);
	}

	public User GetUser(String username) {
		return mUsers.get(username);
	}

	@Override
	public void RemoveUser(String username) {
		User user = mUsers.remove(username);
		mFreeUsers.add(user);
	}
	
	public void SetMyBattleStatus(boolean ready, int team, 
			int ally, boolean spectator, int handicap, Sync sync, int side) {
		int battlestatus = 0;
		
		battlestatus |= (ready ? 1 : 0) << 1;
		battlestatus |= (team << 2);
		battlestatus |= (ally << 6);
		battlestatus |= (spectator ? 1 : 0) << 10;
		battlestatus |= (handicap << 11);
		battlestatus |= (sync.ordinal() << 22);
		battlestatus |= (side << 24);
		mConn.MyBattleStatus(mMyUserName, battlestatus);
	}
	
	public void SetMyStatus(boolean ingame, boolean away, Rank rank, boolean moderator, boolean bot) {
		int status = 0;
		
		status |= (ingame ? 1 : 0) << 0;
		status |= (away ? 1 : 0) << 1;
		status |= rank.ordinal() << 2;
		status |= (moderator ? 1 : 0) << 5;
		status |= (bot ? 1 : 0) << 6;
		mConn.MyStatus(mMyUserName, status);
	}
}
