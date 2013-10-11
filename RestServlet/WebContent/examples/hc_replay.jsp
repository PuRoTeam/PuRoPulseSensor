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
	
	<!-- Inclusione classe java Data -->
	<%@ page import="data.Point" %>
	
	<!-- HighCharts 
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
	                /*
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
	        		*/
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
	                            y: 0.5
	                        });
	                    }
	                    return data;
               		})()
	            }]
	            
	        });
	    });
	    
	});
	</script> -->
	<!-- HighStock -->
	<script type="text/javascript">
		$(function() {
		$.getJSON('http://www.highcharts.com/samples/data/jsonp.php?filename=aapl-c.json&callback=?', function(data) {
	
			// Create the chart
			$('#container').highcharts('StockChart', {
			    chart: {
			    	zoomType: 'x'
			    },
	
			    rangeSelector: {
			        selected: 1
			    },
	
			    title: {
			        text: 'AAPL Stock Price'
			    },
			    
			    series: [{
			        name: 'AAPL Stock Price',
			        data: data,
			        type: 'spline',
			        tooltip: {
			        	valueDecimals: 2
			        }
			    }]
			});
		});
	});
	</script>
	

</head>
<body>
	<script type="text/javascript" src="highstock/js/highstock.js" ></script>
	<script type="text/javascript" src="highstock/js/modules/exporting.js" ></script>
	<script type="text/javascript" src="highstock/js/themes/dark-blue.js" ></script>


	<div id="title">Arduino Healthing Monitor</div>
	<div id="wrapper">
	
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
		<div id="chart_wrapper">
			<div id="container"></div>
			
		</div>
	</div>

</body>
</html>
