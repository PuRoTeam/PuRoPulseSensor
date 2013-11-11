<!DOCTYPE HTML>
<%@page import="servlet.Shared"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="database.MysqlConnect" %>
<html>

<%@ include file="checkIfLogged.jsp" %>

<%
String selUid = request.getParameter("selUid");
Long selectedUid = new Long(-1);

if(selUid == null)
	selUid = "";
else
{
	try
	{
		selectedUid = Long.parseLong(selUid);
	}
	catch(NumberFormatException e)
	{}
}
	
%>
	
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	
	<script type="text/javascript" src="highcharts/js/highcharts.js" ></script>
	<script type="text/javascript" src="highcharts/js/modules/exporting.js" ></script>
	<script type="text/javascript" src="highcharts/js/themes/dark-blue.js" ></script>
	
	<!-- HighCharts -->
	<script type="text/javascript">
	
		var timeinterval = 20;
		
		//crea grafico
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
		                        var latestValue = -1;
		                    	// set up the updating of the chart each second
		                        var series = this.series[0];	                        
		            					                        
		                        setInterval(function() {
		                        	
		                        	//il problema è che tipo buffera le richieste, e quando cambio la selezione, per un pò alterna richieste con vecchio uid a richieste con nuovo uid (con casini per il grafico)
		                        	//if(selectedUid == -1) //se non ho selezionato uid, non faccio richieste
		                        	//	return;
		                        	
		                        	<%
		                        	if(!selectedUid.equals(new Long(-1)))
		                        	{	
		                        	%>		                        	
			    						var urlBeat = "getBeat.jsp";
			    						$.post(urlBeat, 
			    							{uid: <%=selectedUid%>}, 
			    							function(responseText) {
				    							//var heartBeatDiv = document.getElementById("heartBeat");
				    							$('#heartBeat span').text(responseText.BPM);
			    						}, "json");
		                        	
		    							var loadUrl = "getPointsFromShared.jsp";
			            				$.post(loadUrl,
				            				{uid: <%=selectedUid%>},  
				            			    function(responseText) {
	
				            			    	var newPoints = false;	
				            			    		
				            					if(responseText.length > 0) {
				            						
				            						var newLatestTimestamp = responseText[responseText.length - 1].timestamp;
				            						var newLatestValue = responseText[responseText.length - 1].value;
				            						if(latestTimestamp != newLatestTimestamp && latestValue != newLatestValue)
				            						{
				            							newPoints = true;
				            							latestTimestamp = newLatestTimestamp;
				            							latestValue = newLatestValue;
				            						}
				            					}
				            					
				            					if(newPoints) { //se ho aggiunto nuovi punti 				            						            						
				            						for(var i = 0; i < responseText.length; i++)
				            						{
				        	                            var x = responseText[i].timestamp;
				        	                            var y = responseText[i].value;
				        	                            series.addPoint([x, y], false, true); //redraw = false, shift = true
				        	                            //se metto shift = false, tutti i punti creati vengono mostrati (finestra di punti infinita)
				            						}			            						
				            					}/*//in caso è da mettere in getPointsFromDatabase l'azzeramente di tutti i punti
				            					else if(!newPoints && responseText.length > 0) { //se il client arduino si è fermato, anzichè mostrare gli ultimi N valori a ripetizione, mostro solo l'ultimo a ripetizione
			        	                            //var x = responseText[responseText.length - 1].timestamp;
			        	                            var x = new Date().getTime(); //in modo da continuare ad aggiungere punti e mostrare una linea continua
			        	                            var y = responseText[responseText.length - 1].value;
			        	                            series.addPoint([x, y], false, true);
				            					}*/
				            					
				            					$('#container').highcharts().redraw(); //disegno tutto alla fine
				            			    },  
				            			    "json"  
				            			);
			            			<%
		                        	}
			            			%>
			                    }, timeinterval); //16 == Circa 60 FPS
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
		                text: 'Pulse Sensor Realtime'
		            },
		            xAxis: {
		                type: 'datetime',
		                tickInterval: 2000,//500
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
		                max: 530, //700
		                min: 480 //400
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
		    				
		                    for (i = -99; i <= 0; i++) {
		                        data.push({
		                            x: time,// + i * 5,
		                            y: 0
		                        });
		                    }
		                    return data;
	               		})()
		            }]
		            
		        }); //end container highcharts
		    });
		    
		});
	</script>	

</head>
<body>
	<div id="wrapper">
		<div id="sidebar">
			<div id="infoID">
				<label>Welcome</label>
				<%=session.getAttribute("userName") %>
			</div>
			<div id="uid">
				<label>UID</label>
				<form id="formUidSelection" action="home.jsp?mode=realtime" method="POST">
					<select id="uidSelection" onchange="selectUidRealtime()">
						<option><%=" "%></option>
						<%
						MysqlConnect mysql = MysqlConnect.getDbCon();
						ArrayList<Long> arr = mysql.getPatientUid();
						
						for(int i=0; i<arr.size(); i++) {
							if(arr.get(i).equals(selectedUid))
								out.print("<option value=\"" + arr.get(i) + "\" selected>" + arr.get(i) + "</option>");
							else
								out.print("<option value=\"" + arr.get(i) + "\">" + arr.get(i) + "</option>");
						}
						%>
					</select>
					<input type="hidden" value="" name="selUid" id="selUid"/>
				</form>
			</div>
			<div id="heart">
				<img src="css/heart.png" class="pulse" height="100" width="120">
				<div id="heartBeat"><span>0</span></div> <!-- <%=session.getAttribute("userName") %> -->
			</div>
			<div id="uid">
			<label>Saving data:
				<%
				if(Shared.getInstance().isDBWorking()) //se attualmente funziona, domando se voglio interromperlo
				{
					%>
					<input id="save" type="submit" value="Stop" onclick="record()"/>
					<%
				}
				else
				{
					%>
					<input id="save" type="submit" value="Start" onclick="record()"/>
					<%					
				}
				%>
			</label>
			</div>			
		</div>
		<div id="chart_wrapper">
			<div id="container"></div>
		</div>
		<div id="min_max">
			<form name="select_min_max" id="date_form">
				<label>Min:</label>
				<input name="mingraph" type="number">
				<label>Max:</label>
				<input name="maxgraph" type="number">
				<input id="idate" type="button" value="Imposta" onclick="setMinMax()">
			</form>
		</div>		
	</div>
</body>
</html>
