package com.spring.tasclient.simplelobby;

import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.spring.tasclient.simplelobby.UserHandler.Rank;


public class ChatUserModel extends AbstractTableModel {
	public class ChatUserData {
		public String mStatus;
		public String mCountry;
		public String mRank;
		public String mName;
		
		public ChatUserData(String status, String country, Rank rank, String name) {
			mStatus = status;
			mCountry = country;
			mRank = rank.name();
			mName = name;
		}
	}
	public static enum Column {
		Status,
		Country,
		Rank,
		Name
	}
	protected HashMap<String, Integer> mMap;
	
	protected Vector<ChatUserData> mData;
	
	public ChatUserModel() {
		mMap = new HashMap<String, Integer>();
		mData = new Vector<ChatUserData>();
	}
	
	public void Delete(String name) {
		int row = mMap.get(name);
		mData.remove(row);
		mMap.remove(name);
		fireTableRowsDeleted(row, row);
		mMap.clear();
		for (int i = 0; i < mData.size(); i++)
			mMap.put(mData.get(i).mName, i);

	}
	
	@Override
	public int getColumnCount() {
		return Column.values().length;
	}
	
	public String getColumnName(int column) {
		return Column.values()[column].name(); 
	}
	
	@Override
	public int getRowCount() {
		return mData.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ChatUserData data = mData.get(rowIndex);
		if (data != null) {
			switch (Column.values()[columnIndex]) {
				case Status: return data.mStatus;
				case Country: return data.mCountry;
				case Rank: return data.mRank;
				case Name: return data.mName;
			}
		}
		return null;
	}

	public void Insert(String name, ChatUserData data) {
		int row = mData.size();
		mMap.put(name, row);
		mData.insertElementAt(data, row);
		fireTableRowsInserted(row, row);
		mMap.clear();
		for (int i = 0; i < mData.size(); i++)
			mMap.put(mData.get(i).mName, i);
	}

	public void Insert(String status, String country, Rank rank, String name) {
		ChatUserData cud = new ChatUserData(status, country, rank, name);
		Insert(name, cud);
	}
	
	public void setValueAt(Object value, int row, int col) {
		ChatUserData data = mData.get(row);
		switch (Column.values()[col]) {
			case Status: data.mStatus = (String) value; break;
			case Country: data.mCountry = (String) value; break;
			case Rank: data.mRank = (String) value; break;
			case Name: data.mName = (String) value; break;
		}
		fireTableCellUpdated(row, col);
	}

	public void setValueAt(String status, String username, int col) {
		int row = mMap.get(username);
		setValueAt(status, row, col);
	}
}