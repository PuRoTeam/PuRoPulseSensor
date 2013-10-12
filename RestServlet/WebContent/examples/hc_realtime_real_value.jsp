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
	                    	var latestTimestamp = -1; //controlla se sono stati effettivamente aggiunti nuovi punti (se il client stà continuando a scrivere)	                    	
	                        // set up the updating of the chart each second
	                        var series = this.series[0];	                        
	            			var loadUrl = "randomNumber.jsp";
	                        
	                        setInterval(function() {
	                        	
		            			$.get(loadUrl,
			            				{uid: 1},  
			            			    function(responseText) { //TODO aggiungere controlli se risultato nullo? Non credo, restituisce in caso un array vuoto
			            			   		
			            			    	var newPoints = false;	
			            			    		
			            					if(responseText.length > 0)
			            					{
			            						console.log(responseText[responseText.length - 1].timestamp);
			            						
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
			        	                            var x = responseText[i].timestamp,
			        	                            y = responseText[i].value;
			        	                            series.addPoint([x, y], true, true);
			            						}
			            					}
			            					else if(!newPoints && responseText.length > 0)//se il client arduino si è fermato, anzichè mostrare gli ultimi N valori a ripetizione, mostro solo l'ultimo a ripetizione
			            					{
		        	                            var x = responseText[responseText.length - 1].timestamp,
		        	                            y = responseText[responseText.length - 1].value;
		        	                            series.addPoint([x, y], true, true);
			            					}
			            			    },  
			            			    "json"  
			            			);  
		                    }, 1000);	//Modificato 
                    	}
	                	
	                    /*load: function() {
	                        // set up the updating of the chart each second
	                        var series = this.series[0];
	                        setInterval(function() {
	                            var x = (new Date()).getTime(), // current time    
	                            y = Math.random();
	                            series.addPoint([x, y], true, true);
		                    }, 1000);	//Modificato 
	                    }*/
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
	                max: 600,
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
	                            y: 0//Math.random()
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
