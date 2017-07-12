package behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

public class FpsBehavior extends Behavior {
	private WakeupCondition m_WakeupCondition = null;
	private long m_StartTime = 0;
	private final int m_knReportInterval = 100;
	private boolean showFPS = false;
	
	public FpsBehavior() {
		// save the WakeupCriterion for the behavior
		m_WakeupCondition = new WakeupOnElapsedFrames(m_knReportInterval);
	}
	
	public void initialize() {
		// apply the initial WakeupCriterion
		wakeupOn(m_WakeupCondition);
	}
	
	public void setShowFPS(boolean showFPS) {
		this.showFPS = showFPS;
	}
	
	@SuppressWarnings("rawtypes")
	public void processStimulus(Enumeration criteria) {
		while (criteria.hasMoreElements()) {
			WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement();
			
			if (wakeUp instanceof WakeupOnElapsedFrames) {
				if (m_StartTime > 0) {
					final long interval = System.currentTimeMillis() - m_StartTime;
					
					if (showFPS) {
						System.out.println("FPS: " + (m_knReportInterval * 1000) / interval);
					}
				}
				
				m_StartTime = System.currentTimeMillis();
			}
		}
		// assign the next WakeUpCondition
		wakeupOn(m_WakeupCondition);
	}
}