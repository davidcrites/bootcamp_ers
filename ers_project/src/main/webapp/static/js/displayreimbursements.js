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
                <td>${Reimbursement.reimbursementId}</td>
                <td>${Reimbursement.reimbursementAmount}</td>
                <td>${Reimbursement.reimbSubmitted}</td>
                <td>${Reimbursement.reimbResolved}</td>
                <td>${Reimbursement.reimbDescription}</td>
                <td>RECEIPT</td>
                <td>${Reimbursement.authorId}</td>
                <td>test</td>
                <td>${Reimbursement.status.reimbStatus}</td>
                <td>${Reimbursement.type.reimbType}</td>
            </tr>
    `;
    // NOTE: even though the author User and resolver User objects
    // attached to a reimbursement are present on the object before
    // writing to the response, the ObjectWriter is not correctly
    // adding them to the response for some reason..I have pulled
    // those fields out temporarily until I figure out why..replaced
    // with the authorID and "test"
    //  <td>${Reimbursement.author.ersUsername}</td>
    //  <td>${Reimbursement.resolver.ersUsername}</td>

    });
}    

//DETERMINE WHICH QUERY TO CALL
if (document.URL.includes("MyPending")){
	retrievePending();
 
}