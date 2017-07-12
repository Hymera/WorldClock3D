package utility;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class CustomFont {

	public CustomFont() {
	}
	
	public static Font createFont(int style, int size) {
		String path = "/font/agency-fb.ttf";
		InputStream is = ResourceLoader.load(path);
		Font agencyFB = null;
		
		try {
			agencyFB = Font.createFont(Font.TRUETYPE_FONT, is);
			agencyFB = agencyFB.deriveFont(style, size);
		} catch (FontFormatException e) {
			e.printStackTrace();
			System.err.println(path + " not loaded.  Using dialog font.");
			agencyFB = new Font("Dialog", style, size);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(path + " not loaded.  Using dialog font.");
			agencyFB = new Font("Dialog", style, size);
		}
		
		return agencyFB;
	}
}