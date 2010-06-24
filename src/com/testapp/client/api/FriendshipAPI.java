package com.testapp.client.api;

import java.util.List;

import com.testapp.client.pos.UserAccount;

/**
 * Currently not used anywhere but starting to document the different 
 * logical pieces of the services available to the client
 * 
 * @author Bryan
 *
 */
public interface FriendshipAPI {
	
	/**
	 * Get all the friends of the user with the userId
	 * @param userId
	 * @return
	 */
	List<UserAccount> getFriends(long userId);
	
	/**
	 * Sends a request from the current user to the user with the userId to become friends
	 * @param userId
	 * @return
	 */
	UserAccount addFriendship(long userId);
	
	/**
	 * Removes the friendship of the current user and the user with the userId
	 * @param userId
	 * @return
	 */
	UserAccount removeFriendship(long userId);

	/**
	 * Same as addFriend but using email as unique identifier for user
	 * @param email
	 * @return
	 */
	UserAccount addFriendshipByEmail(String email);
	
	/**
	 * Same as removeFriendship but using the email as the identier for the user
	 * @param email
	 * @return
	 */
	UserAccount removeFriendshipByEmail(String email);

	/**
	 * Is the user with the userId a friend of the current user
	 * @param userId
	 * @return
	 */
	boolean isFriend(long userId);
	
	
}
