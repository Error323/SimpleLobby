package test;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.error323.lobby.UnitsyncLibrary;

public class UnitSyncTest {
	public UnitSyncTest() {
		HashMap<String, String> maparchives = new HashMap<String, String>();
		
		UnitsyncLibrary.Init(false, 0);
		int nummaps = UnitsyncLibrary.GetMapCount();
		for (int i = 0; i < nummaps; i++) {
			String name = null, archivename = null;
			int hash = 0, unchainedhash = 0;
			try {
				name = UnitsyncLibrary.GetMapName(i).getString(0);
				hash = UnitsyncLibrary.GetMapChecksum(i);
				int count = UnitsyncLibrary.GetMapArchiveCount(name);
				if (count > 0) {
					archivename = UnitsyncLibrary.GetMapArchiveName(0).getString(0);
					unchainedhash = UnitsyncLibrary.GetArchiveChecksum(archivename);
				}
			} catch (NullPointerException npe) {
				continue;
			}
			try {
				BufferedImage img = createImage(name);
				try {
				    // Save as PNG
				    File file = new File("/home/fhuizing/www/SimpleLobby/"+hash+".png");
				    ImageIO.write(img, "png", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (NullPointerException npe) {
				
			}
		}
	}


	public static void main(String[] args) {
		new UnitSyncTest();
	}
	
	private BufferedImage createImage(String name) {
		int miplevel = 3;
		int width = 1024 >> miplevel;
		int height = 1024 >> miplevel;
		short[] colours = UnitsyncLibrary.GetMinimap(name, miplevel).getShortArray(0, width*height);		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		WritableRaster rast = img.getRaster();
		rast.setDataElements(0, 0, width, height, colours);
		return img;
	}
}
