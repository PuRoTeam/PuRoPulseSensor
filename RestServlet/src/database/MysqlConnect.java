package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    public static Connection conn;
    private static Statement statement;
    public static MysqlConnect db;
    
    private static final String dbConfFileName = "puro_db.conf";
    private static final String url= "jdbc:mysql://localhost:3306/";
    private static final String driver = "com.mysql.jdbc.Driver";

    private MysqlConnect() 
    {
    }
    
    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized MysqlConnect getDbCon() 
    {
    	ArrayList<String> parameters = getConnectionToDbParameters();
    
		if(parameters.size() == 3)
		{
			String dbName = parameters.get(0);
			String userName = parameters.get(1);
			String password = parameters.get(2);
			
	    	try {
	            Class.forName(driver).newInstance();
	            conn = (Connection)DriverManager.getConnection(url + dbName, userName, password);
	            if(conn != null){
	            	if(db == null ) {
	                    db = new MysqlConnect();
	                }
	            }
	        }catch (Exception sqle) {
	            sqle.printStackTrace();
	        }	    				
		}    
		
		return db;
    }
    
    private static ArrayList<String> getConnectionToDbParameters()
    {
    	ArrayList<String> parameters = new ArrayList<String>();
    	
    	String pathToDbConf = System.getProperty("user.home") + System.getProperty("file.separator") + dbConfFileName;
    	
		try (BufferedReader br = new BufferedReader(new FileReader(pathToDbConf)))
		{ 
			String curLine;
 
			while ((curLine = br.readLine()) != null) {
				parameters.add(curLine);
				//System.out.println(curLine);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}   
		return parameters;
    }    
    
    
    /**
     *
     * @param query The query to be executed (select)
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
        statement = conn.createStatement();
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
        statement = conn.createStatement();
        int result = statement.executeUpdate(insertQuery);
        return result; 
    }
 
    public ArrayList<Long> getPatientUid()  throws SQLException
    {
    	String tpTableName = TableInfo.TablePatient.toString();
    	String tpUid = TableInfo.TPatientUid.toString();
    	
    	String query = "SELECT " + tpUid + " FROM " + tpTableName;
    	//System.out.println(query);
    	
    	ResultSet resultSet = query(query);
    	
    	if(resultSet == null)
    		return null;
    	
    	ArrayList<Long> uidList = new ArrayList<Long>();
    	
	    while (resultSet.next()) 
	    {
	    	Long newUid = new Long(resultSet.getLong(tpUid));
	    	uidList.add(newUid);   
	    }
	    resultSet.close();
	    
    	return uidList;
    	
    }
    
    //# e -- sono commenti sql
    //parameter: "' OR 'a'='a' #"
    //SELECT * FROM user WHERE username='' OR 'a'='a' #' AND password='xxx'
    //la query di sopra restituisce true
    public String avoidInjection(String parameter)
    {
    	String escapedString = parameter;
    	escapedString = parameter.replaceAll("'", "\\\\'");
    	escapedString = escapedString.replaceAll("#", "");
    	escapedString = escapedString.replaceAll("--", "");
    	escapedString = escapedString.replaceAll(";", "");
    	return escapedString;
    }
    
    public User userExists(String username, String password) throws SQLException
    {
    	username = avoidInjection(username);
    	String tuTableName = TableInfo.TableUser.toString();
    	String tuFirstName = TableInfo.TUserFirstName.toString();
    	String tuLastName = TableInfo.TUserLastName.toString();
    	String tuUserName = TableInfo.TUserUserName.toString();
    	String tuPassword = TableInfo.TUserPassword.toString();
    	
    	String query = "SELECT * FROM " + tuTableName + " WHERE "
    				   + tuUserName + "='" + username + "' AND " + tuPassword + "='" + SHA256.getMsgDigest(password) + "'";
    	//System.out.println(query);
    	
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
    	//System.out.println(query);
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

    	//System.out.println("MysqlConnect: " + query);
    	
    	ArrayList<Point> pointlist = getArrayOfPointsFromQuery(query);
    	
    	return pointlist;
    }
    
    /* ------------------------------------------Funzioni Di Test-------------------------------------- */
    /* ------------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------------ */

    public static void main(String[] args)
    {
    	testGetPatientUid();
    	//testGetUser();
    	//testInsertUser();
    	//testInsertSelect();
    	//testGetPointsByDate();    	
    }
 
    public static void testGetPatientUid()
    {
    	MysqlConnect mysql = MysqlConnect.getDbCon(); 
    	
    	try 
    	{
			ArrayList<Long> uidList = mysql.getPatientUid();
			for(int i = 0; i < uidList.size(); i++)
				System.out.println(uidList.get(i));
		} 
    	catch (SQLException e) 
    	{ e.printStackTrace(); }
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