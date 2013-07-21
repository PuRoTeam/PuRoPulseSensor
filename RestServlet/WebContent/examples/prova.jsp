<!doctype>
<html>
<head>
    
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	<link rel="stylesheet" href="/resources/demos/style.css" />
	
	<script src="js/jquery-ui-timepicker-addon.js"></script>
	
	<script  type="text/javascript">
	$(document).ready(function(){
		$('#slider_example_4').datetimepicker({
		controlType: 'select',
		timeFormat: 'hh:mm tt'
		});
	});
	</script>
	
	<link type="text/css" rel="stylesheet" href="css/mystyle.css">

</head>
<body>
	
	<div id="wrapper">
			
		<div id="title">Arduino Healting Monitor</div>
		
		<%@ include file="menu.jsp" %>
		
		<p>Date: <input type="text" id="slider_example_4" /></p>
		
	</div>

</body>
</html>
