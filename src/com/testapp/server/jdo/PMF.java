package com.testapp.server.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PMF {
	private static PersistenceManagerFactory pmfInstance = null;

	private PMF() {}

	public static PersistenceManagerFactory get() {
		if (pmfInstance == null) {
			pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		}
		return pmfInstance;
	}
}
