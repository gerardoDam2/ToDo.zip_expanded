package dad.todo.ui.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
	
	public static Date localDateToDate(LocalDate fecha,LocalTime hora){
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.DAY_OF_MONTH,fecha.getDayOfMonth());
		calendar.set(Calendar.MONTH,fecha.getMonthValue()-1);
		calendar.set(Calendar.YEAR,fecha.getYear());
		
		calendar.set(Calendar.HOUR_OF_DAY,hora.getHour());
		calendar.set(Calendar.MINUTE,hora.getMinute());
		calendar.set(Calendar.SECOND,hora.getSecond());
		
		return calendar.getTime();
	}
	
	
	  

}
