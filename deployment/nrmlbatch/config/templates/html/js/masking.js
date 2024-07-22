function maskPIIFieldValueByColumn(className) {
	var row = document.getElementsByClassName(className);
	for (let i = 0; i < row.length; i++) {
		var convertedText = changePIIFieldValue( row[i].innerText);
		row[i].innerText = convertedText;
	}
}

function maskPIIFieldValue(className) {
	var row = document.getElementsByClassName(className);
	for (let i = 0; i < row.length; i++) {
		var rawValue = row[i].getAttribute("value") 
		var convertedText = changePIIFieldValue(rawValue);
		row[i].setAttribute("value",convertedText);
	}
}

function changePIIFieldValue(temp) {
	var pii = temp;
	var convertedArray = pii.split("");
	
	convertedArray.forEach(function(data,index){
	  if(index%2!=0){
	    convertedArray[index] = '*';
	  }
	})
	return convertedArray.join("");
}

function maskContactFieldValueByColumn(className) {
	var row = document.getElementsByClassName(className);
	for (let i = 0; i < row.length; i++) {
		var convertedText = changeContactFieldValue(row[i].innerText);
		row[i].innerText = convertedText;
	}
}

function maskContqactFieldValue(className) {
	var row = document.getElementsByClassName(className);
	for (let i = 0; i < row.length; i++) {
		var rawValue = row[i].getAttribute("value") 
		var convertedText = changeContactFieldValue(rawValue);
		row[i].setAttribute("value",convertedText);
	}
}

function changeContactFieldValue(temp) {
	var contact = temp;
	if (contact !== null && contact.search("@") >= 0) {
		
		var atArray = contact.split("@");
		atArray[0] = atArray[0].substring(0, 2) + atArray[0].substring(2, atArray[0].length).replace(/./g, 'X');
		
		var dotArray = atArray[1].split(".");
		dotArray[0] = dotArray[0].substring(0, dotArray[0].length - 2).replace(/./g, 'X') 
			+ dotArray[0].substring(dotArray[0].length - 2, dotArray[0].length);
		atArray[1] = dotArray.join(".");
		
		contact = atArray.join("X");
	} else if (contact !== null) {
		if (contact[0] == "+") {
			var newPhnNo = contact.replace(/\d(?=\d{2})/g, "X");
			newPhnNo = newPhnNo.slice(3);
			contact = contact[0] + contact[1] + contact[2] + newPhnNo;
		}
		else {
			var newPhnNo = contact.replace(/\d(?=\d{2})/g, "X");
			newPhnNo = newPhnNo.slice(2);
			contact = contact[0] + contact[1] + newPhnNo;
		}
	} return contact;
}

