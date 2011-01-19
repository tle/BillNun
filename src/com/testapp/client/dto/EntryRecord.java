package com.testapp.client.dto;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
public class EntryRecord implements Serializable{
	
	/**   a nother change
	 * 
	 * kkkk
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	@Persistent
	private String person;
	
	@Persistent
	private Date date;
	
	public EntryRecord() {
		// TODO Auto-generated constructor stub
		this.person ="x";
		this.date = new Date(System.currentTimeMillis());
	}

	public EntryRecord( String person, Date date) {
		super();
		this.person = person;
		this.date = date;
	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		String value = person+ " signed in at " +date;
		return value;
	}
	
}
