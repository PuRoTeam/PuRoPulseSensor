<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="checkIfLogged.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>PuRo Sensor</title>
			
		<link type="text/css" rel="stylesheet" href="css/mystyle.css">
		<!-- <link type="text/css" rel="stylesheet" href="css/jquery-ui-timepicker-addon.css" /> -->
		<link rel="stylesheet" href="jquery/jquery-ui-1.10.3.css" /><!-- Tema Troncastic -->
		
		<!-- Inclusione libreria jquery -->
		<script src="jquery/jquery-1.10.2.js"></script>
		<script src="jquery/jquery-ui-1.10.3.js"></script>
			
		<!-- Nostre funzioni-->
		<script type="text/javascript" language="javascript" src="my_javascript/utility.js"></script> 
	</head>
<body>
	
	<% 
		//Validazione parametri query string
		String mode = request.getParameter("mode");
		if(mode == null)
			mode = "";			
	%>
	<!-- <div id="title">Arduino Health Monitor</div>-->
	<img style="margin: 0px 10px 0px 300px" src="css/header.png">
	
	<div id="wrapper">	
		<jsp:include page="menu.jsp"/>
		<img src="css/funny_hearts.jpg" height="400" width="1100" >
	</div>
	
	<%
		if(mode.equals("realtime"))	{	
			%><jsp:include page="realtime.jsp"/><%
		}
		if(mode.equals("replay")) {
			%><jsp:include page="replay.jsp"/><%
		}
	%>

</body>
</html>