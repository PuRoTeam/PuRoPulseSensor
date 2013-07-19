package database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.Connection;

import data.Point;

public final class MysqlConnect 
{
    public Connection conn;
    private Statement statement;
    public static MysqlConnect db;
    
    private MysqlConnect() 
    {
        String url= "jdbc:mysql://localhost:3306/";
        String dbName = "chart";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "stealer";
        
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection)DriverManager.getConnection(url+dbName,userName,password);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized MysqlConnect getDbCon() 
    {
        if ( db == null ) {
            db = new MysqlConnect();
        }
        return db; 
    }
    /**
     *
     * @param query The query to be executed (select)
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
        statement = db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
    
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public int insert(String insertQuery) throws SQLException 
    {
        statement = db.conn.createStatement();
        int result = statement.executeUpdate(insertQuery);
        return result; 
    }
    
    /**
     * @desc Method to insert new point
     * @param point Point to add
     * @return boolean
     * @throws SQLException
     */
    public int insertPoint(Point point) throws SQLException 
    {
    	String tpTableName = TableInfo.TablePoint.toString();
    	String tpUid = TableInfo.TPointUid.toString();
    	String tpValue = TableInfo.TPointValue.toString();
    	String tpTimestamp = TableInfo.TPointTimestamp.toString();
    	
    	long newUid = point.getUid();
    	double newValue = point.getValue();
    	long newTimestamp = point.getTimeStamp().getTime();
    	
    	String query = "INSERT INTO " + tpTableName +" (" + tpUid + ", "+ tpValue + ", " + tpTimestamp + ") " + 
    				   "VALUES (" + newUid +"," + newValue + "," + newTimestamp + ")";
    	return insert(query);
    }
    
    public ArrayList<Point> getPointsByDate(Date start, Date end) throws SQLException
    {
    	String tpTableName = TableInfo.TablePoint.toString();
    	String tpTimestamp = TableInfo.TPointTimestamp.toString();
    	
    	long lstart = start.getTime();
    	long lend = end.getTime();
    	
//    	SELECT * 
//    	FROM  `point` 
//    	WHERE TIMESTAMP > 1 & TIMESTAMP <2000000000000000000000
//    	LIMIT 0 , 30
    	
    	String query = "SELECT * FROM " + tpTableName + " " + 
    				   "WHERE " + tpTimestamp + " > " + lstart + " & " + tpTimestamp + " < " + lend;
    	
    	System.out.println(query);
    	ResultSet rs = query(query);
    	
    	ArrayList<Point> pointlist = new ArrayList<Point>();
    	
	    while (rs.next ()) 
	    {
	    	long newUid = rs.getLong("uid");
	    	double newDouble = rs.getDouble("value");
	    	long newTimestamp = rs.getLong("timestamp");
	    	    			
	    	Point newPoint = new Point(newUid, newDouble, newTimestamp);
	    	pointlist.add(newPoint);   
	    }
    	//devo chiudere resultset?
    	return pointlist;
    }
    
    public static void main(String[] args)
    {
    	//testInsertSelect();
    	testGetPointsByDate();    	
    }
 
    public static void testGetPointsByDate()
    {
    	Date lstart = new Date(0);    	
    	Date lend = new Date(2030, 6, 1);
    	System.out.println(lstart);
    	System.out.println(lend);
    	
    	MysqlConnect mysql = MysqlConnect.getDbCon();
    	
    	try 
    	{
			ArrayList<Point> points = mysql.getPointsByDate(lstart, lend);
			
			for(int i = 0; i < points.size(); i++)
			{
				Point p = points.get(i);
		        System.out.println(p.getUid());
		        System.out.println(p.getValue());
		        System.out.println(p.getTimeStamp());
		        System.out.println("----");
			}
		} 
    	catch (SQLException e) 
    	{ e.printStackTrace(); }
    }
    
    public static void testInsertSelect()
    {
    	MysqlConnect mysql = MysqlConnect.getDbCon();
		try {
			int iteration = 1;
			for(int i = 0; i < iteration; i++) {
				
				long uid = 1;
				double value = Math.random()*100;
				long timestamp = System.currentTimeMillis();
				
				Point point = new Point(uid, value, timestamp);
					
				int result = mysql.insertPoint(point);
				
				System.out.println("Risultato inserimento: " + result);
				
				ResultSet rs = mysql.query("SELECT * FROM point");
				
				int count = 0;
				
			    while (rs.next ()) {
			        System.out.println(rs.getString("uid"));
			        System.out.println(rs.getString("value"));
			        System.out.println(rs.getString("timestamp"));
			        System.out.println("----");
			        count++;
			    }
			    System.out.println("count: " + count);
			}
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
}