package com.testapp.client.pos;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UserAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	@Persistent
	private String email="";
	
	@Persistent
	private String phoneNumber="";
	
	@Persistent
	private String userName="";

	public UserAccount() {
		
	}
	
	public UserAccount( String email, String phoneNumber,
			String userName) {
		super();
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "UserAccount [email=" + email + ", key=" + key
				+ ", phoneNumber=" + phoneNumber + ", userName=" + userName
				+ "]";
	}
	
}
