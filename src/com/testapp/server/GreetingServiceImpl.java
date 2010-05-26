package com.testapp.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.testapp.client.EntryRecord;
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
			
			createEntryRecord(user);
			createNewUserAccount(loginInfo);
		} else {
			//there already is an existed session
			loginInfo.setLoggedIn(false);
			loginInfo.setNewUser(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
				
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
