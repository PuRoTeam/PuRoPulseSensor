package database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.mysql.jdbc.Connection;

import crypto.SHA256;
import data.Point;
import data.User;

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
 
    public User userExists(String username, String password) throws SQLException
    {
    	String tuTableName = TableInfo.TableUser.toString();
    	String tuFirstName = TableInfo.TUserFirstName.toString();
    	String tuLastName = TableInfo.TUserLastName.toString();
    	String tuUserName = TableInfo.TUserUserName.toString();
    	String tuPassword = TableInfo.TUserPassword.toString();
    	
    	String query = "SELECT * FROM " + tuTableName + " WHERE "
    				   + tuUserName + "='" + username + "' AND " + tuPassword + "='" + SHA256.getMsgDigest(password) + "'";
    	
    	ResultSet resultSet = query(query);
    	
    	if(resultSet == null)
    		return null;
    	
    	User user = null;
    	
	    while(resultSet.next()) 
	    {
	    	String firstName = resultSet.getString(tuFirstName);
	    	String lastName = resultSet.getString(tuLastName);
	    	String userName = resultSet.getString(tuUserName);
	    	user = new User(firstName, lastName, userName, password); //dentro viene fatto l'hash della password
	    }
	    resultSet.close();
	    
    	return user;
    }
    
    public int insertUser(User user) throws SQLException
    {
    	String tuTableName = TableInfo.TableUser.toString();
    	String tuFirstName = TableInfo.TUserFirstName.toString();
    	String tuLastName = TableInfo.TUserLastName.toString();
    	String tuUserName = TableInfo.TUserUserName.toString();
    	String tuPassword = TableInfo.TUserPassword.toString();
    	
    	String firstName = user.getFirstName();
    	String lastName = user.getLastName();
    	String userName = user.getUserName();
    	String password = user.getHashOfPassword();
    	
    	String query = "INSERT INTO " + tuTableName +" (" + tuFirstName + ", "+ tuLastName + ", " + tuUserName + ", " + tuPassword + ") " +
    				   "VALUES ('" + firstName +"','" + lastName + "','" + userName + "','" + password + "')";
    	//INSERT INTO `user`(`name`, `lastname`, `username`, `password`) VALUES ("c", "c", "c", "c")
    	System.out.println(query);
    	return insert(query);
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
    	String tpTimestamp = TableInfo.TPointTimestamp.toString();
    	String tpUid = TableInfo.TPointUid.toString(); 
    	String tpValue = TableInfo.TPointValue.toString();
    	
    	ResultSet resultSet = query(query);
    	
    	if(resultSet == null)
    		return null;
    	
    	ArrayList<Point> pointlist = new ArrayList<Point>();
    	
	    while (resultSet.next()) 
	    {
	    	long newUid = resultSet.getLong(tpUid);
	    	double newDouble = resultSet.getDouble(tpValue);
	    	long newTimestamp = resultSet.getLong(tpTimestamp);
	    	    			
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
    	//testGetUser();
    	testInsertUser();
    	//testInsertSelect();
    	//testGetPointsByDate();    	
    }
 
    public static void testGetUser()
    {
    	MysqlConnect mysql = MysqlConnect.getDbCon(); 
    	try 
    	{
			User user = mysql.userExists("MisterPup", "prova");
			if(user != null)
				System.out.println(user.getUserName() + " " + user.getHashOfPassword());
			else
				System.out.println("User does not exist");
		} 
    	catch (SQLException e) 
    	{ e.printStackTrace(); }
    }
    
    public static void testInsertUser()
    {
    	User user = new User("Damiano", "Rossato", "Tank", "prova");
    	//User user = new User("Claudio", "Pupparo", "MisterPup", "prova");
    	MysqlConnect mysql = MysqlConnect.getDbCon(); 
    	
    	try 
    	{
			mysql.insertUser(user);
		} 
    	catch (SQLException e) 
    	{ e.printStackTrace(); }
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