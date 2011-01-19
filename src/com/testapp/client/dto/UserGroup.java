package com.testapp.client.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
public class UserGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	@Persistent
	private String name="";
	
	@Persistent
	private Long createdBy;
	
	@Persistent
	private Set<Long> users = new HashSet<Long>();
	
	public UserGroup() {}
	
	public UserGroup(String name, Long createdBy) {
		super();
		this.name = name;
		this.createdBy = createdBy;
		this.users = new HashSet<Long>();
	}

	public boolean isMemberOf(long userId) {
		return users.contains(userId);
	}
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Set<Long> getMembers() {
		return users;
	}

	public void addMembers(Collection<Long> users) {
		this.users.addAll(users);
	}
	
	public void setMembers(Set<Long> users) {
		this.users = users;
	}
	
	@Override
	public String toString() {
		return "UserGroup [key=" + key + ", name=" + name + ", createdBy="
				+ createdBy + ", members=" + users + "]";
	}
	
	
}
