package mainFrame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.util.Timer;

import javax.swing.JFrame;

import renderPanel.RenderPanel;
import time.MyTimerTask;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private MyTimerTask timertask;
	private int frameWidth = 810;
	private int frameHeight = 600;
	private RenderPanel renderPanel = new RenderPanel();
	
	public MainFrame() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add("Center", renderPanel);
		
		setTitle("Sphere Orbit");
		setResizable(true);
		setSize(frameWidth, frameHeight);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		timerTaskRun();
	}
	
	// keyEvent for Keyboard
	public void keyPressed(KeyEvent e) {
		this.requestFocus();
		renderPanel.keyPressed(e);
	}
	
	public void timerTaskRun()
	{
		Timer timer = new Timer();
		timertask = new MyTimerTask(renderPanel);
		timer.scheduleAtFixedRate(timertask, 0, 100);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainFrame frame = new MainFrame();
	}
}