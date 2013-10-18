
<%
	if(session.getAttribute("userName") == null) 
	{
		out.write("You must be logged to see this content");
		
		response.setHeader("Refresh", "2; URL=login.jsp");
		
		//response.sendRedirect("login.jsp");
		return;
	}
%>