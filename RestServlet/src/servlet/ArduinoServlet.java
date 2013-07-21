package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Point;
import database.MysqlConnect;

@WebServlet("/ArduinoServlet")
public class ArduinoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ArduinoServlet() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("X");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{		
		//singleValue(request, response);
		multivalue(request, response);	
	}
	
	//ricevo un array di punti
	public void multivalue(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String json = request.getParameter("JSON"); //array di punti
		PrintWriter out = response.getWriter();
					
		/* TODO da modificare in base a come è gestita la cosa da arduino - array */
		try 
		{
			JSONArray jarray = new JSONArray(json);
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
						
						//GregorianCalendar curGTimestamp = new GregorianCalendar();
						//curGTimestamp.setTimeInMillis(curTimestamp);
						
						Point newPoint = new Point(curUidJsonElement, curValue, curTimestamp);
						
						pointsWithSameUid.add(newPoint);
					}
					
					singleton.putPointsByUid(curUidInArray, pointsWithSameUid);					
				}
				
				for(int j = 0; j < pointsWithSameUid.size(); j++)
					out.println(pointsWithSameUid.get(j).getUid() + " " + pointsWithSameUid.get(j).getTimestamp() + " " + pointsWithSameUid.get(j).getValue());
				
			}			
		} 
		catch (JSONException e) 
		{ e.printStackTrace(); }
		
	}
	
	//ricevo solo un valore da arduino (da non cancellare finchè non si definisce per bene la comunicazione)
	public void singleValue(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String json = request.getParameter("JSON"); //singolo valore
		PrintWriter out = response.getWriter();
		
		try 
		{
			JSONObject jobj = new JSONObject(json);
			
	        long uid = jobj.getLong("uid");       
			double value = jobj.getDouble("value");
			long timestamp = jobj.getLong("timestamp");
			
			MysqlConnect mysql = MysqlConnect.getDbCon();
			
			Point point = new Point(uid, value, timestamp);
				
			int result = mysql.insertPoint(point);		
			
			if(result != 0)
			{
				out.println(point.getUid());			
				out.println(point.getValue());
				out.println(point.getTimestamp());
				
				ServletContext context = getServletContext();
				context.setAttribute("Random", point.getValue()); //sarebbe da settare "Random + uid"			
			}
		}
		catch (JSONException e) 
		{ e.printStackTrace(); } 
		catch (SQLException e) 
		{ e.printStackTrace(); }
	}
}