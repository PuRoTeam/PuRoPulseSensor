<!DOCTYPE HTML>
<%@page import="java.util.ArrayList"%>
<%@ page import="database.MysqlConnect" %>

<html>

<%@ include file="checkIfLogged.jsp" %>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<script type="text/javascript" src="jquery/jquery-ui-timepicker-addon.js" ></script>

	<script type="text/javascript" src="highstock/js/highstock.js" ></script>
	<script type="text/javascript" src="highstock/js/modules/exporting.js" ></script>
	<script type="text/javascript" src="highstock/js/themes/dark-blue.js" ></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$('#datetimepicker').datetimepicker( {
				addSliderAccess: true,
				sliderAccessArgs: { touchonly: false }
			});
			$('#datetimepicker2').datetimepicker({
				addSliderAccess: true,
				sliderAccessArgs: { touchonly: false }
			});
		});
	</script>
		
	<!-- HighStock -->
	<script type="text/javascript">
	
		$(function ()
		{
			// Create the chart
			$('#container').highcharts('StockChart', {
				chart: {
			    	zoomType: 'x',		
				},
	
			    rangeSelector: {
			        selected: 1,
			        inputEnabled: false,
			        buttons: [{
			        	type: 'second',
			        	count: 30,
			        	text: '30s'
			        }, {
			        	type: 'minute',
			        	count: 1,
			        	text: '1m'
			        }, {
			        	type: 'minute',
			        	count: 10,
			        	text: '10m'
			        }, {
			        	type: 'minute',
			        	count: 60,
			        	text: '1h'
			        }, {
			        	type: 'all',
			        	text: 'All'
			        }]
			    },
	
			    title: {
			        text: 'Pulse Sensor Replay'
			    },
			    
			    series: [{
			    	name: 'replay',
		            data: (function() 
		               	   {
		            			var data = new Array();
	                    		data.push({x: 0, y: 0}); //inserisco almeno un punto perchè altrimenti non disegna nulla
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
	<div id="wrapper">	
		<div id="sidebar">
			<div id="infoID">
				<label>Welcome</label>
				<%
					out.print(session.getAttribute("userName"));
				%>
			</div>
			<div id="uid">
				<label>UID</label>
				<select id="uidSelection" onchange="selectUid()">
					<option></option>
					<%
					MysqlConnect mysql = MysqlConnect.getDbCon();
					ArrayList<Long> arr = mysql.getPatientUid();
					
					for(int i=0; i<arr.size(); i++) {
						out.print("<option>" + arr.get(i) + "</option>");
					}
					%>
				</select>
			</div>
			<div id="datetime">
				<form name="date_form" id="date_form">
					<label>From:</label>
					<input name="date_from" id="datetimepicker"/>
					<label>To:</label>
					<input name="date_to" id="datetimepicker2"/>
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
