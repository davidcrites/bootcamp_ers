function storeStatusSession() {
	var status = ["All", "Pending", "Approved", "Denied"];
    sessionStorage.status = status;
    document.getElementByClassName("radio").innerHTML = localStorage.status;
	
	} 

