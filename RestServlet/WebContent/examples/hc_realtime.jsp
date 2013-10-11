<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	
	<title>Highcharts Example</title>
	
	<link type="text/css" rel="stylesheet" href="css/mystyle.css">
	<!-- <link type="text/css" rel="stylesheet" href="css/jquery-ui-timepicker-addon.css" /> -->
	<link rel="stylesheet" href="jquery/jquery-ui-1.10.3.css" /><!-- Tema Troncastic -->
	
	<!-- Inclusione libreria jquery -->
	<script src="jquery/jquery-1.10.2.js"></script>
	<script src="jquery/jquery-ui-1.10.3.js"></script>
	
	<!-- Datetimepicker -->
	<script type="text/javascript" src="jquery/jquery-ui-timepicker-addon.js" ></script>
	<script type="text/javascript">
		$(document).ready(function(){
		$('#datetimepicker').datetimepicker({ showSecond: true,dateFormat: 'yy-mm-dd',timeFormat: 'hh:mm:ss'});
		});
	</script>
	<!--
	<script type="text/javascript">
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
	-->
	
	<!-- Inclusione classe java Data -->
	<%@ page import="data.Point" %>
	
	
	<!-- HighCharts -->
	<script type="text/javascript">
	var timeinterval = 5000;
	$(function () {
	    $(document).ready(function() {
	        Highcharts.setOptions({
	            global: {
	                useUTC: false
	            }
	        });
	    
	        var chart;
	        $('#container').highcharts({
	            chart: {
	                type: 'spline',
	                animation: false,//Highcharts.svg, // don't animate in old IE
	                marginRight: 30,
	                events: {
	                    load: function() {
	                        // set up the updating of the chart each second
	                        var series = this.series[0];
	                        setInterval(function() {
	                            var x = (new Date()).getTime(), // current time    
	                            y = Math.random();
	                            series.addPoint([x, y], true, true);
		                    }, 100000);	//Modificato
	                    }
	                }
	            },
	            title: {
	                text: 'Live random data'
	            },
	            xAxis: {
	                type: 'datetime',
	                tickInterval: 5000,
	                gridLineWidth: 1,
	                labels: {
	                    rotation: 30
	                },
	                dateTimeLabelFormats: {
	                	millisecond: '%H:%M:%S.%L',
	                }
	            },
	            yAxis: {
	                title: {
	                    text: 'Value'
	                },
	                plotLines: [{
	                    value: 0,
	                    width: 1,
	                    color: '#808080'
	                }],
	                max: 1,
	                min: 0
	            },
	            
	            tooltip: {
	            	formatter: function() {
	                        return '<b>'+ this.series.name +'</b><br/>'+
	                        Highcharts.dateFormat('%H:%M:%S.%L', this.x) +'<br/>'+
	                        Highcharts.numberFormat(this.y, 2);
	                }
	            },
	            legend: {
	                enabled: false
	            },
	            exporting: {
	                enabled: false
	            },
	            
	            series: [{
	                name: 'Random data',
		            data: (function() 
               		{
	                    // generate an array of random data
	                    var data = [],
	                    	time = (new Date()).getTime(), 
	                    	i;
	    
	                    for (i = -19; i <= 0; i++) {
	                        data.push({
	                            x: time + i * 1000, //Modificato
	                            y: Math.random()
	                        });
	                    }
	                    return data;
               		})()
	            }]
	            
	        });
	    });
	    
	});
	</script>
	

</head>
<body>
	<script type="text/javascript" src="highcharts/js/highcharts.js" ></script>
	<script type="text/javascript" src="highcharts/js/modules/exporting.js" ></script>
	<script type="text/javascript" src="highcharts/js/themes/dark-blue.js" ></script>

	<div id="title">Arduino Healthing Monitor</div>
	<div id="wrapper">
	
		<%@ include file="menu.jsp" %>
		<div id="chart_wrapper">
			<div id="container"></div>
		</div>				
				
	</div>

</body>
</html>
