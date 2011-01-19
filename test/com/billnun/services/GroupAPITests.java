package com.billnun.services;

import java.util.Collection;
import java.util.Collections;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.testapp.client.dto.UserAccountDto;
import com.testapp.client.dto.UserGroup;
import com.testapp.server.jdo.GroupFactory;
import com.testapp.server.jdo.UserAccountFactory;

import com.testapp.server.po.UserAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GroupAPITests {

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

    private void doTest() {
    	UserGroup g = createTestGroup();
        System.out.println(g.toString());
    }

    @Test
    public void testInsert1() {
        doTest();
    }

    @Test
    public void testInsert2() {
        doTest();
    }
    
    @Test
    public void testGet1() {
    	Collection<UserGroup> g = GroupFactory.getInstance().getAll();
    	for(UserGroup gg: g) {
    		System.out.println(gg.toString());
    	}
    }
    
    public UserGroup createTestGroup() {
    	UserGroup group = new UserGroup();
    	group.setName("Group_" + System.currentTimeMillis());
    	
    	for(int i=0;i<5;i++) {
    		String name = "User_" + System.currentTimeMillis();
    		UserAccount u = new UserAccount(name + "@billnun.test.com", "555-555-5555", name, UserAccount.Status.ACCEPTED);
    		UserAccountFactory.getInstance().save(u);
    		group.addMembers(Collections.singleton(u.getKey()));
    	}
    	return GroupFactory.getInstance().save(group);
    }
}