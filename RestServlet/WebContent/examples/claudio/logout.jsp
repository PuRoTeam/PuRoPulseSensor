<%
	session.removeAttribute("userName");
	response.sendRedirect("login.jsp");
	return;
%>