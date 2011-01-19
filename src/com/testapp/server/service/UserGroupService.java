package com.testapp.server.service;


import com.testapp.server.jdo.UserAccountFactory;
import com.testapp.server.po.UserAccount;

import java.util.List;

public class UserGroupService {

    private static UserGroupService instance = new UserGroupService();

	public static UserGroupService getInstance() {
		return instance;
	}

    public UserAccount createUser(UserAccount userAccount) {
        //do some checking on the userAccount fields
        return UserAccountFactory.getInstance().newUserAccount(userAccount);
    }

    public List<UserAccount> getUsers() {
        return UserAccountFactory.getInstance().getAll();
    }
}
