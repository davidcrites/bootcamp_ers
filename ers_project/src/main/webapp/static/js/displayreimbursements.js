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
	        if(optString==="Delete" || optString==="Status"){
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
function populateMyFields(MyFieldsChoice){
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = '+ xhttp.readyState);
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //console.log(xhttp.responseText);
	        localUser = JSON.parse(xhttp.responseText);
	        switch (MyFieldsChoice){
	        case 1:
	        	document.getElementById("replace-name").innerText = (': '+ localUser.userFirstName + ' ' + localUser.userLastName);
	        	break;
	        case 2:
	        	document.getElementById("replace-name").innerText = (': Search Results');
	        	break;
	        case 3:
	        	document.getElementById("replace-name").innerText = (': All Pending');
	        	break;
	        case 4:
	        	document.getElementById("replace-name").innerText = (': All Past');
	        	break;
	        case 5:
	        	document.getElementById("replace-name").innerText = (': Pending Requests');
	        	break;
	        }
            

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
    document.getElementById("table-body").innerHTML ="";

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
	                <td><button class="btn btn-primary btn-sm" onclick="showPic(this)">RECEIPT</button></td>
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
	                <td><button class="btn btn-primary btn-sm" onclick="showPic(this)">RECEIPT</button></td>
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
		                <td><button class="btn btn-primary btn-sm" onclick="showPic(this)">RECEIPT</button></td>
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
    document.getElementById("table-body").innerHTML ="";

    if (document.URL.includes("StatusAll")){
    	document.getElementById("opt_column").innerHTML = "Approve or Deny";
	    reimbursements.forEach((Reimbursement)=>{
		    		document.getElementById("table-body").innerHTML += `
		            <tr>
		                <td value="${Reimbursement.reimbusementId}">${Reimbursement.reimbusementId}</td>
		                <td>${Reimbursement.reimbursementAmount}</td>
		                <td>${Reimbursement.reimbSubmitted.year}-${Reimbursement.reimbSubmitted.monthValue}-${Reimbursement.reimbSubmitted.dayOfMonth} 
		                ${Reimbursement.reimbSubmitted.hour}:${Reimbursement.reimbSubmitted.minute}:${Reimbursement.reimbSubmitted.second}</td>
		    				<td>${Reimbursement.reimbResolved}</td>
		                <td>${Reimbursement.reimbDescription}</td>
		                <td><button class="btn btn-primary btn-sm" onclick="showPic(this)">RECEIPT</button></td>
		    			    <td>${Reimbursement.author.ersUsername}</td>
		                <td>${Reimbursement.resolver.ersUsername}</td>
		                <td>${Reimbursement.status.reimbStatus}</td>
		                <td>${Reimbursement.type.reimbType}</td>
		                <td><button style="width:76px;" class="btn btn-success" onclick="approve(this)">Approve</button><br><br>
		                    <button style="width:76px;" class="btn btn-danger" onclick="deny(this)">Deny</button></td>
		            </tr>
		        		`;
	    			}); 
    }else{
	    reimbursements.forEach((Reimbursement)=>{
    		document.getElementById("table-body").innerHTML += `
            <tr>
                <td value="${Reimbursement.reimbusementId}">${Reimbursement.reimbusementId}</td>
                <td>${Reimbursement.reimbursementAmount}</td>
                <td>${Reimbursement.reimbSubmitted.year}-${Reimbursement.reimbSubmitted.monthValue}-${Reimbursement.reimbSubmitted.dayOfMonth} 
                ${Reimbursement.reimbSubmitted.hour}:${Reimbursement.reimbSubmitted.minute}:${Reimbursement.reimbSubmitted.second}</td>
    				<td>${Reimbursement.reimbResolved}</td>
                <td>${Reimbursement.reimbDescription}</td>
                <td><button class="btn btn-primary btn-sm" onclick="showPic(this)">RECEIPT</button></td>
    			    <td>${Reimbursement.author.ersUsername}</td>
                <td>${Reimbursement.resolver.ersUsername}</td>
                <td>${Reimbursement.status.reimbStatus}</td>
                <td>${Reimbursement.type.reimbType}</td>
                <td><button class="btn btn-danger" onclick="deleteRow(this)">Delete</button></td>
            </tr>
        		`;
			}); 
    }
} 
//function to prompt for confirmation of delete and add a listener....make sure to call module to confirm
function showPic(button)
{
    // var i=row.parentNode.parentNode.rowIndex;
	// document.getElementById("test-row").cells[0].attributes.value.value
	let reimbId = button.parentNode.parentNode.cells[0].attributes.value.value;
	console.log(reimbId);
	
	//DECLARATIONS
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        console.log(xhttp.response);
	        appendPicToModal(xhttp.response);
	        $("#show-pic").modal();
	    } else if (xhttp.readyState===4 && xhttp.status===500){
	    		console.log(xhttp.response);
	    		document.getElementById("receipt-image").innerHTML=`<h4>No Receipt Image for this Ticket</h4>`;
	        $("#show-pic").modal();
	    }
	}
    xhttp.open('GET','getReceipt/'+reimbId);
    xhttp.send(); 
	
}

//Function to append the pic to the modal
function appendPicToModal(response){
	
	var rawResponse = response; // truncated for example

	// convert to Base64
	//var b64Response = btoa(rawResponse);  //ATTEMPT 1

	// create an image
	var outputImg = document.createElement('img');
	//outputImg.src = 'data:image/png;base64,'+b64Response;  //ATTEMPT 1
	outputImg.src = 'data:image/png;base64,'+ btoa(unescape(encodeURIComponent(rawResponse)));  //ATTEMPT 2

	// append it to the modal
	
	//document.getElementById("receipt-image").appendChild(outputImg); //ATTEMPT 1/2
	document.getElementById("receipt-image").innerHTML=outputImg; //ATTEMPT3
	
//	var imgsrc = 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(markup)));
//	 var img = new Image(1, 1); // width, height values are optional params 
//	 img.src = imgsrc;
	
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
function approve(button)
{
    // var i=row.parentNode.parentNode.rowIndex;
	// document.getElementById("test-row").cells[0].attributes.value.value
	let reimbId = button.parentNode.parentNode.cells[0].attributes.value.value;
	console.log(reimbId);
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //call delete success modal
			$("#approved-modal").modal()
			setTimeout(function() {$('#approved-modal').modal('hide');}, 2000);
			
	    	//call retrieveAllPending("Status")again
    		retrieveAllPending("Status");
        }else if (xhttp.status===500){
			//call delete failed modal
        	$("#del-failure-modal").modal()
		}
	}
    xhttp.open('GET','approveRecord/'+reimbId);
    xhttp.send(); 

}
function deny(button)
{
    // var i=row.parentNode.parentNode.rowIndex;
	// document.getElementById("test-row").cells[0].attributes.value.value
	let reimbId = button.parentNode.parentNode.cells[0].attributes.value.value;
	console.log(reimbId);
	let xhttp= new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
	    console.log('readyState = ${xhttp.readyState}');
	    if (xhttp.readyState===4 && xhttp.status===200){
	        //call delete success modal
			$("#denied-modal").modal()
			setTimeout(function() {$('#denied-modal').modal('hide');}, 2000);
			
		    	//call retrieveAllPending("Status")again
	    		retrieveAllPending("Status");
        }else if (xhttp.status===500){
			//call delete failed modal
        	$("#del-failure-modal").modal()
		}
	}
    xhttp.open('GET','denyRecord/'+reimbId);
    xhttp.send(); 

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
	    		refreshResults(userId); //passing in userId but don't think I need it
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
refreshResults();

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

//passing in parameter optionally when deleted but don't think I need it
function refreshResults(UserIdOfDeleted){
	if (document.URL.includes("MyPending")){
		populateMyFields(1);
		retrievePending("Delete");
	}
	if (document.URL.includes("MyDelete")){
		populateMyFields(1);
		retrievePending("Delete");
	}
	if (document.URL.includes("MyPast")){
		populateMyFields(1);
		retrievePast();
	}
	if (document.URL.includes("managersearch")){
		populateMyFields(2);
		//doing below in populateFields() now
		//document.getElementById("replace-name").innerText = (': Search Results');
		retrieveSearchResults();
	}
	if (document.URL.includes("AllPending")){
		populateMyFields(3);
		retrieveAllPending("Delete");
		//doing below in populateFields() now
		//document.getElementById("replace-name").innerText = ': All Pending';
	}
	if (document.URL.includes("OtherDelete")){
		populateMyFields(3);
		retrieveAllPending("Delete");
		//doing below in populateFields() now
		//document.getElementById("replace-name").innerText = ': All Pending';
	}
	if (document.URL.includes("AllPast")){
		populateMyFields(4);
		retrieveAllPast();
		//doing below in populateFields() now
		//document.getElementById("replace-name").innerText = ': All Past';
	}
	if (document.URL.includes("StatusAll")){
		populateMyFields(5);
		retrieveAllPending("Status");
		//doing below in populateFields() now
		//document.getElementById("replace-name").innerText = ': Pending Requests';
	}
}