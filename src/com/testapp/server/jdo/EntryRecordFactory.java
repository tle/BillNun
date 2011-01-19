package com.testapp.server.jdo;

import java.util.Date;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.testapp.client.dto.EntryRecord;

public class EntryRecordFactory extends PersistentObjectFactory<EntryRecord>{
	
	private static EntryRecordFactory instance = new EntryRecordFactory(); 
	
	public static EntryRecordFactory getInstance() {
		return instance;
	}
	
	public EntryRecordFactory () {
		
	}
	
	@Override
	protected Class<EntryRecord> getObjectClass() {
		return EntryRecord.class;
	}
	
	public EntryRecord newEntryRecord(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		EntryRecord record  = new EntryRecord( user.getEmail(), new Date(System.currentTimeMillis()));
		
		try {
			pm.makePersistent(record);
		} finally {
			pm.close();
		}
		
		return record;
	}
}
