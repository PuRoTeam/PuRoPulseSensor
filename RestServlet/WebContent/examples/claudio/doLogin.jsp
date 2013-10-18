<!-- <%@ page language="java" import="java.sql.*" errorPage="" %> -->

<%@ page import="database.MysqlConnect" %>
<%@ page import="data.User" %>
<%
	String userName = request.getParameter("sUserName");
	if(userName == null)
		userName = "";
	
	String password = request.getParameter("sPwd");
	if(password == null)
		password = "";

	MysqlConnect mysql = MysqlConnect.getDbCon();
	User user = mysql.userExists(userName, password);
	
	if(user != null) {//successo	
		session.setAttribute("userName", userName);
		response.sendRedirect("home.jsp"); //redirect a home
	}
	else {//fallimento	
        String message = "No user or password matched" ;
        response.sendRedirect("login.jsp?error=" + message);
	}
%>