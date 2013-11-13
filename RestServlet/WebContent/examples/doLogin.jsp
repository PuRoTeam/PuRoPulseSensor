<%@ page import="crypto.SHA256" %>
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
			
			password = SHA256.getMsgDigest(password);
			
			User user = mysql.userExists(userName, password);	
			
			if(user != null){//successo	
				session.setAttribute("userName", userName);
				response.sendRedirect("home.jsp"); //redirect a home
			}
			else{//fallimento	
		        String message = "No user or password matched" ;
		        response.sendRedirect("login.jsp?error=" + message);
			}
		}
		else{
			String message = "No database found" ;
	        response.sendRedirect("login.jsp?error=" + message);
		}
	}
	catch(Exception e)
	{
		String message = "Database error" ;
        response.sendRedirect("login.jsp?error=" + message);		
	}
%>