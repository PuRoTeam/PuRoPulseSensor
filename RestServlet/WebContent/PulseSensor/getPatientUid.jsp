<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.json.JSONArray"%>
<%@ page import="it.uniroma2.pulsesensor.database.MysqlConnect" %>
<%@ page import="java.sql.SQLException" %>

<%
try
{
	MysqlConnect mysql = MysqlConnect.getDbCon();
	ArrayList<Long> uidList = mysql.getPatientUid();
	JSONArray jsonArray = new JSONArray(uidList);
	out.println(jsonArray);
}
catch(SQLException e)
{
	e.printStackTrace();
}
%>
