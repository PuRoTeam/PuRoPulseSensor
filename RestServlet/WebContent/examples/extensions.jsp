<!doctype>
<html>
<head>

	<link type="text/css" rel="stylesheet" href="css/mystyle.css">
	
	<!-- Graph -->
	<link type="text/css" rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css">
	<link type="text/css" rel="stylesheet" href="../src/css/graph.css">
	<link type="text/css" rel="stylesheet" href="../src/css/detail.css">
	<link type="text/css" rel="stylesheet" href="../src/css/legend.css">
	<link type="text/css" rel="stylesheet" href="css/extensions.css">
	
	<script src="../vendor/d3.v2.js"></script>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.15/jquery-ui.min.js"></script>
	
	<script src="../src/js/Rickshaw.js"></script>
	<script src="../src/js/Rickshaw.Class.js"></script>
	<script src="../src/js/Rickshaw.Compat.ClassList.js"></script>
	<script src="../src/js/Rickshaw.Graph.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.Area.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.Line.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.Bar.js"></script>
	<script src="../src/js/Rickshaw.Graph.Renderer.ScatterPlot.js"></script>
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
	<script src="../src/js/Rickshaw.Fixtures.Number.js"></script>
	<script src="../src/js/Rickshaw.Fixtures.RandomData.js"></script>
	<script src="../src/js/Rickshaw.Fixtures.Color.js"></script>
	<script src="../src/js/Rickshaw.Color.Palette.js"></script>
	<script src="../src/js/Rickshaw.Graph.Axis.Y.js"></script>

	<script src="js/extensions.js"></script>
	
	<!-- Datetime picker -->
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	<script src="js/jquery-ui-timepicker-addon.js"></script>
	
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
	
	<script  type="text/javascript">
	$(document).ready(function(){
		$('#datetimepicker').datetimepicker({
			addSliderAccess: true,
			sliderAccessArgs: { touchonly: false }
		});
		$('#datetimepicker2').datetimepicker({
			addSliderAccess: true,
			sliderAccessArgs: { touchonly: false }
		});
	});
	</script>
	
	
	<script>
	
	// set up our data series with 150 random data points
	$(document).ready(function(){
	var seriesData = [ [], [], [], [], [], [], [], [], [] ];
	var random = new Rickshaw.Fixtures.RandomData(150);
	
	for (var i = 0; i < 150; i++) {
		random.addData(seriesData);
	}
	
	var palette = new Rickshaw.Color.Palette( { scheme: 'classic9' } );
	
	// instantiate our graph!
	
	var graph = new Rickshaw.Graph( {
		element: document.getElementById("chart"),
		width: 900,
		height: 500,
		renderer: 'line',
		stroke: true,
		preserve: true,
		series: [
			{
				color: palette.color(),
				data: seriesData[0],
				name: 'Moscow'
			}, {
				color: palette.color(),
				data: seriesData[1],
				name: 'Shanghai'
			}, {
				color: palette.color(),
				data: seriesData[2],
				name: 'Amsterdam'
			}, {
				color: palette.color(),
				data: seriesData[3],
				name: 'Paris'
			}, {
				color: palette.color(),
				data: seriesData[4],
				name: 'Tokyo'
			}, {
				color: palette.color(),
				data: seriesData[5],
				name: 'London'
			}, {
				color: palette.color(),
				data: seriesData[6],
				name: 'New York'
			}
		]
	} );
	
	graph.render();
	
	var slider = new Rickshaw.Graph.RangeSlider( {
		graph: graph,
		element: $('#slider')
	} );
	
	var hoverDetail = new Rickshaw.Graph.HoverDetail( {
		graph: graph
	} );
	
	var annotator = new Rickshaw.Graph.Annotate( {
		graph: graph,
		element: document.getElementById('timeline')
	} );
	
	var legend = new Rickshaw.Graph.Legend( {
		graph: graph,
		element: document.getElementById('legend')
	
	} );
	
	var shelving = new Rickshaw.Graph.Behavior.Series.Toggle( {
		graph: graph,
		legend: legend
	} );
	
	var order = new Rickshaw.Graph.Behavior.Series.Order( {
		graph: graph,
		legend: legend
	} );
	
	var highlighter = new Rickshaw.Graph.Behavior.Series.Highlight( {
		graph: graph,
		legend: legend
	} );
	/*
	var smoother = new Rickshaw.Graph.Smoother( {
		graph: graph,
		element: $('#smoother')
	} );
	*/
	var ticksTreatment = 'glow';
	
	var xAxis = new Rickshaw.Graph.Axis.Time( {
		graph: graph,
		ticksTreatment: ticksTreatment
	} );
	
	xAxis.render();
	
	var yAxis = new Rickshaw.Graph.Axis.Y( {
		graph: graph,
		tickFormat: Rickshaw.Fixtures.Number.formatKMBT,
		ticksTreatment: ticksTreatment
	} );
	
	yAxis.render();
	
	//Problema nel selezionare il form giusto
	var controls = new RenderControls( {
		element: document.forms['side_panel'],
		//element: document.querySelector('form'),
		//element: document.getElementsById('side_panel'),
		graph: graph
	} );
	});
	</script>
	
</head>
<body>
	<div id="wrapper">
			
		<div id="title">Arduino Healthing Monitor</div>
		
		<%@ include file="menu.jsp" %>
	
		
		<div id="datetime">
			<form name="date_form" onsubmit="getDateAndTime()">
				<div>From:</div> 
				<input name="date_from" id="datetimepicker"/>
				<div>To:</div>
				<input name="date_to" id="datetimepicker2"/>
				<input type="submit" value="Invia">
			</form>
		</div>
		<div id="content">
			<div id="sidebar">
				<form id="side_panel">
					<h1>Random Data</h1>
					<section><div id="legend"></div></section>
					<section>
						<div id="renderer_form" class="toggler">
							<input type="radio" name="renderer" id="area" value="area">
							<label for="area">area</label>
							<input type="radio" name="renderer" id="line" value="line" checked>
							<label for="line">line</label>
						</div>
					</section>
					
					<section>
						<div id="offset_form">
							<label for="value">
								<input type="radio" name="offset" id="value" value="value" checked>
								<span>value</span>
							</label>
						</div>
					</section>
					 
					<section>
						<div id="interpolation_form">
							<label for="cardinal">
								<input type="radio" name="interpolation" id="cardinal" value="cardinal" checked>
								<span>cardinal</span>
							</label>
							<label for="linear">
								<input type="radio" name="interpolation" id="linear" value="linear">
								<span>linear</span>
							</label>
							<label for="step">
								<input type="radio" name="interpolation" id="step" value="step-after">
								<span>step</span>
							</label>
						</div>
					</section>
				</form>
			</div>
			<div id="chart_container">
				<div id="chart"></div>
				<div id="timeline"></div>
				<div id="slider"></div>
			</div>
		</div>
		

	</div>
	
	
	

</body>
</html>