package data;

import java.util.Date;
import java.util.GregorianCalendar;

public class Point 
{
	private long uid;
	private double value;
	private GregorianCalendar timestamp;
	
	public Point(long uid, double value, long timestampMs)
	{
		this.uid = uid;
		this.value = value;		
		this.timestamp = new GregorianCalendar();
		this.timestamp.setTimeInMillis(timestampMs);
	}
	
	public long getUid()
	{
		return uid;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public GregorianCalendar getTimeStamp()
	{
		return timestamp;
	}
}
