package com.testapp.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sample_gwt implements EntryPoint {
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	private static LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Sign in using your gmail account");
	private Anchor signinLink = new Anchor("Sign in");
	private VerticalPanel friendsList = new VerticalPanel();
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private TextBox email = new TextBox();
	private TextBox phoneNumber = new TextBox();
	private TextBox userName = new TextBox();
	Button createUserAccount = new Button("create");
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	/**
	 * Returns the current login info
	 * @return
	 */
	public static LoginInfo getCurrentLoginInfo() {
		return loginInfo;
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		greetingService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if (loginInfo.isLoggedIn()) {
					drawSomeStuff();
				} else {
					loadLogin();
				}
			}
		});
	}

	protected void drawSomeStuff() {		
		
		if (loginInfo.isNewUser()) {
			
			HorizontalPanel emailPanel =
				new HorizontalPanel();
			emailPanel.add(new Label("email :"));
			email.setText(loginInfo.getEmailAddress());
			email.setEnabled(false);
			emailPanel.add(email);
			mainPanel.add(emailPanel);
				
			HorizontalPanel phonePanel =
				new HorizontalPanel();
			phonePanel.add(new Label("Phone # :"));
			phonePanel.add(phoneNumber);
			mainPanel.add(phonePanel);
			
			HorizontalPanel userPanel =
				new HorizontalPanel();
			userPanel.add(new Label("User name :"));
			userPanel.add(userName);
			mainPanel.add(userPanel);
			
			mainPanel.add(createUserAccount);
			
			createUserAccount.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					updateAccount();
				}
			});
			
		} else {
			Window.alert("WELCOME back ,"+loginInfo.getAccount().getUserName() +" !!! \n" + 
				loginInfo.getAccount().getPhoneNumber() + "\n"+
				loginInfo.getAccount().getUserName()
				);
		}
		
		Anchor signoutLink = new Anchor("Sign out ," +loginInfo.getEmailAddress());

		signoutLink.setHref(loginInfo.getLogoutUrl());
		mainPanel.add(signoutLink);
		RootPanel.get("nameList").add(mainPanel);
	}

	protected void updateAccount() {
		final UserAccount account = loginInfo.getAccount();
		account.setEmail(email.getText());
		account.setPhoneNumber(phoneNumber.getText());
		account.setUserName(userName.getText());
		
		greetingService.updateUserAccount(account, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(Void result) {
				Window.alert("Account created.");
				
			}
		});
	}

	private FlowPanel getTestPanel() {
		final FlowPanel testPanel = new FlowPanel();
		
		//Test out Friend datastore
		greetingService.getAllObjects(UserAccount.class, new AsyncCallback<List<UserAccount>>() {
			@Override
			public void onFailure(Throwable caught) {}
			
			public void onSuccess(List<UserAccount> result) {
				for (final UserAccount account: result) {
					Button addFriend = new Button("Become Friends with: " + account.getUserName() + " (" + account.getEmail() + ")");
					testPanel.add(addFriend);
					addFriend.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							greetingService.addFriend(account.getEmail(), new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {};
								public void onSuccess(Void v) {}
							});
						}
					});
				}
			};
		});
		
		
		return testPanel;
	}
	
	private void rebuildFriendsPanel() {
		greetingService.getFriends(new AsyncCallback<List<Friend>>() {
			@Override
			public void onFailure(Throwable caught) {}
			
			public void onSuccess(List<Friend> result) {
				friendsList.clear();
				friendsList.add(new Label("-MY FRIENDS-"));
				for (final Friend friend: result) {
					friendsList.add(new Label("" + friend.getFriendUserId()));
				}
			};
		});
	}
	
	protected void loadLogin() {
		loginPanel.setStylePrimaryName("test-panel");
		
		signinLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signinLink);
		RootPanel.get("nameList").add(loginPanel);
	}
}
