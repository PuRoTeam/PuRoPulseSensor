package servlet;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.org.mozilla.javascript.internal.ast.ForInLoop;
import database.MysqlConnect;

public class Prova
{
    public static String avoidInjection(String parameter)
    {
    	String escapedString = parameter;
    	escapedString = parameter.replaceAll("'", "\\\\'");
    	escapedString = escapedString.replaceAll("#", "");
    	escapedString = escapedString.replaceAll("--", "");
    	return escapedString;
    }
	
	public static void main(String[] args) throws JSONException, SQLException
	{
		MysqlConnect mysql = MysqlConnect.getDbCon();
		ArrayList<Long> arr = mysql.getPatientUid();
		JSONArray jarray = new JSONArray(arr);
		
		System.out.println(jarray.toString());
		
		for (int i = 0; i < jarray.length(); i++) 
		{
			System.out.println(jarray.getLong(i));			
		}		
		
		System.out.println(System.currentTimeMillis());
		
		String c = "SELECT * FROM user WHERE username='' OR '1'='1' -- AND password='a'";
		c = avoidInjection(c);
		System.out.println(c);
		
		System.out.println(System.getProperty("user.home"));
		
		/*ArrayList<String> a = new ArrayList<String>();
		a.add("claudio");
		a.add("c");
		String[] b = {"a"};
		b = a.toArray(b);
		for (int i = 0; i < b.length; i++)
			System.out.println(b[i]);*/
	}
}
