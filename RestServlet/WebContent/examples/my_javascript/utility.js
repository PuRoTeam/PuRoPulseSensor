var selectedUid = -1;

function selectUid() {
	var selectBox = document.getElementById("uidSelection");			
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	if(selectedValue != '')
		selectedUid = selectedValue;
	else
		selectedUid = -1;
	
	//alert('selectedUid ' + selectedUid);
}

function getDateAndTime() {
	
	var datefrom = document.forms["date_form"]["date_from"].value;
	var dateto = document.forms["date_form"]["date_to"].value;
	
    var month = datefrom.substring(0,2);
    var day = datefrom.substring(3,5);
	var year = datefrom.substring(6,10);
    var hour = datefrom.substring(11,13);
	var minute = datefrom.substring(14,16);
 
	var date = new Date(year, month, day, hour, minute);
	var from_date = date.getTime();
	     
	month = dateto.substring(0,2);
	day = dateto.substring(3,5);
	year = dateto.substring(6,10);
	hour = dateto.substring(11,13);
	minute = dateto.substring(14,16);
	     
	date = new Date(year, month, day, hour, minute);
	var to_date = date.getTime();

	//alert(day + ' ' + month + ' ' + year + ' ' + hour + ' ' + minute + '----' + to_date);
	
	var url = "getPointsFromDatabase.jsp";   
 
	if(selectedUid != -1)
	{
		$.get(url, {uid: selectedUid, dateFrom: from_date, dateTo: to_date}, function(responseText) {
					
			var newData = new Array();	 
			newData.length = responseText.length;
		 
			$('#container').highcharts().series[0].setData(newData);
		 
			for(var i = 0; i < responseText.length; i++) {
				var x = responseText[i].timestamp;
				var y = responseText[i].value;
	        
				$('#container').highcharts().series[0].addPoint([x, y], false, false, false); //redraw = false, shift = false, animation = false (su questo sono indeciso, di default true)
			}
			$('#container').highcharts().redraw(); //ridisegniamo il grafico solo dopo aver aggiunto tutti i punti
		
		}, "json");
	}
} 

function trim(s) {
    return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
}