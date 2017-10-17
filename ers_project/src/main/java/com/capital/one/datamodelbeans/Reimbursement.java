package com.capital.one.datamodelbeans;

import java.time.LocalDateTime;

import com.capital.one.daos.DAOUtilities;
import com.capital.one.daos.EmployeeDAO;

/**
 * Class used to represent data model table for table ers_reimbursement
 * 
 * @author David Crites
 *
 */
public class Reimbursement {
	
	private int reimbusementId = 0;
	private double reimbursementAmount = 0D;
	private LocalDateTime reimbSubmitted;
	private LocalDateTime reimbResolved;
	private String reimbDescription = "";
	private int reimbAuthor = 0;
	private int reimbResolver = 0;
	private int reimbStatusId = 0;
	private int reimbTypeId = 0;
	//********************IMPORTANT**************************************
	// TODO: need to call setter of the below objects in the constructor
	// TODO: In setters, need to call a DAO method to look up these using the ID and set them.
	private Users author = new Users();
	private Users resolver = new Users();
	private ReimbursementStatus status = new ReimbursementStatus();
	private ReimbursementType type = new ReimbursementType();
	
	
	public Reimbursement() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Reimbursement(int reimbusementId, double reimbursementAmount, LocalDateTime reimbSubmitted,
			LocalDateTime reimbResolved, String reimbDescription, int reimbAuthor, int reimbResolver, int reimbStatusId,
			int reimbTypeId) {
		super();
		this.reimbusementId = reimbusementId;
		this.reimbursementAmount = reimbursementAmount;
		this.reimbSubmitted = reimbSubmitted;
		this.reimbResolved = reimbResolved;
		this.reimbDescription = reimbDescription;
		this.setReimbAuthor(reimbAuthor);
		this.setReimbResolver(reimbResolver);
		this.setReimbStatusId(reimbStatusId);
		this.setReimbTypeId(reimbTypeId);
//		**the below will be set privately by the sets above**
//		this.setAuthorId(reimbAuthor);  //set Author-> ID and let that set username for Author for now
//		this.setResolverId(reimbResolver); //set Resolver-> ID and let that set username for Resolver for now
//		this.setReimbStatusId(reimbStatusId);//set status->id and let that set status->status
//		this.setReimbTypeId(reimbTypeId);//set type->id and let that set type->type
		
		
	}
	public int getReimbusementId() {
		return reimbusementId;
	}
	public void setReimbusementId(int reimbusementId) {
		this.reimbusementId = reimbusementId;
	}
	public double getReimbursementAmount() {
		return reimbursementAmount;
	}
	public void setReimbursementAmount(double reimbursementAmount) {
		this.reimbursementAmount = reimbursementAmount;
	}
	public LocalDateTime getReimbSubmitted() {
		return reimbSubmitted;
	}
	public void setReimbSubmitted(LocalDateTime reimbSubmitted) {
		this.reimbSubmitted = reimbSubmitted;
	}
	public LocalDateTime getReimbResolved() {
		return reimbResolved;
	}
	public void setReimbResolved(LocalDateTime reimbResolved) {
		this.reimbResolved = reimbResolved;
	}
	public String getReimbDescription() {
		return reimbDescription;
	}
	public void setReimbDescription(String reimbDescription) {
		this.reimbDescription = reimbDescription;
	}
	public int getReimbAuthor() {
		return reimbAuthor;
	}
	public void setReimbAuthor(int reimbAuthor) {//**************also calls method to set name/id of author user
		this.reimbAuthor = reimbAuthor;
		this.setAuthorId(reimbAuthor);
	}
	public int getReimbResolver() {
		return reimbResolver;
	}
	public void setReimbResolver(int reimbResolver) {//************also calls method to set name/id of resolver user
		this.reimbResolver = reimbResolver;
		this.setResolverId(reimbResolver);
	}
	public int getReimbStatusId() {
		return reimbStatusId;
	}
	public void setReimbStatusId(int reimbStatusId) {//************also calls method to set name/id of status
		this.reimbStatusId = reimbStatusId;
		this.setStatus(reimbStatusId);
	}
	public int getReimbTypeId() {
		return reimbTypeId;
	}
	public void setReimbTypeId(int reimbTypeId) {//***********also calls method to set name/id of type
		this.reimbTypeId = reimbTypeId;
		this.setType(reimbTypeId);
	}
	public int getAuthorId() {
		return author.getErsUsersId();
	}
	private void setAuthorId(int authorId) {
		this.author.setErsUsersId(authorId);
		String name = DAOUtilities.getEmployeeUsername(authorId);
		this.author.setErsUsername(name);
	}
	public int getResolverId() {
		return resolver.getErsUsersId();
	}
	private void setResolverId(int resolverId) {
		this.resolver.setErsUsersId(resolverId);
		String name = DAOUtilities.getEmployeeUsername(resolverId);
		this.resolver.setErsUsername(name);
	}
	public ReimbursementStatus getStatus() {
		return status;
	}
	private void setStatus(int statusId) {
		this.status.setReimbStatusId(statusId);
		String newStatus = DAOUtilities.getReimbursementStatus(statusId);
		this.status.setReimbStatus(newStatus);
	}
	public ReimbursementType getType() {
		return type;
	}
	private void setType(int typeId) {
		this.type.setReimbTypeId(typeId);
		String newType = DAOUtilities.getReimbursementType(typeId);
		this.type.setReimbType(newType);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + reimbAuthor;
		result = prime * result + ((reimbDescription == null) ? 0 : reimbDescription.hashCode());
		result = prime * result + ((reimbResolved == null) ? 0 : reimbResolved.hashCode());
		result = prime * result + reimbResolver;
		result = prime * result + reimbStatusId;
		result = prime * result + ((reimbSubmitted == null) ? 0 : reimbSubmitted.hashCode());
		result = prime * result + reimbTypeId;
		long temp;
		temp = Double.doubleToLongBits(reimbursementAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + reimbusementId;
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (reimbAuthor != other.reimbAuthor)
			return false;
		if (reimbDescription == null) {
			if (other.reimbDescription != null)
				return false;
		} else if (!reimbDescription.equals(other.reimbDescription))
			return false;
		if (reimbResolved == null) {
			if (other.reimbResolved != null)
				return false;
		} else if (!reimbResolved.equals(other.reimbResolved))
			return false;
		if (reimbResolver != other.reimbResolver)
			return false;
		if (reimbStatusId != other.reimbStatusId)
			return false;
		if (reimbSubmitted == null) {
			if (other.reimbSubmitted != null)
				return false;
		} else if (!reimbSubmitted.equals(other.reimbSubmitted))
			return false;
		if (reimbTypeId != other.reimbTypeId)
			return false;
		if (Double.doubleToLongBits(reimbursementAmount) != Double.doubleToLongBits(other.reimbursementAmount))
			return false;
		if (reimbusementId != other.reimbusementId)
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Reimbursement [reimbusementId=" + reimbusementId + ", reimbursementAmount=" + reimbursementAmount
				+ ", reimbSubmitted=" + reimbSubmitted + ", reimbResolved=" + reimbResolved + ", reimbDescription="
				+ reimbDescription + ", reimbAuthor=" + reimbAuthor + ", reimbResolver=" + reimbResolver
				+ ", reimbStatusId=" + reimbStatusId + ", reimbTypeId=" + reimbTypeId + ", author=" + author
				+ ", resolver=" + resolver + ", status=" + status + ", type=" + type + "]";
	}
	
	

}
