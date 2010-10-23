package com.testapp.server;

import java.util.List;

import com.testapp.client.api.FriendshipAPI;
import com.testapp.client.pos.UserAccount;

public class FriendshipImpl implements FriendshipAPI {
	
	public static FriendshipAPI newInstance() {
		return new FriendshipImpl();
	}

    public List<UserAccount> getFriends(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserAccount addFriendship(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserAccount removeFriendship(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserAccount addFriendshipByEmail(String email) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserAccount removeFriendshipByEmail(String email) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isFriend(long userId) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
