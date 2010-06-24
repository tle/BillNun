package com.testapp.client;

import java.io.Serializable;

import com.testapp.client.pos.UserAccount;

public class LoginInfo implements Serializable {

	private boolean loggedIn = false;
	private boolean newUser;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;
	
	private UserAccount account;

	public boolean isLoggedIn() {
		///stuff
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

	public UserAccount getAccount() {
		return account;
	}
	
	
}