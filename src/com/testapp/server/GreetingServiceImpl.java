package com.testapp.server;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.testapp.client.LoginInfo;
import com.testapp.client.api.GreetingService;
import com.testapp.client.api.GroupAPI;
import com.testapp.client.api.PaymentAPI;
import com.testapp.client.pos.EntryRecord;
import com.testapp.client.pos.Payment;
import com.testapp.client.pos.UserAccount;
import com.testapp.client.pos.UserGroup;
import com.testapp.client.pos.UserAccount.UserAccountStatus;
import com.testapp.server.jdo.EntryRecordFactory;
import com.testapp.server.jdo.FriendFactory;
import com.testapp.server.jdo.GroupFactory;
import com.testapp.server.jdo.PMF;
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
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService, GroupAPI, PaymentAPI {
	
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

	public void addUsersToGroup(long groupId, Collection<Long> users) {
		GroupFactory.getInstance().addUsersToGroup(groupId, users);
	}

	public Collection<Long> getGroupMembers(long groupId) {
		return GroupFactory.getInstance().getGroupMembers(groupId);
	}

	public boolean isMemberOf(long groupId, long userId) {
		return GroupFactory.getInstance().isMemberOf(groupId, userId);
	}

	public void updateUserAccount(UserAccount account) {
		UserAccountFactory.getInstance().save(account);
	}

	public List<UserAccount> getUserAccounts() {
		return UserAccountFactory.getInstance().getAll();
	}
	
	/**
	 * TODO: bc - might want to change this to a list of ids.  Otherwise we'd be required to pass the whole userAccount object around
	 * Considering we are passing in a list of UserAccount, we can assume that they are registerd
	 * @param whoPayed
	 * @param participants
	 * @param amount
	 * @param transactionDate
	 * @param description
	 */
	public void recordPayment (List<UserAccount> whoPayed , List<UserAccount> participants , 
			double amount , Date transactionDate , String description) {
		
		//TODO we really need to put shit in transaction 
		//TODO for now, we will just divided the balance evenly amonng the payers, in the future
		//i imagine we would pass in some sort of spec that tells how much each participant owns each payer
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			//record the payment
			Payment payment = new Payment();
			payment.setDescription(description);
			payment.setAmount(amount);
			payment.setWhoParticipated(extractId(participants));
			payment.setWhoPayed(extractId(whoPayed));
			payment.setDate(transactionDate);
			
			//update balances between friends
			for (UserAccount payer : whoPayed) {
				for (UserAccount payee : participants) {
					if (!payer.getKey().equals(payee.getKey())) {
						FriendFactory.getInstance().updateBalance(
								payer.getKey(), payee.getKey(), amount);
					}
				}
			}
		}
		finally {
			pm.close();
		}
	}
	
	private List<Long> extractId(List<UserAccount> userAccountList) {
		List<Long> idList = Collections.EMPTY_LIST;
		for (UserAccount account : userAccountList) {
			idList.add(account.getKey());
		}
		return idList;
	}
	
	public void generateTestData() {
		UserAccountFactory.getInstance().newUserAccount("chad.walters@billnun.com", "555-555-5555", "cwalters", UserAccountStatus.ACCEPTED);
		UserAccountFactory.getInstance().newUserAccount("trebor@billnun.com", "555-555-5555", "trebor", UserAccountStatus.ACCEPTED);
		UserAccountFactory.getInstance().newUserAccount("triet@billnun.com", "555-555-5555", "triet", UserAccountStatus.PENDING);
		UserAccountFactory.getInstance().newUserAccount("matt@billnun.com", "555-555-5555", "matt", UserAccountStatus.PENDING);
		UserAccountFactory.getInstance().newUserAccount("austin@billnun.com", "555-555-5555", "austin", UserAccountStatus.ACCEPTED);
		UserAccountFactory.getInstance().newUserAccount("leslie@billnun.com", "555-555-5555", "leslie", UserAccountStatus.ACCEPTED);
		UserAccountFactory.getInstance().newUserAccount("bryan@billnun.com", "555-555-5555", "bryan", UserAccountStatus.ACCEPTED);
		UserAccountFactory.getInstance().newUserAccount("damion@billnun.com", "555-555-5555", "damion", UserAccountStatus.ACCEPTED);
		UserAccountFactory.getInstance().newUserAccount("pratt@billnun.com", "555-555-5555", "pratt", UserAccountStatus.ACCEPTED);
		UserAccountFactory.getInstance().newUserAccount("graham@billnun.com", "555-555-5555", "graham", UserAccountStatus.ACCEPTED);
	}
	
	
}
