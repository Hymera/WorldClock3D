package io.mouse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import renderPanel.RenderPanel;

public class MouseInput extends MouseAdapter implements MouseListener, MouseMotionListener {
	RenderPanel renderPanel;
	
	public MouseInput(RenderPanel renderPanel) {
		this.renderPanel = renderPanel;
	}
	
	// MouseEvent for mouse
	public void mousePressed(MouseEvent e) {
		renderPanel.mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e) {
		renderPanel.mouseReleased(e);
	}
	
	public void mouseDragged(MouseEvent e) {
		renderPanel.mouseDragged(e);
	}
}