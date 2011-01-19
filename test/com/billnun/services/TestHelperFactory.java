package com.billnun.services;

import com.testapp.server.jdo.UserAccountFactory;
import com.testapp.server.po.UserAccount;

public class TestHelperFactory {
	
	public static UserAccount createUser() {
		String name = "User_" + System.currentTimeMillis();
		UserAccount u = new UserAccount(name + "@billnun.test.com", "555-555-5555", name, UserAccount.Status.ACCEPTED);
		UserAccountFactory.getInstance().save(u);
		return u;
	}
}
