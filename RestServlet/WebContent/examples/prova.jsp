<!doctype>
<html>
<head>
    <title>PuRo Sensor - Prova</title>
    <link type="text/css" rel="stylesheet" href="css/mystyle.css">
    
    <!-- Datetime picker -->
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	
	<script src="datepicker/jquery-ui-timepicker-addon.js"></script>
	<link rel="stylesheet" href="/resources/demos/style.css" />
	
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
		$(function() {
		$( "#datepicker" ).datepicker();
		});
	</script>
	
	

</head>
<body>
	
	<div id="wrapper">
			
		<div id="title">Arduino Healting Monitor</div>
		
		<%@ include file="menu.jsp" %>
		
		<div id="datetime">
			<form name="date_form" onsubmit="getDateAndTime()">
				<div>From:</div> 
				<input name="date_from" id="datetimepicker"/>
				<div>To:</div>
				<input name="date_to" id="datetimepicker2"/>
				<input type="submit" value="Invia" />
			</form>
		</div>
		
		<div id="content">
			<div id="sidebar">
				<form name="date_form" onsubmit="getDateAndTime()">
					<div>From:</div> 
					<input name="date_from" class="datetimepicker"/>
					<div>To:</div>
					<input name="date_to" class="datetimepicker2"/>
					<br/><br/>
					<input type="submit" value="Invia">
				</form>
			</div>
			
		</div>
	</div>

</body>
</html>
