package com.capital.one.datamodelbeans;

/**
 * Class used to represent data model table for table ers_user_roles
 * 
 * @author David Crites
 *
 */
public class UserRoles {
	
	private int ersUserRoleid = 0;
	private String userRole = "";
	
	public UserRoles() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserRoles(int ersUserRoleid, String userRole) {
		super();
		this.ersUserRoleid = ersUserRoleid;
		this.userRole = userRole;
	}
	public int getErsUserRoleid() {
		return ersUserRoleid;
	}
	public void setErsUserRoleid(int ersUserRoleid) {
		this.ersUserRoleid = ersUserRoleid;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ersUserRoleid;
		result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
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
		UserRoles other = (UserRoles) obj;
		if (ersUserRoleid != other.ersUserRoleid)
			return false;
		if (userRole == null) {
			if (other.userRole != null)
				return false;
		} else if (!userRole.equals(other.userRole))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UserRoles [ersUserRoleid=" + ersUserRoleid + ", userRole=" + userRole + "]";
	}
	
	

}
