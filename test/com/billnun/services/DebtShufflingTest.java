package com.billnun.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.testapp.client.pos.UserAccount;
import com.testapp.server.DebtShufflingCore;
import com.testapp.server.Graph;
import com.testapp.server.Graph.Edge;

public class DebtShufflingTest extends TestCase {
	                               
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
	
	@Test
	public void testNetworth() {
		UserAccount userA = TestHelperFactory.createUser() ;
		UserAccount userB = TestHelperFactory.createUser() ;
		UserAccount userC = TestHelperFactory.createUser() ;
		UserAccount userD = TestHelperFactory.createUser() ;
		UserAccount userE = TestHelperFactory.createUser() ;
		Edge edgeAB = DebtShufflingCore.createEdge(userA.getKey(), userB.getKey(), 30);
		Edge edgeDA = DebtShufflingCore.createEdge(userD.getKey(), userA.getKey(), 15);
		Edge edgeEB = DebtShufflingCore.createEdge(userE.getKey(), userB.getKey(), 20);
		Edge edgeDE = DebtShufflingCore.createEdge(userD.getKey(), userE.getKey(), 50);
		Edge edgeCD = DebtShufflingCore.createEdge(userC.getKey(), userD.getKey(), 25);
		
		Graph graph = new DebtShufflingCore.MinTransactionShuffleGraph();
		graph.addEdge(edgeCD);
		graph.addEdge(edgeDE);
		graph.addEdge(edgeEB);
		graph.addEdge(edgeDA);
		graph.addEdge(edgeAB);

        String str = "";


		
/*		Set<Map.Entry<Long, Integer>> networths = graph.getNetWorths();      
		
		assertValidNetworthsSet(networths);*/
		
		graph.shuffle();
		
	}
	
	private void assertValidNetworthsSet(Set<Map.Entry<Long, Integer>> networths) {
		int sum = 0;
		Set<Long> idSet = new HashSet<Long>();
		for (Map.Entry<Long, Integer> networth : networths) {
			System.out.println(networth);
			sum+= networth.getValue();
			if (idSet.contains(networth.getKey())) {
				fail("each id in a networth set is unique. " + networth.getKey().toString() + " appears at least twice in the set");
			}
		}
		assertEquals("the sum of all networths is equals to 0",0, sum);
	}
}
