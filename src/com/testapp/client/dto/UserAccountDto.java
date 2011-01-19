package com.testapp.client.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="userAccount")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAccountDto {
	
	public enum Status {
		PENDING , ACCEPTED ;
	}

    @XmlElement(name="key")
	private Long key;

    @XmlElement(name="email")
	private String email="";

    @XmlElement(name="phoneNumber")
	private String phoneNumber="";

    @XmlElement(name="username")
	private String userName="";

    @XmlElement(name="status")
	private Status status;
       
	public UserAccountDto() {}
	
	public UserAccountDto(String email, String phoneNumber, String userName) {
		this (email,phoneNumber, userName, Status.ACCEPTED);
	}
	
	public UserAccountDto(String email, String phoneNumber,
                          String userName, Status status) {
		super();
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
