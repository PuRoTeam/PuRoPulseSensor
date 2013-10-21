package servlet;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Prov {

	public static void main(String args[])
	{
		long l = 1385852400000L;
		Date d = new Date(l);
		System.out.println("javascript: " + d.toString());
		
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.set(Calendar.YEAR, 2013);
    	gc.set(Calendar.MONTH, Calendar.OCTOBER);
    	gc.set(Calendar.DATE, 31);
    	gc.set(Calendar.HOUR, 0);
    	gc.set(Calendar.MINUTE, 0);
    	gc.set(Calendar.SECOND, 0);
    	gc.set(Calendar.MILLISECOND, 0);
    	
    	/*int anno = gc.get(Calendar.YEAR);
    	int mese = gc.get(Calendar.MONTH) + 1;
    	int giorno = gc.get(Calendar.DATE);
    	int ore = gc.get(Calendar.HOUR);
    	int min = gc.get(Calendar.MINUTE);
    	int sec = gc.get(Calendar.SECOND);
    	int msec = gc.get(Calendar.MILLISECOND);
    	System.out.println(giorno + "/" + mese + "/" + anno + " " + ore + ":" + min + ":" + sec + ":" + msec);
    	*/
    	System.out.println("java: " + gc.getTimeInMillis());    	
    			
	}
}
