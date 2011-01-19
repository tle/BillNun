package com.testapp.client.api;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.testapp.client.LoginInfo;
import com.testapp.client.dto.EntryRecord;
import com.testapp.client.dto.UserAccountDto;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void login(String requestUri, AsyncCallback<LoginInfo> async);
	
	void getRecords(AsyncCallback<List<EntryRecord>> async);
	
	void getUserAccounts(AsyncCallback<List<UserAccountDto>> async);
		
	void getFriends(AsyncCallback<List<UserAccountDto>> async);
	
	void addFriend(String email, AsyncCallback<Void> async);

	void updateUserAccount(UserAccountDto account, AsyncCallback<Void> async);
}
