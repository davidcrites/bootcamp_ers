
// FUNCTION for getting roleID and calling function to add Manager options
function getRoleId(){
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        let id = JSON.parse(xhttp.responseText);
	        localRoleId=id.ersUserRoleid;
	        //Add options to navbar if they are a manager
	        if(localRoleId===2){
	        		addOptions();
	        }
	    } else {
	    	
	    }
	}
    xhttp.open('GET','getRole');
    xhttp.send(); 
    
}

// UTILITY FUNCTIONS

// adding options if they are a Manager
function addOptions(){
	document.getElementById("dropdown-links").innerHTML +=`
    <li><a href="http://localhost:8080/ers_project/static/reimbursements/AllPending">View All Pending Reimbursements</a></li>
    <li><a href="http://localhost:8080/ers_project/static/reimbursements/AllPast">View All Closed Reimbursements</a></li>`;
}

// populating user fields
function populateMyFields(){
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //console.log(xhttp.responseText);
	        let localUser = JSON.parse(xhttp.responseText);
	        document.getElementById("new-reimb-id").defaultValue = localUser.ersUsersId;
            document.getElementById("new-reimb-name").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
            document.getElementById("replace-name").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
            // ADD attribute readonly="readonly" to new-reimb-id after writing it above
    //NOTE: STILL HAVE TO CATCH THE CALL IN THE CONTROLLER, WRITE CURRENT USER TO RESPONSE
        }
	}
    xhttp.open('GET','getCurrentUser');
    xhttp.send(); 
    
}
function populateOtherFields(passedId){
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //console.log(xhttp.responseText);
	        let localUser = JSON.parse(xhttp.responseText);
	        document.getElementById("new-reimb-id").defaultValue = localUser.ersUsersId;
            document.getElementById("new-reimb-name").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
            document.getElementById("replace-name").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
            // ADD attribute readonly="readonly" to new-reimb-id after writing it above
    //NOTE: STILL HAVE TO CATCH THE CALL IN THE CONTROLLER, WRITE CURRENT USER TO RESPONSE
        }
	}
    xhttp.open('GET','getOtherUser/'+passedId);
    xhttp.send(); 
    
}

//enabling user id for entry
function enableUserIdEntry(){
	document.getElementById("new-reimb-id").removeAttribute("readonly");
	document.getElementById("new-reimb-id").className = "new-input";
	
}

// FUNCTIONS for calling queries

function retrievePending(){

	//DECLARATIONS
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        appendResults(xhttp.responseText);
	    } else {
	    	
	    }
	}
    xhttp.open('GET','pendingReimb');
    xhttp.send(); 
}

function retrieveOther(){
	
	idForParam = document.getElementById("new-reimb-id").value;
	console.log("The retrieveOther function is being called");
	//DECLARATIONS
	let xhttp= new XMLHttpRequest();
	console.log('readyState = ' + xhttp.readyState + ' and new-reimb-id value = ' + document.getElementById("new-reimb-id").value);
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        populateOtherFields(idForParam);
	        appendResults(xhttp.responseText);
	    } else {
	    	
	    }
	}
    //NOTE: STILL HAVE TO CATCH THE CALL IN THE CONTROLLER, WRITE DISPLAY LIST TO RESPONSE
	// this call will use the Other person's id they entered with getParameter("new-id") and get their pending reimbursements
	// if we can't see it on the request, then try passing in the send method...may need to change to post method
    xhttp.open('GET','pendingOther/' + idForParam);
    xhttp.send(); 
}

function appendResults(results){
    let reimbursements = JSON.parse(results);
    console.log(results);
    console.log(reimbursements);
//id name description date amount type
    reimbursements.forEach((Reimbursement)=>{   	    
    		document.getElementById("pending-rows").innerHTML += `
            <tr>
                <td>${Reimbursement.reimbusementId}</td>
                <td>${Reimbursement.author.ersUsername}</td>
                <td>${Reimbursement.reimbDescription}</td>
                <td>${Reimbursement.reimbSubmitted.year}-${Reimbursement.reimbSubmitted.monthValue}-${Reimbursement.reimbSubmitted.dayOfMonth} 
                ${Reimbursement.reimbSubmitted.hour}:${Reimbursement.reimbSubmitted.minute}:${Reimbursement.reimbSubmitted.second}</td>
                <td>${Reimbursement.reimbursementAmount}</td>
    				<td>${Reimbursement.type.reimbType}</td>
                <td>RECEIPT</td>
                
            </tr>
        		`;   	   
    });
    
} 

//OnClick event on my List Item for "Home" in the navbar calls this function
function goHome(){
	if (localRoleId===2){
		console.log("Finance Manager - go to his menu");
		location.href = "http://localhost:8080/ers_project/static/FinanceManagerMenu.html";
	}else{
		console.log("Employee - go to his menu");
		location.href = "http://localhost:8080/ers_project/static/EmployeeMenu.html";
	}
}
// ACTUAL PROGRAM CODE
//Populate the Local Role Id
getRoleId();

//DETERMINE WHICH QUERY TO CALL

if (document.URL.includes("MyNew")){
	populateMyFields();
	retrievePending();
	
}else{ // this is someone entering a reimbursement for someone else;
	document.getElementById("replace-name").value = 'Other Employee';
	enableUserIdEntry();
}
//EVENT LISTENERS
// On valid ID entry and lose focus event or User ID, call retrieveOther()
document.getElementById("new-reimb-id").addEventListener("change",function(){retrieveOther();});

// On press of submit button, override functionality, and call to post, sending parameters
// to write new reimb to the database...when returns here, run retrieveOther() since that
// works for any id

