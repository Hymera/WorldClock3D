package time;

import java.util.TimerTask;

import renderPanel.RenderPanel;

public class MyTimerTask extends TimerTask
{
	RenderPanel renderPanel;
	
	public MyTimerTask(RenderPanel renderPanel) {
		this.renderPanel = renderPanel;
	}
	
	public void run()
	{
		renderPanel.run();
	}
}