<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
	String error=request.getParameter("error");
	if(error==null || error=="null"){
		error="";
	}
%>
<html>
<head>
	<title>User Login JSP</title>
	<script>
		function trim(s) 
		{
		    return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
		}

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

<body>
	<div><%=error%></div>
	<form name="frmLogin" onSubmit="return validate();" action="doLogin.jsp" method="post">
		User Name <input type="text" name="sUserName" /><br />
		Password <input type="password" name="sPwd" /><br />
		<input type="submit" name="sSubmit" value="Submit" />
	</form>
</body>
</html>
