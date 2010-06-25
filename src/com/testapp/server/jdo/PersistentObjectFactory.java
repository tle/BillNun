package com.testapp.server.jdo;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public abstract class PersistentObjectFactory<T> {
	
	
	
	/**
	 * Generic method to get all objects of a certain type
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> ArrayList<T> getAll() {
		Class<T> clazz = (Class<T>) getObjectClass();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(clazz);
		
		List<T> objects;
		try {
			objects = (List<T>) pm.newQuery(q).execute();
			return wrapResults(objects);
		}
		finally {
			q.closeAll();
		}
	}
	
	/**
	 * Iterates through result set and puts it into an ArrayList.
	 * 
	 * Should be used after a query execute call.
	 * 
	 * @param <T>
	 * @param results
	 * @return
	 */
	protected <T> ArrayList<T> wrapResults(List<T> results) {
		ArrayList<T> resultList = new ArrayList<T>();
		for(T result: results) {
			resultList.add(result);
		}
		return resultList;
	}
	
	protected abstract Class<T> getObjectClass();
}
