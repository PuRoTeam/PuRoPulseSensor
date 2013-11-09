<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%

	/*if(session.getAttribute("userName") != null) 
	{
		out.write("User logged as " + session.getAttribute("userName"));
		return;
	}*/

	String error=request.getParameter("error");
	if(error==null || error=="null"){
		error="";
	}
%>
<html>
<head>
	<link type="text/css" rel="stylesheet" href="css/mystyle.css">
	<title>Pulse Sensor - Login</title>
	
	<script type="text/javascript" src="my_javascript/utility.js"></script>
	<script>
		function validate()
		{
		    if(trim(document.frmLogin.sUserName.value)=="")
		    {
		      alert("Login empty");
		      document.frmLogin.sUserName.focus();
		      return false;
		    }
		    else if(trim(document.frmLogin.sPwd.value)=="")
		    {
		      alert("password empty");
		      document.frmLogin.sPwd.focus();
		      return false;
		    }
		}
	</script>
</head>

<%
String username = (String)session.getAttribute("userName");
if(username == null)
{
%>
	<body style="background-color: #323B55">
		<img id="slick-banner" src="css/header.png">
		<form id="slick-login" name="frmLogin" onSubmit="return validate();" action="doLogin.jsp" method="post">
			<label>Username</label><input type="text" name="sUserName"  placeholder="Username"/>
			<label>Password</label><input type="password" name="sPwd" placeholder="Password"/>
			<input class="pulse" type="submit" name="sSubmit" value="Submit" />
			<div id="error"><%=error%></div>
		</form>
	</body>	
<%	
}
else
{
%>
<body style="background-color: #323B55">
	<img id="slick-banner" src="css/header.png">
	<form id="slick-login" name="frmLogin" action="" method="post">
		<div id="error">Welcome <%=username %></div>		
		<input class="pulse" type="submit" name="sSubmit" value="Home" onclick="document.getElementById('slick-login').action = 'home.jsp';"/>
		<input type="submit" name="sSubmit" value="Logout" onclick="document.getElementById('slick-login').action = 'logout.jsp';"/>
	</form>
</body>
<%	
}
%>

</html>
