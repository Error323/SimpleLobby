package com.spring.tasclient.simplelobby.unitsync;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import unitsync.MapInfo;
import unitsync.UnitsyncLibrary;

/**
 * The unitsync lib is compiled with:
 * java -jar jnaerator-0.9.3.jar -library unitsync -scanSymbols -nocpp -noLibBundle -direct -preferJavac unitsync.h unitsync_api.h
 * note that 
 * @author fhuizing
 *
 */
public class UnitSync {
	public static void main(String[] args) {
		Runtime.getRuntime().load("/usr/local/lib/libunitsync.so");
		UnitSync us = new UnitSync();
		us.PopulateArchiveList();
		System.out.println(us.GetMapInfo("SmallDivide"));
	}
	
	private HashMap<String, Integer> mMapHash;
	private HashMap<String, Integer> mMapUnchainedHash;
	private HashMap<String, String>  mMapArchiveName;
	private HashMap<String, LobbyMapInfo> mMapInfo;
	private HashMap<String, Integer> mModHash;
	private HashMap<String, Integer> mModUnchainedHash;
	private HashMap<String, String>  mModArchiveName;
	
	public UnitSync() {
		mMapHash = new HashMap<String, Integer>();
		mMapUnchainedHash = new HashMap<String, Integer>();
		mModHash = new HashMap<String, Integer>();
		mModUnchainedHash = new HashMap<String, Integer>();
		mMapArchiveName = new HashMap<String, String>();
		mModArchiveName = new HashMap<String, String>();
		mMapInfo = new HashMap<String, LobbyMapInfo>();
		UnitsyncLibrary.Init(false, 0);
		GetError();
	}
	
	public void PopulateArchiveList() {
		int nummaps = UnitsyncLibrary.GetMapCount();
		for (int i = 0; i < nummaps; i++) {
			String name = null, archivename = null;
			int hash = 0, unchainedhash = 0;
			try {
				name = UnitsyncLibrary.GetMapName(i).getString(0);
				hash = UnitsyncLibrary.GetMapChecksum(i);
				mMapHash.put(name, hash);
				int count = UnitsyncLibrary.GetMapArchiveCount(name);
				if (count > 0) {
					archivename = UnitsyncLibrary.GetMapArchiveName(0).getString(0);
					unchainedhash = UnitsyncLibrary.GetArchiveChecksum(archivename);
					mMapUnchainedHash.put(name, unchainedhash);
					mMapArchiveName.put(name, archivename);
				}
			} catch (NullPointerException npe) {
				GetError();
				continue;
			}
		}
		
		int nummods = UnitsyncLibrary.GetPrimaryModCount();
		for (int i = 0; i < nummods; i++) {
			String name = null, archivename = null;
			int hash = 0, unchainedhash = 0;
			try {
				name = UnitsyncLibrary.GetPrimaryModName(i).getString(0);
				hash = UnitsyncLibrary.GetPrimaryModChecksum(i);
				mModHash.put(name, hash);
				int count = UnitsyncLibrary.GetPrimaryModArchiveCount(i);
				if (count > 0) {
					archivename = UnitsyncLibrary.GetPrimaryModArchive(i).getString(0);
					unchainedhash = UnitsyncLibrary.GetArchiveChecksum(archivename);
					mModUnchainedHash.put(name, unchainedhash);
					mModArchiveName.put(name, archivename);
				}
			} catch (NullPointerException npe) {
				GetError();
				continue;
			}
		}
	}
	
	public LobbyMapInfo GetMapInfo(String name) {
		MapInfo info = new MapInfo();
		try {
		UnitsyncLibrary.GetMapInfo(name, info);
		} catch (NullPointerException npe) {
			GetError();
		}
		return new LobbyMapInfo(info);
	}
	
	public String GetSpringVersion() {
		String s = "";
		try {
			s = UnitsyncLibrary.GetSpringVersion().getString(0);
		} catch (NullPointerException npe) {
			GetError();
		}
		return s;
	}
	
	public String GetWritableDataDir() {
		String s = "";
		try {
			s = UnitsyncLibrary.GetWritableDataDirectory().getString(0);
		} catch (NullPointerException npe) {
			GetError();
		}
		return s;
	}
	
	private String GetError() {
		String error = "";
		try {
			while (true) {
				error += UnitsyncLibrary.GetNextError().getString(0) + "\n";
				System.err.println(error);
			}
		} catch (NullPointerException npe) {
			
		}
		return error;
	}
	
	private void WriteImageToCache(BufferedImage img, String path, String name) {
		try {
		    // Save as PNG
		    File file = new File(path+"/"+name+".png");
		    ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage CreateImage(String archivename) {
		int miplevel = 3;
		int width = 1024 >> miplevel;
		int height = 1024 >> miplevel;
		short[] colours = UnitsyncLibrary.GetMinimap(archivename, miplevel).getShortArray(0, width*height);		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		WritableRaster rast = img.getRaster();
		rast.setDataElements(0, 0, width, height, colours);
		return img;
	}
	
	protected void finalize() throws Throwable {
		UnitsyncLibrary.UnInit();
		super.finalize();
	}
}
