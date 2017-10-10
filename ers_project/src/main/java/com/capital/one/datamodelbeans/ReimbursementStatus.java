package com.capital.one.datamodelbeans;

/**
 * Class used to represent data model table for table ers_reimbursement_status
 * 
 * @author David Crites
 *
 */
public class ReimbursementStatus {
	
	private int reimbStatusId = 0;
	private String reimbStatus = "";
	
	public ReimbursementStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReimbursementStatus(int reimbStatusId, String reimbStatus) {
		super();
		this.reimbStatusId = reimbStatusId;
		this.reimbStatus = reimbStatus;
	}
	public int getReimbStatusId() {
		return reimbStatusId;
	}
	public void setReimbStatusId(int reimbStatusId) {
		this.reimbStatusId = reimbStatusId;
	}
	public String getReimbStatus() {
		return reimbStatus;
	}
	public void setReimbStatus(String reimbStatus) {
		this.reimbStatus = reimbStatus;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reimbStatus == null) ? 0 : reimbStatus.hashCode());
		result = prime * result + reimbStatusId;
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
		ReimbursementStatus other = (ReimbursementStatus) obj;
		if (reimbStatus == null) {
			if (other.reimbStatus != null)
				return false;
		} else if (!reimbStatus.equals(other.reimbStatus))
			return false;
		if (reimbStatusId != other.reimbStatusId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ReimbursementStatus [reimbStatusId=" + reimbStatusId + ", reimbStatus=" + reimbStatus + "]";
	}
	
	

}
