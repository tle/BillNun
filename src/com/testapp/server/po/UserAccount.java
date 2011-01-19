package com.testapp.server.po;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.io.Serializable;

@PersistenceCapable(detachable="true")
public class UserAccount implements Serializable {
	
	public static enum Status {
		PENDING , ACCEPTED ;
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

	@Persistent
	private String email="";

	@Persistent
	private String phoneNumber="";

	@Persistent
	private String userName="";

	@Persistent
	private Status status;

    public UserAccount(String email, String phoneNumber, String userName, Status status) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.status = status;
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
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserAccountDto [email=" + email + ", key=" + key
				+ ", phoneNumber=" + phoneNumber + ", userName=" + userName
				+ ", status=" + status
				+ "]";
	}
	
}
