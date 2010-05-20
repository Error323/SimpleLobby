package com.spring.tasclient.simplelobby.unitsync;

import unitsync.MapInfo;

public class LobbyMapInfo {
	public String mDescription;
	public int mTidalStrength;
	public int mGravity;
	public float mMaxMetal;
	public int mExtractorRadius;
	public int mMinWind;
	public int mMaxWind;
	public int mWidth;
	public int mHeight;
	public int[] mXPositions;
	public int[] mZPositions;
	public String mAuthor;
	
	public  LobbyMapInfo(MapInfo mapinfo) {
		mDescription = new String(mapinfo.description);
		mTidalStrength = mapinfo.tidalStrength;
		mGravity = mapinfo.gravity;
		mMaxMetal = mapinfo.maxMetal;
		mExtractorRadius = mapinfo.extractorRadius;
		mMinWind = mapinfo.minWind;
		mMaxWind = mapinfo.maxWind;
		mWidth = mapinfo.width;
		mHeight = mapinfo.height;
		mXPositions = mapinfo.positions[0].getPointer().getIntArray(0, mapinfo.posCount);
		mZPositions = mapinfo.positions[1].getPointer().getIntArray(0, mapinfo.posCount);
	}
	
	public String toString() {
		String s = "";
		s += "Description      " + mDescription + "\n";
		s += "TidalStrength    " + mTidalStrength + "\n";
		s += "Gravity          " + mGravity + "\n";
		s += "MaxMetal         " + mMaxMetal + "\n";
		s += "ExtractionRadius " + mExtractorRadius + "\n";
		s += "MinWind          " + mMinWind + "\n";
		s += "MaxWind          " + mMaxWind + "\n";
		s += "Width            " + mWidth + "\n";
		s += "Height           " + mHeight + "\n";
		s += "Positions\n";
		for (int i = 0; i < mXPositions.length; i++) {
			s += "  " + (mXPositions[i]) + "," + (mZPositions[i]) + "\n";
		}
		if (mAuthor != null)
			s += "Author           " + mAuthor + "\n";
		return s;
	}
}