//function to clear the current User if one exists, effectively logging out the current user
function clearCurrentUser(){
	
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ' + xhttp.readyState);
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //call delete success modal
			console.log("No 'currentUser' stored now");
        }else{
        	   //nothing to do
		}
	}
    xhttp.open('GET','clearUser');
    xhttp.send(); 
}


// ACTUAL PROGRAM CODE
//Clear the Current User if there is one
clearCurrentUser();
console.log("In the signin.js file");
