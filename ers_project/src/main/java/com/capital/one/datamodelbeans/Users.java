package com.capital.one.datamodelbeans;

import com.capital.one.daos.DAOUtilities;

/**
 * Class used to represent data model table for table ers_users
 * 
 * @author David Crites
 *
 */
public class Users {
	
	private int ersUsersId = 0;
	private String ersUsername = "";
	private String ersPassword = "";
	private String userFirstName = "";
	private String userLastName = "";
	private String userEmail = "";
	private int userRoleId = 0;
	
	//*****IMPORTANT********
	//TODO: Need to calls setter of UserRoles object in field constructor with userRoleId
	//TODO: In the setter, need to call a DAO method to look up the role and set it for the RoleId
	private UserRoles role = new UserRoles();
	
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Users(int ersUsersId, String ersUsername, String ersPassword, String userFirstName, String userLastName,
			String userEmail, int userRoleId) {
		super();
		this.ersUsersId = ersUsersId;
		this.ersUsername = ersUsername;
		this.ersPassword = ersPassword;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userEmail = userEmail;
		this.userRoleId = userRoleId;
		this.role.setErsUserRoleid(userRoleId); //this will set the RoleId and Role of the Role
	}
	public int getErsUsersId() {
		return ersUsersId;
	}
	public void setErsUsersId(int ersUsersId) {
		this.ersUsersId = ersUsersId;
	}
	public String getErsUsername() {
		return ersUsername;
	}
	public void setErsUsername(String ersUsername) {
		this.ersUsername = ersUsername;
	}
	public String getErsPassword() {
		return ersPassword;
	}
	public void setErsPassword(String ersPassword) {
		this.ersPassword = ersPassword;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public int getUserRoleId() {
		return userRoleId;
	}

	public UserRoles getRole() {
		return role;
	}
	public void setRole(int userRoleId) {
		//String newRole = [Call DAO method to return Role using userRoleId]
		String newRole = DAOUtilities.getEmployeeRole(userRoleId);
		this.userRoleId = userRoleId;
		this.role.setErsUserRoleid(userRoleId);
		this.role.setUserRole(newRole);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ersPassword == null) ? 0 : ersPassword.hashCode());
		result = prime * result + ((ersUsername == null) ? 0 : ersUsername.hashCode());
		result = prime * result + ersUsersId;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result + ((userFirstName == null) ? 0 : userFirstName.hashCode());
		result = prime * result + ((userLastName == null) ? 0 : userLastName.hashCode());
		result = prime * result + userRoleId;
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
		Users other = (Users) obj;
		if (ersPassword == null) {
			if (other.ersPassword != null)
				return false;
		} else if (!ersPassword.equals(other.ersPassword))
			return false;
		if (ersUsername == null) {
			if (other.ersUsername != null)
				return false;
		} else if (!ersUsername.equals(other.ersUsername))
			return false;
		if (ersUsersId != other.ersUsersId)
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		if (userFirstName == null) {
			if (other.userFirstName != null)
				return false;
		} else if (!userFirstName.equals(other.userFirstName))
			return false;
		if (userLastName == null) {
			if (other.userLastName != null)
				return false;
		} else if (!userLastName.equals(other.userLastName))
			return false;
		if (userRoleId != other.userRoleId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Users [ersUsersId=" + ersUsersId + ", ersUsername=" + ersUsername + ", ersPassword=" + ersPassword
				+ ", userFirstName=" + userFirstName + ", userLastName=" + userLastName + ", userEmail=" + userEmail
				+ ", userRoleId=" + userRoleId + ", role=" + role + "]";
	}
	

}
