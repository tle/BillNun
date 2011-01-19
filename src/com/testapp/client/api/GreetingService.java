package com.testapp.client.api;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.testapp.client.LoginInfo;
import com.testapp.client.dto.EntryRecord;
import com.testapp.client.dto.UserAccountDto;
import com.testapp.server.po.UserAccount;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	public LoginInfo login(String requestUri);
	public List<EntryRecord> getRecords();
	public List<UserAccount> getUserAccounts();
	public List<UserAccount> getFriends();
	public void addFriend(String email);
	public void updateUserAccount(UserAccount account);
}
