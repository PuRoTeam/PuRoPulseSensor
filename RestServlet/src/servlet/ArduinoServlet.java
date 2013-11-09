package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import crypto.AES;
import data.Point;
import database.MysqlConnect;

/*
Under normal circumstances, a servlet is only destroyed at shutdown (ie when the application container, such as Tomcat, is shut down). 
Regarding your question about requests, a servlet is designed to handle many requests. It is said that the servlet is application-scoped, whereas the request has its own scope.
*/
@WebServlet("/ArduinoServlet")
public class ArduinoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final boolean DB_FUNZIONANTE = false; //TODO da eliminare, solo per test senza db
	
    public ArduinoServlet() 
    {
        super();
    }

	public void init() throws ServletException 
	{
		super.init();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("GET method of ArduinoServlet");
	} 

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{	
		String paramJson = request.getParameter("JSON");		
		multiCryptoValue(request, response, paramJson);
		
		response.flushBuffer();
	}
	
	//ricevo un array di punti criptato
	public void multiCryptoValue(HttpServletRequest request, HttpServletResponse response, String cryptoJson) 
			throws ServletException, IOException
	{
		//System.out.println("cryptoJson: " + cryptoJson);		
		String clientIP = getRequestIP(request);
		String diffieHellmanKey = Shared.getInstance().getDiffieHellmanKeyFromIP(clientIP);
		//System.out.println("diffieHellmanKey: " + diffieHellmanKey);
		try 
		{
			String plainJson = AES.DecryptIVFromKey(cryptoJson, diffieHellmanKey);
			//System.out.println("plainJson: " + plainJson);
			multivalue(request, response, plainJson);
		} 
		catch (InvalidKeyException e) 
		{ e.printStackTrace(); }
		catch (NoSuchAlgorithmException e) 
		{ e.printStackTrace(); } 
		catch (NoSuchPaddingException e) 
		{ e.printStackTrace(); } 
		catch (IllegalBlockSizeException e) 
		{ e.printStackTrace(); } 
		catch (BadPaddingException e) 
		{ e.printStackTrace(); } 
		catch (NoSuchProviderException e) 
		{ e.printStackTrace(); } 
		catch (InvalidAlgorithmParameterException e) 
		{ e.printStackTrace(); }
	}
	
	//ricevo un array di punti
	public void multivalue(HttpServletRequest request, HttpServletResponse response, String plainJson) 
			throws ServletException, IOException 
	{					
		try 
		{
			JSONArray jarray = new JSONArray(plainJson);
			Shared singleton = Shared.getInstance();
			
			ArrayList<Long> allUidsInJsonArray = new ArrayList<Long>();
			
			int lenght = jarray.length();
			
			//salvo tutti gli uid (una sola volta) che trovo nell'array di punti
			for(int i = 0; i < lenght; i++) 
			{
				JSONObject curJsonElement = jarray.getJSONObject(i);
				
				long curUid = curJsonElement.getLong("uid");
				
				if(!allUidsInJsonArray.contains(curUid))
					allUidsInJsonArray.add(curUid);
			}
			
			//creo un array (per ogni uid) contenente tutti i punti con stesso uid
			for(int i = 0; i < allUidsInJsonArray.size(); i++) 
			{
				long curUidInArray = allUidsInJsonArray.get(i);
				ArrayList<Point> pointsWithSameUid = new ArrayList<Point>();
				
				for(int j = 0; j < lenght; j++)
				{
					JSONObject curJsonElement = jarray.getJSONObject(j);					
					long curUidJsonElement = curJsonElement.getLong("uid");
					
					if(curUidJsonElement == curUidInArray)
					{
						double curValue = curJsonElement.getDouble("value");
						long curTimestamp = curJsonElement.getLong("timestamp");
						
						String clientIP = getRequestIP(request);
						ShareTime st = Shared.getInstance().getShareTimeFromIP(clientIP);
						long now = st.getNow();
						
						long realCurTimeStamp = now + (curTimestamp-st.getTimestampArduino());
						//System.out.println("SERVLET: " + curValue + " " + realCurTimeStamp);
						Point newPoint = new Point(curUidJsonElement, curValue, realCurTimeStamp);
						
						pointsWithSameUid.add(newPoint);
						
						if(DB_FUNZIONANTE)
						{
							MysqlConnect mysql = MysqlConnect.getDbCon();
							int result = mysql.insertPoint(newPoint);
						}	
					}
					
					singleton.putPointsByUid(curUidInArray, pointsWithSameUid);					
				}
				//PrintWriter out = response.getWriter();
				//ArrayList<Point> singletonPointSameUid = singleton.getPointsByUid(curUidInArray);
				
				//for(int j = 0; j < singletonPointSameUid.size(); j++)
				//	out.println(singletonPointSameUid.get(j).getUid() + " " + singletonPointSameUid.get(j).getTimestamp() + " " + singletonPointSameUid.get(j).getValue());				
			}			
		} 
		catch (JSONException e) 
		{ e.printStackTrace(); } 
		catch (SQLException e) 
		{ e.printStackTrace(); }
	}
	
	public String getRequestIP(HttpServletRequest request)
	{
		String clientIP = request.getRemoteAddr();
		//System.out.println("SERVLET - Client IP: " + clientIP);
		return clientIP;
	}
}