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

		     /*alert("day"+day+"\n"+
		     		"month"+month+"\n"+
		     		"year"+year+"\n"+
		     		"hour"+hour+"\n"+
		     		"minute"+minute+"\n");*/
     
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
		    	 
		    	 //$('#container').highcharts().series[0].remove();
		    	 
		    	 for(var i = 0; i < responseText.length; i++) {
					
					var x = responseText[i].timestamp,
	                y = responseText[i].value;
                    
					$('#container').highcharts().series[0].addPoint([x, y], true, true);
				}
		    },  
		    "json"  
			);
		     
		     /*alert("day"+day+"\n"+
		     		"month"+month+"\n"+
		     		"year"+year+"\n"+
		     		"hour"+hour+"\n"+
		     		"minute"+minute+"\n");*/
		     //alert(from_date);
		     //alert(to_date);
     
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
			    	
	                /*events: {	                	
	                    load: function() {
	                        var series = this.series[0];	                        
	            			var loadUrl = "getPointsFromDatabase.jsp";
              	                        	
	            			$.get(loadUrl,
		            				{uid: 1},  
		            			    function(responseText) {
	            						for(var i = 0; i < responseText.length; i++) {
	        	                            var x = responseText[i].timestamp,
	        	                            y = responseText[i].value;
	        	                   
	        	                            series.addPoint([x, y], true, true);
	            						}
		            			    },  
		            			    "json"  
		            			);  
                    	}	                	
	                }*/
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
		<div id="datetime">
			<form name="date_form">
				<div>From:</div> 
				<input name="date_from" id="datetimepicker"/>
				<div>To:</div>
				<input name="date_to" id="datetimepicker2"/>
				<input type="button" value="Invia" onclick="getDateAndTime()">
			</form>
		</div>
		<div id="chart_wrapper">
			<div id="container"></div>
			
		</div>
	</div>

</body>
</html>
