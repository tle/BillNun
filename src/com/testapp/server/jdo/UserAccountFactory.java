package com.testapp.server.jdo;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.testapp.client.UserAccount;
import com.testapp.client.UserAccount.UserAccountStatus;

public class UserAccountFactory extends PersistentObjectFactory<UserAccount> {
	
	private static UserAccountFactory instance = new UserAccountFactory();
	
	public static UserAccountFactory getInstance () {
		return instance;
	}
	
	public UserAccountFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public UserAccount newUserAccount(String email, String phoneNumber, String username, UserAccountStatus status ) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserAccount account = null;
		try {

			account = new UserAccount(email, "xxx-xx-xxxx", 
					"default_name"+System.currentTimeMillis(), status);
			account = pm.makePersistent(account);
			return account;
		} finally {
			pm.close();
		}
	}
	
	public UserAccount getUserAccount(String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = " select from " + UserAccount.class.getName() +" where email == '"+email+"'" ;
		List<UserAccount> accounts = (List<UserAccount>)pm.newQuery(query).execute();
		if (accounts!=null && accounts.size()>0) {
			return accounts.get(0); 
		} else {
			return null;
		}
	}
	
	public void save(UserAccount account) {
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

	@Override
	protected Class<UserAccount> getObjectClass() {
		return UserAccount.class;
	}
	
	
}
