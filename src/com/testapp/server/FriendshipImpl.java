package com.testapp.server;

import java.util.List;

import com.testapp.client.api.FriendshipAPI;
import com.testapp.client.dto.UserAccountDto;

public class FriendshipImpl implements FriendshipAPI {
	
	public static FriendshipAPI newInstance() {
		return new FriendshipImpl();
	}

    public List<UserAccountDto> getFriends(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserAccountDto addFriendship(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserAccountDto removeFriendship(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserAccountDto addFriendshipByEmail(String email) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserAccountDto removeFriendshipByEmail(String email) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isFriend(long userId) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
