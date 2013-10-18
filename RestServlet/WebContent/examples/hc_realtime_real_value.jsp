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
	    
	        $('#container').highcharts({
	            chart: {
	                type: 'line',
	                animation: false,//Highcharts.svg, // don't animate in old IE
	                marginRight: 30,
	                events: {	                	
	                    load: function() {
	                    	var latestTimestamp = -1; //controlla se sono stati effettivamente aggiunti nuovi punti (se il client stà continuando a scrivere)	                    	
	                        // set up the updating of the chart each second
	                        var series = this.series[0];	                        
	            			var loadUrl = "getPointsFromShared.jsp";
	                        
	                        setInterval(function() {
	                        	
		            			$.get(loadUrl,
			            				{uid: 1},  
			            			    function(responseText) {

			            			    	var newPoints = false;	
			            			    		
			            					if(responseText.length > 0) {
			            						console.log(responseText[responseText.length - 1].timestamp);
			            						
			            						var newLatestTimestamp = responseText[responseText.length - 1].timestamp;
			            						if(latestTimestamp != newLatestTimestamp)
			            						{
			            							newPoints = true;
			            							latestTimestamp = newLatestTimestamp;
			            						}
			            					}
			            					
			            					if(newPoints) { //se ho aggiunto nuovi punti 
			            						
			            						/*//reimposto la visualizzazione dei punti (e non solo di una linea continua)
			            						//casomai l'avessi disattivata quando il client si era stoppato
				            					$('#container').highcharts().series[0].update({
				            						marker: {
		            					            	enabled: false //true
		            					        	}
				            					});*/
			            						
			            						for(var i = 0; i < responseText.length; i++)
			            						{
			        	                            var x = responseText[i].timestamp,
			        	                            y = responseText[i].value;
			        	                            series.addPoint([x, y], false, true); //redraw = false, shift = true
			        	                            //se metto shift = false, tutti i punti creati vengono mostrati (finestra di punti infinita)
			            						}			            						
			            					}
			            					else if(!newPoints && responseText.length > 0) { //se il client arduino si è fermato, anzichè mostrare gli ultimi N valori a ripetizione, mostro solo l'ultimo a ripetizione
		        	                            //var x = responseText[responseText.length - 1].timestamp,
		        	                            var x = new Date().getTime(); //in modo da continuare ad aggiungere punti e mostrare una linea continua
		        	                            y = responseText[responseText.length - 1].value;
		        	                            series.addPoint([x, y], false, true);
		        	                            
		        	                            /*//meglio mostrare una linea continua che una serie di punti tutti attaccati
				            					$('#container').highcharts().series[0].update({
				            						marker: {
		            					            	enabled: false
		            					        	}
				            					});*/
			            					}
			            					
			            					$('#container').highcharts().redraw(); //disegno tutto alla fine
			            			    },  
			            			    "json"  
			            			);  
		                    }, 16);	//Circa 60 FPS
                    	}
	                }
	            },
	            plotOptions: {
	                line: {
	                    marker: {
	                        enabled: false
	                    }
	                }
	            },	            
	            title: {
	                text: 'Live random data'
	            },
	            xAxis: {
	                type: 'datetime',
	                tickInterval: 1000,//500
	                gridLineWidth: 1,
	                labels: {
	                    rotation: 30, //30
	                    align: 'left'
	                },
	                dateTimeLabelFormats: {	                	
	                	millisecond: '%H:%M:%S.%L'
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
	                max: 200,
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
	                    var data = new Array(), time = (new Date()).getTime(), i;
	    				
	                    for (i = -19; i <= 0; i++) {
	                        data.push({
	                            x: time + i * 500,
	                            y: 0
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
		<div id="sidebar">
			<div id="infoID">
				<h3>Sensor#</h3> 
			</div>
		</div>
		<div id="chart_wrapper">
			<div id="container"></div>
		</div>				
				
	</div>

</body>
</html>
