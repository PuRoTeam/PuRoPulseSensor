package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String json = request.getParameter("JSON");
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
				out.println(point.getTimeStamp());
				
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