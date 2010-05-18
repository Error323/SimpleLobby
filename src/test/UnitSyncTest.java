package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.error323.lobby.UnitsyncLibrary;

public class UnitSyncTest {
	public static void main(String[] args) {
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
//			System.out.println(name + " " + archivename + " " + hash + " " + unchainedhash);
			try {
				maparchives.put(name, archivename);
			} catch (NullPointerException npe) {
				
			}
		}
		BufferedImage img = loadImage("SmallDivide.smf");
		try {
		    // Save as PNG
		    File file = new File("/home/fhuizing/www/smalldivide.png");
		    ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static BufferedImage loadImage(String name) {
		int miplevel = 0;
		int width = 512 >> miplevel;
		int height = 512 >> miplevel;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		
		byte[] colors = UnitsyncLibrary.GetMinimap(name, miplevel).getByteArray(0, width*height*2);
		int x = 0, y = 0;
		for (int i = 0, n = width*height*2; i < n; i+=2) {
			int color = 0xFFFF & (colors[i] << 8 | colors[i+1]);
			x = (i/2) % (width-1);
			bi.setRGB(x, y, color);
			if (x == 0)	y++;
		}
		return bi;
	}
}
