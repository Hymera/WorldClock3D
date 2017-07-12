package utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoadImage {
	private int[][][] rgbList;
	
	public LoadImage(int rowCount, int columnCount) throws IOException {
		BufferedImage img = null;
		String path = "/map/worldMap.png";
		
		rgbList = new int[rowCount][columnCount][4];
		int x = 0;
		int y = 0 ;
		int pixel = 0;
		int alpha = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		
		// read image
		try {
			img = ImageIO.read(ResourceLoader.load(path));
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			img.flush();
		}
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		// get the responsible coordinates from a pre defined raster
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				x = (int) MapRange.map(j, 0, columnCount, 0, width);
				y = (int) MapRange.map(i, 0, rowCount, 0, height);
				// get pixel value
				pixel = img.getRGB(x, y);
				// get alpha
				alpha = (pixel >> 24) & 0xff;
				rgbList[i][j][0] = alpha;
				// get red
				red = (pixel >> 16) & 0xff;
				rgbList[i][j][1] = red;
				// get green
				green = (pixel >> 8) & 0xff;
				rgbList[i][j][2] = green;
				// get blue
				blue = pixel & 0xff;
				rgbList[i][j][3] = blue;
				
//				System.out.println(pixel + " " + alpha + " " + red + " " + green + " " + blue);
			}
		}
	}
	
	public int[][][] getRGBVectorList() {
		return rgbList;
	}
}