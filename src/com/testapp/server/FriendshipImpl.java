package com.testapp.server;

import java.util.List;

import com.testapp.client.api.FriendshipAPI;
import com.testapp.client.pos.UserAccount;

public class FriendshipImpl implements FriendshipAPI {
	
	public static FriendshipAPI newInstance() {
		return new FriendshipImpl();
	}
	
	@Override
	public UserAccount addFriendship(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount addFriendshipByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserAccount> getFriends(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFriend(long userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserAccount removeFriendship(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount removeFriendshipByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
