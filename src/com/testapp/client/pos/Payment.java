package com.testapp.client.pos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Bryan
 */

@PersistenceCapable
public class Payment implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	@Persistent
	private List<Long> whoPayed;

	@Persistent
	private List<Long> whoParticipated;
	
	@Persistent
	private double amount;

	@Persistent
	private Date date;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public List<Long> getWhoPayed() {
		return whoPayed;
	}

	public void setWhoPayed(List<Long> whoPayed) {
		this.whoPayed = whoPayed;
	}

	public List<Long> getWhoParticipated() {
		return whoParticipated;
	}

	public void setWhoParticipated(List<Long> whoParticipated) {
		this.whoParticipated = whoParticipated;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
