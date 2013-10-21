<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%

	//TODO TEST
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

<body style="background-color: #323B55">
	<img id="slick-banner" src="css/header.png">
	<form id="slick-login" name="frmLogin" onSubmit="return validate();" action="doLogin.jsp" method="post">
		<label>Username</label><input type="text" name="sUserName"  placeholder="Username"/>
		<label>Password</label><input type="password" name="sPwd" placeholder="Password"/>
		<input type="submit" name="sSubmit" value="Submit" />
		<div id="error"><%=error%></div>
	</form>
</body>
</html>
