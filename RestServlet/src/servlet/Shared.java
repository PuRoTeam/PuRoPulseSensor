package servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import data.Point;

public class Shared 
{
    private static Shared instance = null;
    
    private Map<Long, ArrayList<Point>> points = Collections.synchronizedMap(new HashMap<Long, ArrayList<Point>>());
        
    private Shared() {}
 
    public static synchronized Shared getInstance() 
    {
        if (instance == null) 
        	instance = new Shared();
        return instance;
    }
    
    public ArrayList<Point> getPointsByUid(long uid)
    {
    	return points.get(new Long(uid));
    }
    
    public void putPointsByUid(long uid, ArrayList<Point> newPoints)
    {
    	points.put(new Long(uid), newPoints);
    }
    
    public static void main(String args[])
    {
    	Shared sh1 = Shared.getInstance();
    	
    	long uid1 = 1;
    	Point a = new Point(uid1,1,1);
    	Point b = new Point(uid1,2,1);
    	ArrayList<Point> ap1 = new ArrayList<Point>();
    	ap1.add(a);
    	ap1.add(b);
    	sh1.putPointsByUid(uid1, ap1);
    	
    	System.out.println(sh1.getPointsByUid(uid1));
    	
    	Shared sh2 = Shared.getInstance();
    	
    	long uid2 = 2;
    	Point c = new Point(uid2,3,4);
    	Point d = new Point(uid2,2,1);
    	ArrayList<Point> ap2 = new ArrayList<Point>();
    	ap2.add(c);
    	ap2.add(d);
    	sh2.putPointsByUid(uid2, ap2);
    	
    	System.out.println(sh2.getPointsByUid(uid1));
    	System.out.println(sh2.getPointsByUid(uid2));
    	
    	System.out.println(sh1.getPointsByUid(uid2));
    	    	
    	//JSONArray ar = new JSONArray(ap1);
    	//System.out.println(ar.toString());
    }
}