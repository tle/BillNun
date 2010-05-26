package com.testapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void login(String requestUri, AsyncCallback<LoginInfo> async);
	
	void getRecords(AsyncCallback<List<EntryRecord>> async);
	
	void getUserAccounts(AsyncCallback<List<UserAccount>> async);
		
	void getFriends(AsyncCallback<List<Friend>> async);
	
	void addFriend(String email, AsyncCallback<Void> async);
	
	<T> void getAllObjects(Class<T> clazz, AsyncCallback<List<T>> async);

	void updateUserAccount(UserAccount account, AsyncCallback<Void> async);
}
