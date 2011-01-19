package com.testapp.server.rest;

import com.testapp.client.dto.UserAccountDto;
import com.testapp.server.po.UserAccount;
import com.testapp.server.service.UserGroupService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class HelloWorldResource {

    static private List<UserAccountDto> fakeUsers;

    static {
        fakeUsers = new ArrayList<UserAccountDto>();
        fakeUsers.add(new UserAccountDto("bryanrcampbell@gmail.com","512-473-9310","bcampbell", UserAccountDto.Status.ACCEPTED));
        fakeUsers.add(new UserAccountDto("trobertson@gmail.com","555-555-5555","trob", UserAccountDto.Status.PENDING));
        fakeUsers.add(new UserAccountDto("tle@lombardi.com","555-567-8897","tle", UserAccountDto.Status.ACCEPTED));
        fakeUsers.add(new UserAccountDto("cwalters@gmail.com","555-555-5555","cwalters", UserAccountDto.Status.PENDING));
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
    public List<UserAccountDto> getJson() {
        return fakeUsers;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/users")
    public UserAccountDto createUserAccount(UserAccountDto userAccount) {
        UserAccount account = new UserAccount(userAccount.getEmail(),
                userAccount.getPhoneNumber(),userAccount.getUserName(), UserAccount.Status.ACCEPTED);

        account = UserGroupService.getInstance().createUser(account);

        UserAccountDto newUser = new UserAccountDto(account.getEmail(),
                account.getPhoneNumber(), account.getUserName(), UserAccountDto.Status.ACCEPTED);
        
        return newUser;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/users")
    public List<UserAccountDto> getUserAccounts() {
        List<UserAccount> ua = UserGroupService.getInstance().getUsers();
        List<UserAccountDto> dtos = new ArrayList<UserAccountDto>();

        for(UserAccount account: ua) {
            dtos.add(new UserAccountDto(account.getEmail(), account.getPhoneNumber(), account.getUserName(), UserAccountDto.Status.ACCEPTED));
        }

        return dtos;
    }
}