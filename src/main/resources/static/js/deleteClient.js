function deleteClient(row_id) {
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open("DELETE", "/demo/client/deleteClient?id="+row_id, false);
	xmlhttp.send(null);
	alert("Client with the id " + row_id + " has been deleted");
	window.location.href='/demo/client/viewClients';
}