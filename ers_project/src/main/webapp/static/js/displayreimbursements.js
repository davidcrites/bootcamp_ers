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

function appendResults(results){
    let reimbursements = JSON.parse(results);
    console.log(results);
    console.log(reimbursements);

    reimbursements.forEach((Reimbursement)=>{
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
    // NOTE: may need to resolver.ersUsername to "null" since these are pending
    //  <td>${Reimbursement.author.ersUsername}</td>
    //  <td>${Reimbursement.resolver.ersUsername}</td>
    // NOTE: reimbusementId intentionally misspelled...misspelled earlier in database.

    });
}    

//DETERMINE WHICH QUERY TO CALL
if (document.URL.includes("MyPending")){
	retrievePending();
 
}