package com.testapp.server.rest;

import com.testapp.client.pos.UserAccount;
import com.testapp.server.jdo.FriendFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class HelloWorldResource {

    static private List<UserAccount> fakeUsers;

    static {
        fakeUsers = new ArrayList<UserAccount>();
        fakeUsers.add(new UserAccount("bryanrcampbell@gmail.com","512-473-9310","bcampbell", UserAccount.UserAccountStatus.ACCEPTED));
        fakeUsers.add(new UserAccount("trobertson@gmail.com","555-555-5555","trob", UserAccount.UserAccountStatus.PENDING));
        fakeUsers.add(new UserAccount("tle@lombardi.com","555-567-8897","tle", UserAccount.UserAccountStatus.ACCEPTED));
        fakeUsers.add(new UserAccount("cwalters@gmail.com","555-555-5555","cwalters", UserAccount.UserAccountStatus.PENDING));
    }
    
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/hello")
    public String getEmployee() {
        return "Hello World!";
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/friends")
    public List<UserAccount> getJson() {
        return fakeUsers;
    }

}