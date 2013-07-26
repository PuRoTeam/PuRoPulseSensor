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
	<script src="../src/js/Rickshaw.Graph.Axis.X.js"></script>
	<script src="../src/js/Rickshaw.Graph.Axis.Y.js"></script>
	<script src="../src/js/Rickshaw.Graph.Axis.Time.js"></script>
	<script src="../src/js/Rickshaw.Graph.HoverDetail.js"></script>
	
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
			<div id="y_axis"></div>
		</div>
		<p>Date: <input id="datepicker" type="text" /></p>
		
		<!-- 
		<div id="sidebar">
			<form>
				<input type="checkbox" name="vehicle" value="Bike">I have a bike<br>
				<input type="checkbox" name="vehicle" value="Car">I have a car
			</form>
		</div> 
		-->
	</div>
		
	<script>
		var tv = 150; //deve essere sincronizzato col client/arduino
		var palette = new Rickshaw.Color.Palette( { scheme: 'classic9' } );
				
		var graph = new Rickshaw.Graph( {
			element: document.getElementById("chart"),
			width: 900,
			height: 500,
			renderer: 'line',
			series: new Rickshaw.Series.FixedDuration([{ name: 'one', x: 0, y: 0 }], palette, { //mostra solo una finestra temporale e non TUTTI i dati inseriti da sempre 
				timeInterval: tv,
				maxDataPoints: 100, // grandezza finestra temporale in termini di punti visualizzati 
				timeBase: new Date().getTime() / 1000
			}) 
		} );
		
		var hoverDetail = new Rickshaw.Graph.HoverDetail( {
			graph: graph
		} );		
		
		/*var graph = new Rickshaw.Graph({
			element: document.getElementById("chart"),
			width: 900,
			height: 500,
			renderer: 'line',			
			series: new Rickshaw.Series([{ name: 'This' }], [{data: [{ x: 0, y: 0 }, { x: 0.1, y: 100 }, { x: 5, y: 50 }]}])
		    // series: [{ data: [{ x: 0, y: 0 }, { x: 0.1, y: 100 }, { x: 5, y: 50 }] }],
		});*/
				
		/*var x_axes = new Rickshaw.Graph.Axis.X( {
			graph: graph
		} );*/
		
		graph.render();		
		
		var latestTimestamp = -1; //controlla se sono stati effettivamente aggiunti nuovi punti (se il client stà continuando a scrivere)
		
		//se il client smette di inviare dati, il grafico continua ad indicare l'ultimo valore (comportamento giusto)
		var iv = setInterval( function() {
			var loadUrl = "randomNumber.jsp";
			$.get(loadUrl,
				{uid: 1},  
			    function(responseText) { //TODO aggiungere controlli se risultato nullo? Non credo, restituisce in caso un array vuoto 
			   		
			    	var newPoints = false;	
			    		
					if(responseText.length > 0)
					{
						var newLatestTimestamp = responseText[responseText.length - 1].timestamp;
						if(latestTimestamp != newLatestTimestamp)
						{
							newPoints = true;
							latestTimestamp = newLatestTimestamp;
						}
					}
					
					if(newPoints) //se non ho aggiunto nuovi punti
					{
						for(var i = 0; i < responseText.length; i++)
						{
							var data = { x: responseText[i].timestamp, y: responseText[i].value}; //le x non funzionano, se le metto ad un valore fisso mi compare un altro grafico!
							//console.log(responseText[i].timestamp + " " + responseText[i].value);
							graph.series.addData(data);
							graph.render();
						}
					}
					else if(!newPoints && responseText.length > 0)//se il client arduino si è fermato, anzichè mostrare gli ultimi N valori a ripetizione, mostro solo l'ultimo a ripetizione
					{
						var data = { x: responseText[responseText.length - 1].timestamp, y: responseText[responseText.length - 1].value};
						graph.series.addData(data);
						graph.render();
					}
			    },  
			    "json"  
			);  
		}, tv );
					
	</script>
	
</body>
</html>