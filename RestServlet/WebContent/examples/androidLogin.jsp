<!-- <%@ page language="java" import="java.sql.*" errorPage="" %> -->

<%@page import="java.io.PrintWriter"%>
<%@ page import="database.MysqlConnect" %>
<%@ page import="data.User" %>
<%
	String userName = request.getParameter("sUserName");
	if(userName == null)
		userName = "";
	
	String password = request.getParameter("sPwd");
	if(password == null)
		password = "";

	try
	{
		MysqlConnect mysql = MysqlConnect.getDbCon();
		if(mysql != null){
			User user = mysql.userExists(userName, password);	
			
			if(user != null){//successo	
				out.println("{\"res\":true}");
			}
			else{//fallimento	
				out.println("{\"res\":false}");	
			}
		}
		else{
			out.println("{\"res\":false}");
		}
	}
	catch(Exception e)
	{
		String message = "Database error" ;
        response.sendRedirect("login.jsp?error=" + message);		
	}
%>