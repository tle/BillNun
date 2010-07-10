package com.testapp.server.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.testapp.client.pos.Friend;
import com.testapp.client.pos.UserAccount;
import com.testapp.client.pos.UserAccount.UserAccountStatus;

public class FriendFactory extends PersistentObjectFactory<Friend> {
	
	private static Logger logger = Logger.getLogger(FriendFactory.class.getName());
	
	private static FriendFactory instance = new FriendFactory();
	
	public static FriendFactory getInstance() {
		return instance;
	}
	
	public FriendFactory() {
	}
	
	@Override
	protected Class<Friend> getObjectClass() {
		return Friend.class;
	}
	
	public List<UserAccount> getFriendsOf(Long userKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Get all the account ids of the friends
		Query q1 = pm.newQuery("select friendAccountId from " + Friend.class.getName());
		q1.setFilter("userId == userIdParam");
		q1.declareParameters("Long userIdParam");
				
		try {
			List<Long> friendUserIds = (List<Long>) pm.newQuery(q1).execute(userKey);
			// Look up all the user account objects with the given friendUserIds
			return UserAccountFactory.getInstance().getUserAccounts(friendUserIds);
		}
		finally {
			q1.closeAll();
		}
	}
	
	public void addFriend(Long userId, String email) {
		//		version 1 just add it to the table
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(UserAccount.class);
		q.setFilter("email == emailParam");
		q.declareParameters("String emailParam");
		
		try{
			List<UserAccount> accounts = (List<UserAccount>)pm.newQuery(q).execute(email);
			UserAccount account = null;
			if(accounts.size()==0) {
				//TODO Need to send out Invite
				
				//for now we will create a user account with a PENDING status
				account =
					UserAccountFactory.getInstance().newUserAccount(email, "xxx-xx-xxxx",
							"default_name"+System.currentTimeMillis(), 
							UserAccountStatus.PENDING);
			} 
			else if (accounts.size() ==1) {
				account = accounts.get(0);
			}
			else {
				logger.log(Level.SEVERE, "There are two user accounts with the same email address:  " + accounts.toString());
			}
			if (account!=null) {
				try {
					Friend newFriend = new Friend();
					newFriend.setUserId(userId);
					newFriend.setFriendAccountId(account.getKey());
					newFriend.setBalance(0);
					pm.makePersistent(newFriend);
				}
				finally {
					pm.close();
				}
			}
		}
		finally {
			q.closeAll();
		}
	}
	
	public void updateBalance (Long userId, Long friendAccountId, double newBalance) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Extent<Friend> extent = pm.getExtent(Friend.class, false);

			String filter =
				"userId == user && friendAccountId == friendAccount";
			Query query = pm.newQuery(extent, filter);
			query.declareParameters("Long user, Long friendAccount");     


			Collection<Friend> friends = 
				(Collection<Friend>)query.executeWithArray(userId, friendAccountId);

			if (friends.size() > 0) {
				//there should be only one
				Friend friend = friends.iterator().next();
				friend.setBalance(newBalance);								
			}
			
//			friends =
//				(Collection<Friend>)query.executeWithArray( friendAccountId, userId);
//			if (friends.size() > 0) {
//				//there should be only one
//				Friend friend = friends.iterator().next();
//				friend.setBalance(-1*newBalance);								
//			}
			
		} finally {
			pm.close();
		}
	}
	
	
}
