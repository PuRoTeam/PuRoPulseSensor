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

	<script>
	
		function getDateAndTime() {  
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
     
		     var url = "getPointsFromDatabase.jsp";       
		     
		     $.get(url, {uid: 1, dateFrom: from_date, dateTo: to_date}, function(responseText) {
		    	 		
		    	 var mydata = [];
		    	 //$('#container').highcharts().series[0]
		    	 //$('#container').highcharts().series[0].remove();
		    	 //$('#container').highcharts().addSeries(mydata);
		    	 
		    	 for(var i = 0; i < responseText.length; i++) {
					
					var x = responseText[i].timestamp;
	                var y = responseText[i].value;
                    
	                console.log(x);
	                console.log(y);
	                
					mydata.push({x: x, y: y});
					//$('#container').highcharts().series[0].addPoint([x, y], true, true);
				}
		    	 /*$('#container').highcharts().series[0].update(
		    		 {
		    		 data: mydata,
		    		 type: 'areaspline'
		    		 }
		    	 );*/
		    	$('#container').highcharts().series[0].setData(mydata);
		    },  
		    "json"  
			);
     
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
	
	<!-- HighStock -->
	<script type="text/javascript">
		var grafico;	
	
		$(function ()
		{
			// Create the chart
			$('#container').highcharts('StockChart', {
				chart: {
			    	zoomType: 'x',		
				},
	
			    rangeSelector: {
			        selected: 1
			    },
	
			    title: {
			        text: 'AAPL Stock Price'
			    },
			    
			    series: [{
			    	id: "PRIMA",
			        name: 'AAPL Stock Price',
		            data: (function() 
		               		{	            			
			                    // generate an array of random data
			                    var data = [];
			                    var i;
			    
			                    for (i = -25; i <= 0; i++) {
			                    	data.push({
			                            x: 0,
			                            y: 0
			                        });
			                    }
			                    
			                    return data;
		               		})(),
			        //data: data,
			        type: 'spline',
			        tooltip: {
			        	valueDecimals: 2
			        }
			    }]
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
		<div id="sidebar">
			<div id="infoID">
			</div>
			<div id="datetime">
				<form name="date_form">
					<div><h3>From:</h3>
					<input name="date_from" id="datetimepicker"/>
					</div>
					<div><h3>To:</h3>
					<input name="date_to" id="datetimepicker2"/>
					</div>
					<input id="idate" type="button" value="Invia" onclick="getDateAndTime()">					
				</form>
			</div>	
		</div>
		<div id="chart_wrapper">
			<div id="container"></div>
		</div>
	</div>

</body>
</html>
