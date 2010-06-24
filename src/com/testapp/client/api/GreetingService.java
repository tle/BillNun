package com.testapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	public LoginInfo login(String requestUri);
	public List<EntryRecord> getRecords();
	public List<UserAccount> getUserAccounts();
	public List<Friend> getFriends();
	public void addFriend(String email);
	//public <T> List<T> getAllObjects(Class<T> clazz);
	public void updateUserAccount(UserAccount account);
}
