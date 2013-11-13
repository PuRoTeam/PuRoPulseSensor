package it.uniroma2.pulsesensor.servlet;

import it.uniroma2.pulsesensor.crypto.SHA256;
import it.uniroma2.pulsesensor.database.MysqlConnect;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.org.mozilla.javascript.internal.ast.ForInLoop;

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
		String prova = "prova";
		String sha = SHA256.getMsgDigest(prova);
		System.out.println(sha);
		
		byte[] data = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		String msgDigest = Hex.encodeHexString(data);
		System.out.println(msgDigest);
		msgDigest = new String(Hex.encodeHex(data));
		System.out.println(msgDigest);
		
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
