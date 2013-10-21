<html>
<head>
	<link type="text/css" rel="stylesheet" href="css/mystyle.css">
	<title>Pulse Sensor - Login</title>
	<script type="text/javascript" src="my_javascript/utility.js"></script>
</head>

<body style="background-color: #323B55">
	<img id="slick-banner" src="css/header.png">
	<div id="slick-login">
		<div id="error">
			<%
				if(session.getAttribute("userName") == null) 
				{
					out.write("You must be logged to see this content");
					
					response.setHeader("Refresh", "2; URL=login.jsp");
					
					//response.sendRedirect("login.jsp");
					return;
				}
			%>
		</div>
	</div>
</body>
</html>