package com.testapp.client.api;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.testapp.client.LoginInfo;
import com.testapp.client.pos.EntryRecord;
import com.testapp.client.pos.UserAccount;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void login(String requestUri, AsyncCallback<LoginInfo> async);
	
	void getRecords(AsyncCallback<List<EntryRecord>> async);
	
	void getUserAccounts(AsyncCallback<List<UserAccount>> async);
		
	void getFriends(AsyncCallback<List<UserAccount>> async);
	
	void addFriend(String email, AsyncCallback<Void> async);

	void updateUserAccount(UserAccount account, AsyncCallback<Void> async);
}
