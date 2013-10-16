<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Math"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="servlet.Shared"%>
<%@ page import="java.lang.NumberFormatException"%>
<%@ page import="org.json.JSONArray"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="data.Point"%>
<%@ page import="database.MysqlConnect" %>
<%@ page import="java.sql.SQLException" %>

<%	
	String selectedUid = request.getParameter("uid");
	String strDateFrom = request.getParameter("dateFrom");
	String strDateTo = request.getParameter("dateTo");
	
	Long uid = null;
	Long dateFrom = null;
	Long dateTo = null;
	
	try
	{		
		if(selectedUid != null)
			uid = Long.parseLong(selectedUid);
		if(strDateFrom != null)
			dateFrom = Long.parseLong(strDateFrom);
		if(strDateTo != null)
			dateTo = Long.parseLong(strDateTo);
			
		MysqlConnect mysql = MysqlConnect.getDbCon();
		
		ArrayList<Point> points = mysql.getPointsByUidAndDate(uid, dateFrom, dateTo);
		
		JSONArray json = new JSONArray(points); //non posso convertire direttamente in json, perchè devo levare la colonna uid
		
		out.println(json.toString());	
	}
	catch(SQLException e)
	{
		out.println("[]"); //array vuoto
		return;
	}
	catch(NumberFormatException e)
	{
		out.println("[]"); //se un valore non valido, invio array vuoto
		return;
	}
	
%>