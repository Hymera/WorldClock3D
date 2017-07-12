package renderPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import time.SystemDateTime;
import utility.CustomFont;

@SuppressWarnings("serial")
public class PanelFrontText extends JPanel {
	private String timeformat = "HH:mm:ss";
	private String timezone = "LOCAL TIME";
	private String timeNow = "00:00:00";
	private int width = 145;
	private int height = 70;
	
	public PanelFrontText(int x, int y) {
		setBackground(Color.BLACK);
		setBounds(x - width, y - height, width, height);
	}
	
	public void update(int x, int y) {
		timeNow = SystemDateTime.now(timeformat, null);
		setLocation(x - width, y - height);
	}
	
	public void paintComponent(Graphics g) {
		// magic used to stop flickering when redrawing for animation later.
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;
		// make drawing antialias
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		
		// front time
		g2D.setColor(Color.RED);
		g.setFont(CustomFont.createFont(Font.BOLD, 25));
		g2D.drawString(timezone, 0, 20);
		g.setFont(CustomFont.createFont(Font.PLAIN, 50));
		g2D.drawString(timeNow, 0, 62);
		
		g2D.dispose();
	}
}