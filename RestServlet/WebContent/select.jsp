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
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	<link rel="stylesheet" href="/resources/demos/style.css" />
	<script>
		$(function() {
		$( "#datepicker" ).datepicker();
		});
	</script>
	
	<%@ page import="data.Point" %>
	
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
	
</head>
<body>
	<% int mode = 1; %>
	<div id="wrapper">
		
		<div id="title">Arduino Healting Monitor</div>
		
		<div id="menu">
				<h3><a href="">Real time</a></h3>
				<h3><a href="">Replay</a></h3>
				<h3><a href="">Log out</a></h3>
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