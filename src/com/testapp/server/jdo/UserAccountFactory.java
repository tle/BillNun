package com.testapp.server.jdo;

import com.testapp.server.po.UserAccount;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


public class UserAccountFactory extends PersistentObjectFactory<UserAccount> {
	
	private static UserAccountFactory instance = new UserAccountFactory();
	
	public static UserAccountFactory getInstance () {
		return instance;
	}
	
	public UserAccountFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public UserAccount newUserAccount(String email, String phoneNumber, String username, UserAccount.Status status ) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserAccount account = null;
		try {

			account = new UserAccount(email, phoneNumber, username, status);
			account = pm.makePersistent(account);
			return account;
		} finally {
			pm.close();
		}
	}

    public UserAccount newUserAccount(UserAccount userAccount) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
        	UserAccount ua = pm.makePersistent(userAccount);
            return pm.detachCopy(ua);
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
	
	/**
	 * Get all the UserAccounts with userIds that exist in the userIds list
	 * 
	 * @param userIds - list of all the user ids to look up.  Duplicates are okay as they are filtered out by the query.
	 * @return
	 */
	public List<UserAccount> getUserAccounts(List<Long> userIds) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(UserAccount.class, ":p.contains(key)");
		if (userIds.size() == 0) {
			return  new ArrayList<UserAccount>();
		}
		List<UserAccount> accounts = (List<UserAccount>) query.execute(userIds);
		return wrapResults(accounts);
	}
	
	
	@Override
	protected Class<UserAccount> getObjectClass() {
		return UserAccount.class;
	}
}
