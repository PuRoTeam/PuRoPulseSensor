var selectedUid = -1;

function record() {
	
	var saveb = document.getElementById("save");
	var value = saveb.getAttribute("value");
	var url = "setRecord.jsp";
	
	if(value == "Start"){
		var rec = true;
		saveb.setAttribute("value", "Stop");
		$.post(url, {state: rec});
	}
	
	if(value == "Stop"){
		var rec = false;
		saveb.setAttribute("value", "Start");
		$.post(url, {state: rec});
	}

}

function setMinMax() {
	
	var min = document.forms["select_min_max"]["mingraph"].value;
	var max = document.forms["select_min_max"]["maxgraph"].value;
	
	if(min < max)
		$('#container').highcharts().yAxis[0].setExtremes(min, max);
}


function selectUid() {
	var selectBox = document.getElementById("uidSelection");			
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	if(selectedValue != '')
		selectedUid = selectedValue;
	else
		selectedUid = -1;
	
	//alert('selectedUid ' + selectedUid);
}

function selectUidRealtime() {
	selectUid();

	var hiddenSelectedUid = document.getElementById("selUid");
	hiddenSelectedUid.value = selectedUid;
	//alert(hiddenSelectedUid.value);
	//alert(document.getElementById("selUid").value);
	var formUidSelection = document.getElementById("formUidSelection");
	
	formUidSelection.submit();
}

function getDateAndTime() {
	
	var datefrom = document.forms["date_form"]["date_from"].value;
	var dateto = document.forms["date_form"]["date_to"].value;
	
    var month = datefrom.substring(0,2);
    var day = datefrom.substring(3,5);
	var year = datefrom.substring(6,10);
    var hour = datefrom.substring(11,13);
	var minute = datefrom.substring(14,16);
	
	var date = new Date(year, month - 1, day, hour, minute);
	var from_date = date.getTime();
	     
	month = dateto.substring(0,2);
	day = dateto.substring(3,5);
	year = dateto.substring(6,10);
	hour = dateto.substring(11,13);
	minute = dateto.substring(14,16);
	
	date = new Date(year, month - 1, day, hour, minute, 0, 0);
	var to_date = date.getTime();

	//alert(date.getDate() + ' ' + date.getMonth() + ' ' + date.getFullYear() + ' ' + date.getHours() + ' ' + date.getMinutes() + '----' + to_date);
	
	var url = "getPointsFromDatabase.jsp";   
 
	if(selectedUid != -1)
	{
		$.post(url, {uid: selectedUid, dateFrom: from_date, dateTo: to_date}, function(responseText) {
					
			var newData = new Array();	 
			newData.length = responseText.length;
			
			$('#container').highcharts().destroy();
			
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
			        },			        
			    }]
			});
			
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
