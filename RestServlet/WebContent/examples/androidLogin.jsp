<%@ page import="database.MysqlConnect" %>
<%@ page import="data.User" %>
<%
	String userName = request.getParameter("sUserName");
	if(userName == null)
		userName = "";
	
	String password = request.getParameter("sPwd");
	if(password == null)
		password = "";

	System.out.println(userName);
	System.out.println(password);
	
	try
	{
		MysqlConnect mysql = MysqlConnect.getDbCon();
		if(mysql != null){
			User user = mysql.userExists(userName, password);	
			
			System.out.println(user);
			
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
		out.println("{\"res\":false}");		
	}
%>