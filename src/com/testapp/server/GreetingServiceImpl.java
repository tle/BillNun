package com.testapp.server;

import java.util.List;
import java.util.logging.Logger;
import com.testapp.client.LoginInfo;
import com.testapp.client.api.GreetingService;
import com.testapp.client.pos.EntryRecord;
import com.testapp.client.pos.UserAccount;
import com.testapp.client.pos.UserAccount.UserAccountStatus;
import com.testapp.server.jdo.EntryRecordFactory;
import com.testapp.server.jdo.FriendFactory;
import com.testapp.server.jdo.UserAccountFactory;
import com.testapp.shared.FieldVerifier;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	
	private static Logger logger = Logger.getLogger(GreetingServiceImpl.class.getName());
	
	private static LoginInfo currentUser = null;
	
	public static LoginInfo getCurrentLoginInfo() {
		return currentUser;
	}
	
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}
		
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent +"/n" ;
	}
	
	@Override
	public LoginInfo login(String requestUri) {
		this.currentUser = null; // Go ahead an invalidate the current user whenever we get a login request
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		
		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			
			createEntryRecord(user);
			createUserAccountPerLoginInfo(loginInfo);
		} else {
			//there already is an existed session
			loginInfo.setLoggedIn(false);
			loginInfo.setNewUser(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
			
		this.currentUser = loginInfo;
		
		return loginInfo;
	}
	
	private void createUserAccountPerLoginInfo(LoginInfo loginInfo) {
		
		String emailAddress = loginInfo.getEmailAddress();
		UserAccount account = UserAccountFactory.getInstance().getUserAccount(emailAddress);
		if (account == null) {
			account = UserAccountFactory.getInstance().newUserAccount(loginInfo.getEmailAddress(), 
					"xxx-xx-xxxx", "default_name"+System.currentTimeMillis(), 
					UserAccountStatus.ACCEPTED);
			loginInfo.setNewUser(true);			
		} else {
			loginInfo.setNewUser(false);
		}
		loginInfo.setAccount(account);
	}	
	
	private void createEntryRecord(User user) {
		EntryRecordFactory.getInstance().newEntryRecord(user);
	}

	@Override
	public List<EntryRecord> getRecords() {
		List<EntryRecord> result = EntryRecordFactory.getInstance().getAll();
		return result;
	}
	
	public List<UserAccount> getFriends() {
		return FriendFactory.getInstance().getFriendsOf(currentUser.getAccount().getKey());
	}
	
	/**
	 * Add a friend via email address.  If the email address is not already in the system, send them an invite.  If they are in the
	 * system, add an entry into the friend table between the current user, and the user they'd like to friend
	 * 
	 * @param email
	 */
	public void addFriend(String email) {
		FriendFactory.getInstance().addFriend(currentUser.getAccount().getKey(), email);
	}
	
	/**
	 * Get all the objects of a certain type.
	 * 
	 * This will probably mostly be used for debugging and test.
	 * 
	 */
//	public <T> ArrayList<T> getAllObjects(Class<T> clazz) {
//		return getAll(clazz);
//	}

	@Override
	public void updateUserAccount(UserAccount account) {
		UserAccountFactory.getInstance().save(account);
	}

	@Override
	public List<UserAccount> getUserAccounts() {
		return UserAccountFactory.getInstance().getAll();
	}
}
