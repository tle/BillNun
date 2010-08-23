package com.testapp.server.jdo;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;


public abstract class PersistentObjectFactory<T> {
	
	protected abstract Class<T> getObjectClass();
	
	/**
	 * Get the object with the given id
	 * 
	 * @param persistenObject
	 * @return
	 */
	public T get(long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			T po = pm.getObjectById(getObjectClass(), id);
			return pm.detachCopy(po);
		} catch(JDOObjectNotFoundException notFound) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	/**
	 * Save an object to the data store
	 * 
	 * @param persistenObject
	 * @return
	 */
	public T save(T po) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		
		try{
			tx.begin();
			T detachablePO = PMF.get().getPersistenceManager().makePersistent(po);
			if(po == null) {
				return null;
			}
			tx.commit();
			return detachablePO;
		}
		finally {
			if (tx.isActive()) {
                tx.rollback();
            }
			pm.close();
		}
	}
	
	/**
	 * Delete an object from the data store
	 * 
	 * @param persistenObject
	 * @return
	 */
	public void delete(long id) {
		T po = this.get(id);
		
		if(po != null) {
			this.delete(po);
		}
	}
	
	/**
	 * 
	 * @param po
	 */
	public void delete(T po) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			PMF.get().getPersistenceManager().deletePersistent(po);
		}
		finally {
			pm.close();
		}
		
		
	}
	
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
}
