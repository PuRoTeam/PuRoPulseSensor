<%@ page import="it.uniroma2.pulsesensor.database.MysqlConnect" %>
<%@ page import="it.uniroma2.pulsesensor.data.User" %>
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
		out.println("{\"res\":false}");		
	}
%>