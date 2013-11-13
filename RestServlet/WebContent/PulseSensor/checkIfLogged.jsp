<html>
<head>
	<link type="text/css" rel="stylesheet" href="css/mystyle.css">
	<script type="text/javascript" src="my_javascript/utility.js"></script>
</head>

<body style="background-color: #323B55">
	<% 
	if(session.getAttribute("userName") == null) 
	{
	%>
	<img id="slick-banner" src="css/header.png">
	<div id="slick-login">
		<div id="error">
			<%
			out.write("You must be logged to see this content");
			response.setHeader("Refresh", "4; URL=login.jsp");
			
			%>
		</div>
	</div>
	<% 
			return;
	}
	%>
</body>
</html>