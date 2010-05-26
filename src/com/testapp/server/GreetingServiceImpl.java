package com.testapp.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.testapp.client.EntryRecord;
import com.testapp.client.Friend;
import com.testapp.client.GreetingService;
import com.testapp.client.LoginInfo;
import com.testapp.client.UserAccount;
import com.testapp.server.jdo.PMF;
import com.testapp.shared.FieldVerifier;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	
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
		
		PersistenceManagerFactory pmf = PMF.get();
		
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		String tst = pmf.toString();
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
			createNewUserAccount(loginInfo);
		} else {
			//there already is an existed session
			loginInfo.setLoggedIn(false);
			loginInfo.setNewUser(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
			
		this.currentUser = loginInfo;
		
		return loginInfo;
	}
	
	private void createNewUserAccount(LoginInfo loginInfo) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = " select from " + UserAccount.class.getName() +" where email == '"+loginInfo.getEmailAddress()+"'" ;
		List<UserAccount> accounts = (List<UserAccount>)pm.newQuery(query).execute();
		if (accounts.size() == 0) {
			try {
				//new user
				loginInfo.setNewUser(true);
				UserAccount account = new UserAccount(loginInfo.getEmailAddress(), "xxx-xx-xxxx", 
						"default_name"+System.currentTimeMillis());
				account = pm.makePersistent(account);
				loginInfo.setAccount(account);
				
			} finally {
				pm.close();
			}
		} else {
			loginInfo.setAccount(accounts.get(0));//there should be only one
			loginInfo.setNewUser(false);
		}
	}

	/**
	 * Get all the UserAccounts in the system
	 * 
	 * @return
	 */
	public List<UserAccount> getUserAccounts() {
		return getAll(UserAccount.class);
	}
	
	private void createEntryRecord(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		EntryRecord record  = new EntryRecord( user.getEmail(), new Date(System.currentTimeMillis()));
		
		try {
			pm.makePersistent(record);
		} finally {
			pm.close();
		}
	}

	@Override
	public List<EntryRecord> getRecords() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from "+EntryRecord.class.getName();
		List<EntryRecord> greetings = (List<EntryRecord>)pm.newQuery(query).execute();
		List<EntryRecord> result = new ArrayList<EntryRecord>();
		for (EntryRecord rec: greetings) {
			result.add(rec);
		}
		return result;
	}
	
	public List<Friend> getFriends() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Friend.class);
		q.setFilter("userId == userIdParam");
		q.declareParameters("Long userIdParam");
		
		List<Friend> friends;
		try {
			friends = (List<Friend>) pm.newQuery(q).execute(currentUser.getAccount().getKey());
			return wrapResults(friends);
		}
		finally {
			q.closeAll();
		}
	}
	
	/**
	 * Add a friend via email address.  If the email address is not already in the system, send them an invite.  If they are in the
	 * system, add an entry into the friend table between the current user, and the user they'd like to friend
	 * 
	 * @param email
	 */
	public void addFriend(String email) {
		//version 1 just add it to the table
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(UserAccount.class);
		q.setFilter("email == emailParam");
		q.declareParameters("String emailParam");
		
		try{
			List<UserAccount> accounts = (List<UserAccount>)pm.newQuery(q).execute(email);
			if(accounts.size()==0) {
				//Need to send out Invite
			} 
			else if (accounts.size() ==1) {
				try {
					UserAccount account = accounts.get(0);
					Friend newFriend = new Friend();
					newFriend.setUserId(currentUser.getAccount().getKey());
					newFriend.setFriendUserId(account.getKey());
					newFriend.setBalance(0);
					pm.makePersistent(newFriend);
				}
				finally {
					pm.close();
				}
			}
			else {
				logger.log(Level.SEVERE, "There are two user accounts with the same email address:  " + accounts.toString());
			}
		}
		finally {
			q.closeAll();
		}
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
	
	/**
	 * Generic method to get all objects of a certain type
	 * 
	 * @param <T>
	 * @return
	 */
	private <T> ArrayList<T> getAll(Class<T> clazz) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(clazz);
		
		List<T> objects;
		try {
			objects = (List<T>) pm.newQuery(q).execute();
			return wrapResults(objects);
		}
		finally {
			q.closeAll();
		}
	}
	
	/**
	 * Iterates through result set and puts it into an ArrayList.
	 * 
	 * Should be used after a query execute call.
	 * 
	 * @param <T>
	 * @param results
	 * @return
	 */
	private <T> ArrayList<T> wrapResults(List<T> results) {
		ArrayList<T> resultList = new ArrayList<T>();
		for(T result: results) {
			resultList.add(result);
		}
		return resultList;
	}

	@Override
	public void updateUserAccount(UserAccount account) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserAccount acc = pm.getObjectById(UserAccount.class, account.getKey());
			acc.setEmail(account.getEmail());
			acc.setPhoneNumber(account.getPhoneNumber());
			acc.setUserName(account.getUserName());
		} finally {
			pm.close();
		}
	}
}
