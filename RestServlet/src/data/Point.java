package data;

import java.util.Date;

public class Point 
{
	private long uid;
	private double value;
	private Date timestamp;
	
	public Point(long uid, double value, long timestampms)
	{
		this.uid = uid;
		this.value = value;		
		this.timestamp = new Date(timestampms);
	}
	
	public long getUid()
	{
		return uid;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public Date getTimeStamp()
	{
		return timestamp;
	}
}
