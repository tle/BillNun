package com.testapp.client.pos;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="userAccount")
@XmlAccessorType(XmlAccessType.FIELD)
@PersistenceCapable(detachable="true")
public class UserAccount implements Serializable {
	
	public enum UserAccountStatus {
		PENDING , ACCEPTED ;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @XmlElement(name="key")
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

    @XmlElement(name="email")
	@Persistent
	private String email="";

    @XmlElement(name="phoneNumber")
	@Persistent
	private String phoneNumber="";

    @XmlElement(name="username")
	@Persistent
	private String userName="";

    @XmlElement(name="status")
	@Persistent
	private UserAccountStatus status;
       
	public UserAccount() {
		
	}
	
	public UserAccount( String email, String phoneNumber,
			String userName) {
		this (email,phoneNumber, userName, UserAccountStatus.ACCEPTED);
	}
	
	public UserAccount(String email, String phoneNumber,
			String userName, UserAccountStatus status) {
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
	
	public UserAccountStatus getStatus() {
		return status;
	}
	
	public void setStatus(UserAccountStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserAccount [email=" + email + ", key=" + key
				+ ", phoneNumber=" + phoneNumber + ", userName=" + userName
				+ ", status=" + status
				+ "]";
	}
	
}
