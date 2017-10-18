//Global Variables needed
let localRoleId = 0;

// FUNCTION for getting roleID
function getRoleId(){
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.responseText);
	        let id = JSON.parse(xhttp.responseText);
	        localRoleId=id.ersUserRoleid;
	    } else {
	    	
	    }
	}
    xhttp.open('GET','getRole');
    xhttp.send(); 
    
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

function retrieveAllPending(){

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
    xhttp.open('GET','pendingAll');
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
	                <td>${Reimbursement.reimbusementId}</td>
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
	            </tr>
	        		`;
    	    }else{
	    		document.getElementById("table-body").innerHTML += `
	            <tr>
	                <td>${Reimbursement.reimbusementId}</td>
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
	            </tr>
	        		`;
    	    }
    });
    
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

if (document.URL.includes("MyPending")){
	retrievePending();
}
if (document.URL.includes("MyPast")){
	retrievePast();
}
if (document.URL.includes("AllPending")){
	retrieveAllPending();
}
if (document.URL.includes("AllPast")){
	retrieveAllPast();
}