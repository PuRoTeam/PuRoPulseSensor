<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>PuRo Sensor</title>
	
	<link type="text/css" rel="stylesheet" href="src/css/graph.css">
	<link type="text/css" rel="stylesheet" href="src/css/detail.css">
	<link type="text/css" rel="stylesheet" href="src/css/legend.css">
	<link type="text/css" rel="stylesheet" href="css/extensions.css">
	<link type="text/css" rel="stylesheet" href="css/mystyle.css">
	
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
	<link rel="stylesheet" href="/resources/demos/style.css" />
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	
	<script type="text/javascript" language="javascript" src="datepicker/prototype-1.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/prototype-base-extensions.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/prototype-date-extensions.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/behaviour.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/datepicker.js"></script>
	<script type="text/javascript" language="javascript" src="datepicker/behaviors.js"></script>
	<link rel="stylesheet" href="datepicker/datepicker.css">
		
	<%@ page import="data.Point" %>
	<%@ page import="java.util.GregorianCalendar"%>
	
	<script src="vendor/d3.v2.js"></script>

	<script src="src/js/Rickshaw.js"></script>
	<script src="src/js/Rickshaw.Class.js"></script>
	<script src="src/js/Rickshaw.Compat.ClassList.js"></script>
	<script src="src/js/Rickshaw.Graph.js"></script>
	<script src="src/js/Rickshaw.Graph.Renderer.js"></script>
	<script src="src/js/Rickshaw.Graph.Renderer.Stack.js"></script>
	<script src="src/js/Rickshaw.Graph.Renderer.Line.js"></script>
	<script src="src/js/Rickshaw.Graph.Renderer.Area.js"></script>
	<script src="src/js/Rickshaw.Graph.RangeSlider.js"></script>
	<script src="src/js/Rickshaw.Graph.HoverDetail.js"></script>
	<script src="src/js/Rickshaw.Graph.Annotate.js"></script>
	<script src="src/js/Rickshaw.Graph.Legend.js"></script>
	<script src="src/js/Rickshaw.Graph.Axis.Time.js"></script>
	<script src="src/js/Rickshaw.Graph.Behavior.Series.Toggle.js"></script>
	<script src="src/js/Rickshaw.Graph.Behavior.Series.Order.js"></script>
	<script src="src/js/Rickshaw.Graph.Behavior.Series.Highlight.js"></script>
	<script src="src/js/Rickshaw.Graph.Smoother.js"></script>
	<script src="src/js/Rickshaw.Graph.Unstacker.js"></script>
	<script src="src/js/Rickshaw.Fixtures.Time.js"></script>
	<script src="src/js/Rickshaw.Fixtures.RandomData.js"></script>
	<script src="src/js/Rickshaw.Fixtures.Color.js"></script>
	<script src="src/js/Rickshaw.Color.Palette.js"></script>
	<script src="src/js/Rickshaw.Series.js"></script>
	<script src="src/js/Rickshaw.Series.FixedDuration.js"></script>
	
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
        
        var url = ".jsp";
        
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
		
		<div id="menu">
				<h3><a href="select.jsp">Real time</a></h3>
				<h3><a href="select.jsp">Replay</a></h3>
				<h3><a href="login.jsp">Log out</a></h3>
		</div>
		<div id="content">
			<%	 
				if(mode == 1){ 
					out.write("<div id=\"sidebar\">");
					out.write("<form>");
					out.write("<input type=\"checkbox\" name=\"vehicle\" value=\"Bike\">I have a bike<br>");
					out.write("<input type=\"checkbox\" name=\"vehicle\" value=\"Car\">I have a car");
					out.write("</form>");	
					out.write("</div>");
				} 
			%>
			
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
			<div id="chart_wrapper">
				
				<div id="chart"></div>
				
			</div>
		</div>
		
	</div><!-- end #wrapper -->

	<script>
		var tv = 150; //deve essere sincronizzato col client/arduino
		
		// instantiate our graph!
		var graph = new Rickshaw.Graph( {
			element: document.getElementById("chart"),
			width: 900,
			height: 500,
			renderer: 'line',
			series: new Rickshaw.Series.FixedDuration([{ name: 'one' }], undefined, {
				timeInterval: tv,
				maxDataPoints: 100,
				timeBase: new Date().getTime() / 1000
			}) 
		} );
		
		graph.render();

		var iv = setInterval( function() {
			var loadUrl = "randomNumber.jsp";
			$.get(loadUrl,  
			    null,  
			    function(responseText) {
			    	console.log(responseText);
					var data = { one: responseText.random};
		    		graph.series.addData(data);
		    		graph.render();
		    		$("#testo").html(responseText);
			    },  
			    "json"  
			);  
			
		}, tv );
					
	</script>
	
	<div id="testo"></div>

</body>
</html>