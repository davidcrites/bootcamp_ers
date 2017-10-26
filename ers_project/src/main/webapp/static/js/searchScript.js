function storeStatusSession() {
	var status = ["All", "Pending", "Approved", "Denied"];
    sessionStorage.status = status;
    document.getElementByClassName("radio").innerHTML = localStorage.status;
	
	} 
function getRoleId(){
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ' + xhttp.readyState);
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        if(!(xhttp.responseText.charAt(0)==='<')){
		        let id = JSON.parse(xhttp.responseText);
		        localRoleId=id.ersUserRoleid;
		        //Add options to navbar if they are a manager
		        if(localRoleId===2){
		        		// allowed here
		        }else if(localRoleId===1){
		        		location.href = "http://localhost:8080/ers_project/static/NotAuthorized.html";
		        		location.reload(true);
		        }else{
		        		location.href = "http://localhost:8080/ers_project/static/NotAuthorized.html";
		        		location.reload(true);
		        }
		        // nothing to do here for new reimbursements
	    		}else{
	    			location.reload(true);
	    		}
	    } else if (xhttp.getResponseHeader("Location")==="/ers_project/static/NotAuthorized.html"){
	    		//Couldn't validate role ID so send to not authorized.
	    		location.href = "http://localhost:8080/ers_project/static/NotAuthorized.html";
	    }
	}
    xhttp.open('GET','getRole');
    xhttp.send(); 
}
