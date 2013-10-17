package database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    	long newTimestamp = point.getTimestamp();
    	
    	String query = "INSERT INTO " + tpTableName +" (" + tpUid + ", "+ tpValue + ", " + tpTimestamp + ") " + 
    				   "VALUES (" + newUid +"," + newValue + "," + newTimestamp + ")";
    	return insert(query);
    }
    
    
    
    public ArrayList<Point> getArrayOfPointsFromQuery(String query) throws SQLException
    {
    	ResultSet resultSet = query(query);
    	
    	if(resultSet == null)
    		return null;
    	
    	ArrayList<Point> pointlist = new ArrayList<Point>();
    	
	    while (resultSet.next()) 
	    {
	    	long newUid = resultSet.getLong("uid");
	    	double newDouble = resultSet.getDouble("value");
	    	long newTimestamp = resultSet.getLong("timestamp");
	    	    			
	    	Point newPoint = new Point(newUid, newDouble, newTimestamp);
	    	pointlist.add(newPoint);   
	    }
	    resultSet.close();
	    
    	return pointlist;
    }

    public ArrayList<Point> getPointsByUidAndDate(Long uid, Long dateFrom, Long dateTo) throws SQLException
    {
    	String tpTableName = TableInfo.TablePoint.toString();
    	String tpTimestamp = TableInfo.TPointTimestamp.toString();
    	String tpUid = TableInfo.TPointUid.toString(); 
    	
    	String query = "SELECT * FROM " + tpTableName + " WHERE 1"; //"WHERE 1" è comodissimo per la concatenazione
    	
    	if(uid != null)
    		query += " AND " + tpUid + " = " + uid;
    	if(dateFrom != null)
    		query += " AND " + tpTimestamp + " >= " + dateFrom;
    	if(dateTo != null)
    		query += " AND " + tpTimestamp + " <= " + dateTo;

    	System.out.println("MysqlConnect: " + query);
    	
    	ArrayList<Point> pointlist = getArrayOfPointsFromQuery(query);
    	
    	return pointlist;
    }
    
    /* ------------------------------------------Funzioni Di Test-------------------------------------- */
    /* ------------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------------ */

    public static void main(String[] args)
    {
    	//testInsertSelect();
    	testGetPointsByDate();    	
    }
 
    public static void testGetPointsByDate()
    {	
    	GregorianCalendar start = new GregorianCalendar();
    	start.set(Calendar.DATE, 17);
    	
    	GregorianCalendar end = new GregorianCalendar();
    	end.set(Calendar.DATE, 17);
    	
    	/*GregorianCalendar gc = new GregorianCalendar();
    	int anno = gc.get(Calendar.YEAR);
    	int mese = gc.get(Calendar.MONTH) + 1;
    	int giorno = gc.get(Calendar.DATE);
    	int ore = gc.get(Calendar.HOUR);
    	int min = gc.get(Calendar.MINUTE);
    	int sec = gc.get(Calendar.SECOND);
    	int msec = gc.get(Calendar.MILLISECOND);
    	
    	System.out.println(gc.getTimeInMillis());    	
    	System.out.println(giorno + "/" + mese + "/" + anno + " " + ore + ":" + min + ":" + sec + ":" + msec);*/
    	
    	MysqlConnect mysql = MysqlConnect.getDbCon();    	
    	
    	try 
    	{
    		//start.getTimeInMillis()
    		//end.getTimeInMillis()
    		ArrayList<Point> points = mysql.getPointsByUidAndDate(new Long(1), null, null);
    		
			System.out.println("Numero di punti: " + points.size());
			
			for(int i = 0; i < points.size(); i++)
			{
				Point p = points.get(i);
		        System.out.println(p.getUid());
		        System.out.println(p.getValue());
		        System.out.println(p.getTimestamp());//.getTime());
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