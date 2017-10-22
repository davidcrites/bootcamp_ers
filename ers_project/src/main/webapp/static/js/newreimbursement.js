//INITIAL DECLARATIONS
//	let newId=0;
//	let newDescription='';
//	let newAmount=0;
//	let newType='';
let localUser=null;


// FUNCTION for getting roleID and calling function to add Manager options
function getRoleId(){
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = '+ xhttp.readyState);
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
	    console.log('readyState = '+ xhttp.readyState);
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //console.log(xhttp.responseText);
	        localUser = JSON.parse(xhttp.responseText);
	        document.getElementById("new-reimb-id").defaultValue = localUser.ersUsersId;
            document.getElementById("new-reimb-name").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
            document.getElementById("replace-name").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
            document.getElementById("pend-title").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
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
	    console.log('readyState = '+ xhttp.readyState);
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //console.log(xhttp.responseText);
	        localUser = JSON.parse(xhttp.responseText);
	        if(localUser != null){
		        document.getElementById("new-reimb-id").defaultValue = localUser.ersUsersId;
	            document.getElementById("new-reimb-name").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
	            document.getElementById("replace-name").innerText = ('Other: ' + localUser.userFirstName + ' ' + localUser.userLastName);
	            document.getElementById("pend-title").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
	        }
	        else{
	        		console.log("No User was found with that ID.")
	        		$("#unfound-modal").modal();
	        		document.getElementById("new-reimb-id").value=``;
	        }
  
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

//function to submit and create new ticket;
//onClick of submit button in html button, call MY submit function, and call to post, sending parameters
//to write new reimb to the database...when returns here, run retrieveOther() since that
//works for any id...doing this near the top of the .js page...not here
function submit(){
	console.log("we got to the submit() function");
	let xhttp = new XMLHttpRequest();
	
	let newId=document.getElementById("new-reimb-id").value;
	let newDescription=document.getElementById("new-reimb-description").value;
	let newAmount=document.getElementById("new-reimb-amount").value;
	let newType=document.getElementById("new-reimb-type").value;
	let params = ("new-id=" + newId + "&new-description=" + newDescription + "&new-amount=" + newAmount + "&new-type=" + newType);
	
	if (newId===undefined || newDescription===undefined || newAmount===undefined ||
		newId===''        || newDescription===''        || newAmount===''){
		//call enter input modal and that's it
		$("#invalid-modal").modal()
	}else {
	
		xhttp.onreadystatechange = function() {//Call a function when the state changes.
			if(xhttp.readyState == 4 && xhttp.status == 200) {
				//alert(xhttp.responseText);
				//call success modal
				$("#success-modal").modal()
				setTimeout(function() {$('#success-modal').modal('hide');}, 2000);
				document.getElementById("submit-form").reset();
				retrieveOther(newId);
			}
		}
		
		console.log("My params are: " + params);
		
		xhttp.open('POST', '/ers_project/static/reimbursement/new', true);
	
		//Send the proper header information along with the request - this should tell the server what format the parameters will be in
		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	
		xhttp.send(params);
	}
}

// FUNCTIONS for calling queries

function retrievePending(){

	//DECLARATIONS
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ' + xhttp.readyState);
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        appendResults(xhttp.responseText);
	    } else {
	    	
	    }
	}
    xhttp.open('GET','pendingReimb');
    xhttp.send(); 
}

function retrieveOther(optId){
	
	if(optId==undefined){
		idForParam = document.getElementById("new-reimb-id").value;
	}else{
		idForParam = optId;
	}
	console.log("The retrieveOther function is being called");
	//DECLARATIONS
	let xhttp= new XMLHttpRequest();
	console.log('readyState = ' + xhttp.readyState + ' and new-reimb-id value = ' + document.getElementById("new-reimb-id").value);
	xhttp.onreadystatechange = function(){
	    console.log('readyState = '+ xhttp.readyState);
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
// empty the innerHTML before adding, in case this isn't the first time
    document.getElementById("pending-rows").innerHTML=``;
    reimbursements.forEach((Reimbursement)=>{   	    
    		document.getElementById("pending-rows").innerHTML += `
            <tr>
                <td value="${Reimbursement.reimbusementId}">${Reimbursement.reimbusementId}</td>
                <td>${Reimbursement.author.ersUsername}</td>
                <td>${Reimbursement.reimbDescription}</td>
                <td>${Reimbursement.reimbSubmitted.year}-${Reimbursement.reimbSubmitted.monthValue}-${Reimbursement.reimbSubmitted.dayOfMonth} 
                ${Reimbursement.reimbSubmitted.hour}:${Reimbursement.reimbSubmitted.minute}:${Reimbursement.reimbSubmitted.second}</td>
                <td>${Reimbursement.reimbursementAmount}</td>
    				<td>${Reimbursement.type.reimbType}</td>
                <td>RECEIPT</td>
                <td><button class="btn btn-danger" onclick="deleteRow(this)">Delete</button></td>
            </tr>
        		`;   	   
    });
    
} 

//function to prompt for confirmation of delete and add a listener....make sure to call module to confirm
function deleteRow(button)
{
    // var i=row.parentNode.parentNode.rowIndex;
	// document.getElementById("test-row").cells[0].attributes.value.value
	let reimbId = button.parentNode.parentNode.cells[0].attributes.value.value;
	console.log(reimbId);
	
	//prompt the user to see if they are sure they want to delete; will return true if they confirm.
	$("#del-confirm-modal").modal();
	
	//document.getElementById("confirm-del").addEventListener("click",function(){retrieveOther();});
	document.getElementById("confirm-del").addEventListener("click",function(){confirmDelete(reimbId);});
}
//function to delete a pending reimbursement.
function confirmDelete(id){
	
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //call delete success modal
			$("#del-success-modal").modal()
			setTimeout(function() {$('#del-success-modal').modal('hide');}, 2000);
			
		    	//Get author of deleted record from response...use his ers_users_id to call retreiveOther(id)
	    		let userId = localUser.ersUsersId;
	    		retrieveOther(userId);
        }else if (xhttp.status===500){
			//call delete failed modal
        	$("#del-failure-modal").modal()
		}
	}
    xhttp.open('GET','deleteRecord/'+id);
    xhttp.send(); 
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

// onClick of submit button in html button, call MY submit function, and call to post, sending parameters
// to write new reimb to the database...when returns here, run retrieveOther() since that
// works for any id...doing this near the top of the .js page...not here


//document.getElementById("reimb-submit").addEventListener("click",function(event){
//	event.preventDefault();
//	console.log("If performed after click values should be available");
//	newId=document.getElementById("new-reimb-id").Value;
//	newDescription=document.getElementById("new-reimb-description").innerText;
//	newAmount=document.getElementById("new-reimb-amount").Value;
//	newType=document.getElementById("new-reimb-type").Value;
//	console.log(newId + ' ' + newDescription + ' ' + newAmount + ' ' + newType);
//	postTicket();
//	
//})
