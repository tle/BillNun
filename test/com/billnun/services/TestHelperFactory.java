package com.billnun.services;

import com.testapp.client.pos.UserAccount;
import com.testapp.server.jdo.UserAccountFactory;

public class TestHelperFactory {
	
	public static UserAccount createUser() {
		String name = "User_" + System.currentTimeMillis();
		UserAccount u = new UserAccount(name + "@billnun.test.com", "555-555-5555", name);
		UserAccountFactory.getInstance().save(u);
		return u;
	}
}
