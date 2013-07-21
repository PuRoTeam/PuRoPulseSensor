package servlet;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

public class ABuffo {

	private long uid;
	private double value;
	private long timestamp;
	
	public ABuffo(long uid, double value, long timestamp)
	{
		this.uid = uid;
		this.value = value;
		this.timestamp = timestamp;
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
	
	public static void main(String[] args)
	{
		long uid1 = 1;
		double value1 = 2;
		long timestamp1 = System.currentTimeMillis();		
		ABuffo a = new ABuffo(uid1, value1, timestamp1);
		
		long uid2 = 2;
		double value2 = 3;
		long timestamp2 = System.currentTimeMillis();	
		ABuffo b = new ABuffo(uid2, value2, timestamp2);
		//[{"uid":1,"timeStamp":1374256224200,"value":2},{"uid":2,"timeStamp":1374256224200,"value":3}]
    	ArrayList<ABuffo> buffo1 = new ArrayList<ABuffo>();
    	buffo1.add(a);
    	buffo1.add(b);
    	
    	ArrayList<ABuffo> buffo2 = new ArrayList<ABuffo>();
    	buffo2.add(a);
    	buffo2.add(b);
    	
    	//[[{"uid":1,"timeStamp":1374256612365,"value":2},{"uid":2,"timeStamp":1374256612365,"value":3}],[{"uid":1,"timeStamp":1374256612365,"value":2},{"uid":2,"timeStamp":1374256612365,"value":3}]]
    	ArrayList<ArrayList<ABuffo>> aa = new ArrayList<ArrayList<ABuffo>>();
    	aa.add(buffo1);
    	aa.add(buffo2);
    	
    	//HashMap<Long, ArrayList<ABuffo>> hm = new HashMap<Long, ArrayList<ABuffo>>();
    	//hm.put(uid1, buffo1);
    	//hm.put(uid1, buffo2);
    	
    	JSONArray ar = new JSONArray(aa);
    	System.out.println(ar.toString());
	}
}
