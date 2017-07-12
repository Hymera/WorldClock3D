package time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SystemDateTime {
	private static Calendar cal;
	private static SimpleDateFormat sdf;
	
	public static String now(String dateFormat, String gmt) {
		
		if (gmt != null) {
		cal = Calendar.getInstance();
		sdf = new SimpleDateFormat(dateFormat);
		sdf.setTimeZone(TimeZone.getTimeZone(gmt));
		} else {
			cal = Calendar.getInstance();
			sdf = new SimpleDateFormat(dateFormat);
		}
		
		return sdf.format(cal.getTime());
	}
}