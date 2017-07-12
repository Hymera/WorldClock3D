package io.keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import renderPanel.RenderPanel;

public class KeyInput extends KeyAdapter implements KeyListener {
	RenderPanel renderPanel;
	
	public KeyInput(RenderPanel renderPanel) {
		this.renderPanel = renderPanel;
	}
	
	// keyEvent for Keyboard
	public void keyPressed(KeyEvent e) {
		renderPanel.keyPressed(e);
	}
}