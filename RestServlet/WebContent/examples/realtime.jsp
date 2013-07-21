<!doctype>
<html>
<head>
	<link type="text/css" rel="stylesheet" href="../src/css/graph.css">
	<link type="text/css" rel="stylesheet" href="../src/css/detail.css">
	<link type="text/css" rel="stylesheet" href="../src/css/legend.css">
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
	
	<script src="../vendor/d3.v2.js"></script>

	<script src="../src/js/Rickshaw.js"></script>
	<script src="../src/js/Rickshaw.Class.js"></script>
	<script src="../src/js/Rickshaw.Compat.ClassList.js"></script>
	<script src="../src/js/Rickshaw.Graph.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.Stack.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.Line.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.Area.js"></script>
	<script src="../src/js/Rickshaw.Graph.RangeSlider.js"></script>
	<script src="../src/js/Rickshaw.Graph.HoverDetail.js"></script>
	<script src="../src/js/Rickshaw.Graph.Annotate.js"></script>
	<script src="../src/js/Rickshaw.Graph.Legend.js"></script>
	<script src="../src/js/Rickshaw.Graph.Axis.Time.js"></script>
	<script src="../src/js/Rickshaw.Graph.Behavior.Series.Toggle.js"></script>
	<script src="../src/js/Rickshaw.Graph.Behavior.Series.Order.js"></script>
	<script src="../src/js/Rickshaw.Graph.Behavior.Series.Highlight.js"></script>
	<script src="../src/js/Rickshaw.Graph.Smoother.js"></script>
	<script src="../src/js/Rickshaw.Graph.Unstacker.js"></script>
	<script src="../src/js/Rickshaw.Fixtures.Time.js"></script>
	<script src="../src/js/Rickshaw.Fixtures.RandomData.js"></script>
	<script src="../src/js/Rickshaw.Fixtures.Color.js"></script>
	<script src="../src/js/Rickshaw.Color.Palette.js"></script>
	<script src="../src/js/Rickshaw.Series.js"></script>
	<script src="../src/js/Rickshaw.Series.FixedDuration.js"></script>
	
	<script>  
      function raj(){  
        <% String str="Raja"; %>  
        var s="<%=str%>";  
        alert(s);  
      }  
    </script>
</head>
<body>
	<div id="title">Arduino Healting Monitor</div>
	<div id="wrapper">
		
		<%@ include file="menu.jsp" %>
		
		<div id="chart_wrapper">
			<div id="chart"></div>
		</div>
		<p>Date: <input id="datepicker" type="text" /></p>
		
		<div id="sidebar">
			<form>
				<input type="checkbox" name="vehicle" value="Bike">I have a bike<br>
				<input type="checkbox" name="vehicle" value="Car">I have a car
			</form>
		</div>
	</div>
		
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
					
		/*for(i = 0; i < 200; i++)				
		{
			var data = {one: i}
			graph.series.addData(data);
		}*/ 
		
		graph.render();
		
		//graphDataToPlot = new Array();
		//var indexToInsert = 0; //posizione in cui inserire nuovi elemento 
		//var indexToView = 0; //posizione del primo elemento da visualizzare 
		
		//se il client smette di inviare dati, il grafico continua ad indicare l'ultimo valore (comportamento giusto)
		/*var iv = setInterval( function() {
			var loadUrl = "randomNumber.jsp";
			$.get(loadUrl,
				{uid: 1},  
			    function(responseText) {
			    	console.log(responseText.length);
					var data = { one: responseText[0].value};
		    		graph.series.addData(data);
		    		graph.render();
		    		$("#testo").html(responseText);
			    },  
			    "json"  
			);  
			
		}, tv );*/
		
		//se il client smette di inviare dati, il grafico continua ad indicare l'ultimo valore (comportamento giusto)
		var iv = setInterval( function() {
			var loadUrl = "randomNumber.jsp";
			$.get(loadUrl,
				{uid: 1},  
			    function(responseText) {
			   					
					for(var i = 0; i < responseText.length; i++)
					{
						var data = { one: responseText[i].value};
						graph.series.addData(data);
						graph.render();
					}
					
					
					
					/*var i;
					//da mettere un controllo/booleano per evitare di aggiungere dati già aggiunti (quando client smette di trasmettere)
					for(i = 0; i < responseText.length; i++)
					{
						graphDataToPlot[indexToInsert] = responseText[i].value;
						indexToInsert++;
					}*/
					//console.log(indexToInsert);
			    	
			    },  
			    "json"  
			);  
			
		}, tv );
		
		//sarebbe da svuotare gli elementi già visti 
		/*setInterval( function() {
			var data = { one: graphDataToPlot[indexToView]};
			indexToView++;
			graph.series.addData(data);
		}, tv );*/
		
					
	</script>
	
	<div id="testo"></div>
	
</body>
</html>