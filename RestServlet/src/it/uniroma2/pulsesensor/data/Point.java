package it.uniroma2.pulsesensor.data;

import java.util.GregorianCalendar;

public class Point
{
	private long uid;
	private double value;
	//private GregorianCalendar timestamp;
	private long timestamp;
	
	public Point(long uid, double value, long timestamp)
	{
		this.uid = uid;
		this.value = value;
		this.timestamp = timestamp;
		//this.timestamp = new GregorianCalendar();
		//this.timestamp.setTimeInMillis(timestamp);
	}
	
	public long getUid()
	{
		return uid;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	/*public GregorianCalendar getTimestamp()
	{
		return timestamp;
	}*/
}
