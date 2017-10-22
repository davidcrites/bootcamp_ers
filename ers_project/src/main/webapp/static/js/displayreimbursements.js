//Global Variables needed
let localRoleId = 0;
let localUser = null;

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

// FUNCTION for adding options if they are a Manager
function addOptions(){
	document.getElementById("dropdown-links").innerHTML +=`
    <li><a href="http://localhost:8080/ers_project/static/reimbursements/AllPending">View All Pending Reimbursements</a></li>
    <li><a href="http://localhost:8080/ers_project/static/reimbursements/AllPast">View All Closed Reimbursements</a></li>`;
}

// FUNCTIONS for calling queries

function retrieveSearchResults(){

	//DECLARATIONS
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        appendResults(xhttp.responseText);
	    		}
	   }
    xhttp.open('GET','doSearch');
    xhttp.send(); 
}


function retrievePending(optString){

	//DECLARATIONS
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        if(optString==="Delete"){
	        		appendResultsWithDelete(xhttp.responseText);
	        }else{
	        		appendResults(xhttp.responseText);
	    		}
	    } else {
	    	
	    }
	}
    xhttp.open('GET','pendingReimb');
    xhttp.send(); 
}

function retrievePast(){

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
    xhttp.open('GET','pastReimb');
    xhttp.send(); 
}

function retrieveAllPending(optString){

	//DECLARATIONS
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        if (optString==="Delete"){
	        		appendResultsWithDelete(xhttp.responseText);
	        }else{
	        		appendResults(xhttp.responseText);
	        }
	        
	    } else {
	    	
	    }
	}
    xhttp.open('GET','pendingAll');
    xhttp.send(); 
}

//populating user fields
function populateMyFields(){
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = '+ xhttp.readyState);
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //console.log(xhttp.responseText);
	        localUser = JSON.parse(xhttp.responseText);
	        //document.getElementById("new-reimb-id").defaultValue = localUser.ersUsersId;
            //document.getElementById("new-reimb-name").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
            document.getElementById("replace-name").innerText = (': '+ localUser.userFirstName + ' ' + localUser.userLastName);
            //document.getElementById("pend-title").innerText = (localUser.userFirstName + ' ' + localUser.userLastName);
            // ADD attribute readonly="readonly" to new-reimb-id after writing it above
        }
	}
    xhttp.open('GET','getCurrentUser');
    xhttp.send(); 
    
}


function retrieveAllPast(){

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
    xhttp.open('GET','pastAll');
    xhttp.send(); 
}

function appendResults(results){
    let reimbursements = JSON.parse(results);
    console.log(results);
    console.log(reimbursements);

    reimbursements.forEach((Reimbursement)=>{
    	    if(!(Reimbursement.reimbResolved===null)){
	    		document.getElementById("table-body").innerHTML += `
	            <tr>
	                <td value="${Reimbursement.reimbusementId}">${Reimbursement.reimbusementId}</td>
	                <td>${Reimbursement.reimbursementAmount}</td>
	                <td>${Reimbursement.reimbSubmitted.year}-${Reimbursement.reimbSubmitted.monthValue}-${Reimbursement.reimbSubmitted.dayOfMonth} 
	                ${Reimbursement.reimbSubmitted.hour}:${Reimbursement.reimbSubmitted.minute}:${Reimbursement.reimbSubmitted.second}</td>
	    				<td>${Reimbursement.reimbResolved.year}-${Reimbursement.reimbResolved.monthValue}-${Reimbursement.reimbResolved.dayOfMonth} 
	                ${Reimbursement.reimbResolved.hour}:${Reimbursement.reimbResolved.minute}:${Reimbursement.reimbResolved.second}</td>
	                <td>${Reimbursement.reimbDescription}</td>
	                <td>RECEIPT</td>
	    			    <td>${Reimbursement.author.ersUsername}</td>
	                <td>${Reimbursement.resolver.ersUsername}</td>
	                <td>${Reimbursement.status.reimbStatus}</td>
	                <td>${Reimbursement.type.reimbType}</td>
	                <td><button class="disabled btn btn-danger" onclick="deleteRow(this)">Delete</button></td>
	            </tr>
	        		`;
    	    }else if (document.URL.includes("managersearch")){
	    		document.getElementById("table-body").innerHTML += `
	            <tr>
	                <td value="${Reimbursement.reimbusementId}">${Reimbursement.reimbusementId}</td>
	                <td>${Reimbursement.reimbursementAmount}</td>
	                <td>${Reimbursement.reimbSubmitted.year}-${Reimbursement.reimbSubmitted.monthValue}-${Reimbursement.reimbSubmitted.dayOfMonth} 
	                ${Reimbursement.reimbSubmitted.hour}:${Reimbursement.reimbSubmitted.minute}:${Reimbursement.reimbSubmitted.second}</td>
	    				<td>${Reimbursement.reimbResolved}</td>
	    				<td>${Reimbursement.reimbDescription}</td>
	                <td>RECEIPT</td>
	    			    <td>${Reimbursement.author.ersUsername}</td>
	                <td>${Reimbursement.resolver.ersUsername}</td>
	                <td>${Reimbursement.status.reimbStatus}</td>
	                <td>${Reimbursement.type.reimbType}</td>
	                <td><button class="btn btn-danger" onclick="deleteRow(this)">Delete</button></td>
	            </tr>
	        		`;
    	    }else{
	    		document.getElementById("table-body").innerHTML += `
		            <tr>
		                <td value="${Reimbursement.reimbusementId}">${Reimbursement.reimbusementId}</td>
		                <td>${Reimbursement.reimbursementAmount}</td>
		                <td>${Reimbursement.reimbSubmitted.year}-${Reimbursement.reimbSubmitted.monthValue}-${Reimbursement.reimbSubmitted.dayOfMonth} 
		                ${Reimbursement.reimbSubmitted.hour}:${Reimbursement.reimbSubmitted.minute}:${Reimbursement.reimbSubmitted.second}</td>
		    				<td>${Reimbursement.reimbResolved}</td>
		    				<td>${Reimbursement.reimbDescription}</td>
		                <td>RECEIPT</td>
		    			    <td>${Reimbursement.author.ersUsername}</td>
		                <td>${Reimbursement.resolver.ersUsername}</td>
		                <td>${Reimbursement.status.reimbStatus}</td>
		                <td>${Reimbursement.type.reimbType}</td>
		                <td><button class="disabled btn btn-danger" onclick="deleteRow(this)">Delete</button></td>
		            </tr>
		        		`;
    	    }
    });
    
} 
function appendResultsWithDelete(results){
    let reimbursements = JSON.parse(results);
    console.log(results);
    console.log(reimbursements);

    reimbursements.forEach((Reimbursement)=>{
	    		document.getElementById("table-body").innerHTML += `
	            <tr>
	                <td value="${Reimbursement.reimbusementId}">${Reimbursement.reimbusementId}</td>
	                <td>${Reimbursement.reimbursementAmount}</td>
	                <td>${Reimbursement.reimbSubmitted.year}-${Reimbursement.reimbSubmitted.monthValue}-${Reimbursement.reimbSubmitted.dayOfMonth} 
	                ${Reimbursement.reimbSubmitted.hour}:${Reimbursement.reimbSubmitted.minute}:${Reimbursement.reimbSubmitted.second}</td>
	    				<td>${Reimbursement.reimbResolved}</td>
	                <td>${Reimbursement.reimbDescription}</td>
	                <td>RECEIPT</td>
	    			    <td>${Reimbursement.author.ersUsername}</td>
	                <td>${Reimbursement.resolver.ersUsername}</td>
	                <td>${Reimbursement.status.reimbStatus}</td>
	                <td>${Reimbursement.type.reimbType}</td>
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
//Populate the Local Role Id
getRoleId();


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

//DETERMINE WHICH QUERY TO CALL

if (document.URL.includes("managersearch")){
	document.getElementById("replace-name").innerText = (': Search Results');
	retrieveSearchResults();
}

if (document.URL.includes("MyPending")){
	populateMyFields();
	retrievePending("Delete");
}
if (document.URL.includes("MyDelete")){
	populateMyFields();
	retrievePending("Delete");
}
if (document.URL.includes("MyPast")){
	retrievePast();
	populateMyFields();
}
if (document.URL.includes("AllPending")){
	retrieveAllPending("Delete");
	document.getElementById("replace-name").innerText = ': All Pending';
}
if (document.URL.includes("OtherDelete")){
	retrieveAllPending("Delete");
	document.getElementById("replace-name").innerText = ': All Pending';
}
if (document.URL.includes("AllPast")){
	retrieveAllPast();
	document.getElementById("replace-name").innerText = ': All Past';
}