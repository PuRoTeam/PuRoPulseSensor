<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Math"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="servlet.Shared"%>
<%@ page import="java.lang.NumberFormatException"%>
<%@ page import="org.json.JSONArray"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="data.Point"%>

<%
	/*
	//singolo valore salvato come variabile d'applicazione
	ServletContext context = getServletContext();
	Double point_var = (Double)context.getAttribute("Random");

	int max = 100;
	double random = Math.random()*max;
	JSONObject jObject = new JSONObject().put("random", point_var);
	out.println(jObject.toString());*/
	//System.out.println("SHARED: " + request.getMethod());
	
	String selectedUid = request.getParameter("uid");
	long uid = -1;
	
	try
	{
		if(selectedUid != null)
		{
			uid = Long.parseLong(selectedUid);
		
			Shared singleton = Shared.getInstance();
			ArrayList<Point> points = singleton.getPointsByUid(uid);
			
			if(points == null)
			{
				out.println("[]");
				return;
			}
			//for(int i = 0; i < points.size(); i++)
			//	System.out.println("GET: " + points.get(i).getValue() + " " + points.get(i).getTimestamp()); 
				
			JSONArray json = new JSONArray(points);
			
			out.println(json.toString());
		}
	}
	catch(NumberFormatException e)
	{
		out.println("[]"); //array vuoto
	}
	
%>
