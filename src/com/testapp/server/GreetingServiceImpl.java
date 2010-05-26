package com.testapp.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.testapp.client.EntryRecord;
import com.testapp.client.Friend;
import com.testapp.client.GreetingService;
import com.testapp.client.LoginInfo;
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
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		
		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			EntryRecord record  = new EntryRecord( user.getEmail(), new Date(System.currentTimeMillis()));
			
			try {
				pm.makePersistent(record);
			} finally {
				pm.close();
			}
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		
		
		return loginInfo;
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
	
	public List<Friend> getFriends(Long userId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Friend.class.getName());
		q.setFilter("userIdParam == userId");
		q.declareParameters("Long userIdParam");
		
		List<Friend> friends;
		try {
			friends = (List<Friend>) pm.newQuery(q).execute(userId);
			return wrapResults(friends);
		}
		finally {
			q.closeAll();
		}
	}
	
	@Override
	public void addFriend() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		
		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			EntryRecord record  = new EntryRecord( user.getEmail(), new Date(System.currentTimeMillis()));
			
			try {
				pm.makePersistent(record);
			} finally {
				pm.close();
			}
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		
		
		return loginInfo;
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
	
	
}
