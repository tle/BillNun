package com.testapp.client.api;

import java.util.Collection;
import java.util.List;

import com.testapp.client.pos.UserAccount;
import com.testapp.client.pos.UserGroup;

public interface GroupAPI {

	/**
	 * Add the users to the group
	 * @param user
	 */
	public void addUsersToGroup(long groupId, Collection<Long> users);
	
	/**
	 * Get a collection of user ids which are part of the group identified by groupId
	 * @param group
	 * @return
	 */
	public Collection<Long> getGroupMembers(long groupId);

	/**
	 * Returns whether or not the user is a member of the group
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public boolean isMemberOf(long groupId, long userId);
}
