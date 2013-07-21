<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>PuRo Sensor</title>
	
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
	<link rel="stylesheet" href="/resources/demos/style.css" />
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	
	<!-- Datepicker --> 
	<script type="text/javascript" language="javascript" src="datepicker/prototype-1.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/prototype-base-extensions.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/prototype-date-extensions.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/behaviour.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/datepicker.js"></script>
	<script type="text/javascript" language="javascript" src="datepickers/behaviors.js"></script>
	<link rel="stylesheet" href="datepicker/datepicker.css">
		
	<!-- Project -->
	<%@ page import="data.Point" %>
	<link type="text/css" rel="stylesheet" href="css/mystyle.css">
	
	<script>  
      function getDateAndTime(){  
        
        var datefrom = document.forms["date_form"]["date_from"].value;
        var dateto = document.forms["date_form"]["date_to"].value;
         
        var day = datefrom.substring(0,2);
        var month = datefrom.substring(3,5);
        var year = datefrom.substring(6,10);
        var hour = datefrom.substring(11,13);
        var minute = datefrom.substring(14,16);

        var date = new Date(year, month, day, hour, minute);
        var from_date = date.getTime();
        
        day = dateto.substring(0,2);
        month = dateto.substring(3,5);
        year = dateto.substring(6,10);
        hour = dateto.substring(11,13);
        minute = dateto.substring(14,16);
        
        date = new Date(year, month, day, hour, minute);
        var to_date = date.getTime();
        
        //var url = ".jsp";
        
        /*
        $.get(url, {from: from_date, to: to_date}, function(responseText) {
			    	console.log(responseText);
					var data = { one: responseText.random};
		    		graph.series.addData(data);
		    		graph.render();
		    		$("#testo").html(responseText);
			    },  
			    "json"  
			);  
        */
        alert("day"+day+"\n"+
        		"month"+month+"\n"+
        		"year"+year+"\n"+
        		"hour"+hour+"\n"+
        		"minute"+minute+"\n");
        alert(from_date);
        alert(to_date);
        
      }  
    </script>
    	
</head>
<body>
	<% int mode = 0; %>
	<div id="wrapper">
		
		<div id="title">Arduino Healting Monitor</div>
		
		<%@ include file="menu.jsp" %>
		
		<div id="content">
			<div id="sidebar">
				<form name="date_form" onsubmit="getDateAndTime()">
					<div>From:</div> 
					<input name="date_from" class="datetimepicker"/>
					<div>To:</div>
					<input name="date_to" class="datetimepicker"/>
					<br/><br/>
					<input type="submit" value="Invia">
				</form>
			</div>
			
		</div>
	</div><!-- end #wrapper -->

</body>
</html>