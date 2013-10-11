<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
	<TITLE> Timepicker Addon</TITLE>
	<META NAME="Generator" CONTENT="EditPlus">
	<META NAME="Author" CONTENT="">
	<META NAME="Keywords" CONTENT="">
	<META NAME="Description" CONTENT="">
	
	<link type="text/css" rel="stylesheet" href="css/mystyle.css">
	<link type="text/css" rel="stylesheet" href="jquery/jquery-ui-1.10.3.css" />
	
	<script type="text/javascript" src="jquery/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="jquery/jquery-ui-1.10.3.js"></script>
	<script type="text/javascript" src="jquery-ui-timepicker-addon.js" ></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
		$('#event_date').datetimepicker({ showSecond: true,dateFormat: 'yy-mm-dd',timeFormat: 'hh:mm:ss'});
		});
	</script>
</HEAD>

<BODY>
	<center><h3>Time picker add-on</h3></center>
	<form id="form" method="post" action="" >
	<table align="center">
	<tr>
	<td style="width: 124px">Event Date</td>
	<td>
	<input name="event_date" type="text" size="40" id="event_date"/>
	</td>
	</tr>
	</table>
	</form>
</BODY>
</HTML>