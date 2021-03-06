package com.testapp.client.ui;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.testapp.client.LoginInfo;
import com.testapp.client.api.GreetingService;
import com.testapp.client.api.GreetingServiceAsync;
import com.testapp.client.dto.UserAccountDto;

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
	private HashMap<Long, CheckBox> friendshipMap = new HashMap<Long, CheckBox>();
	
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
		
		// User registration data
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
		phoneNumber.setText(loginInfo.getAccount().getPhoneNumber());
		mainPanel.add(phonePanel);

		HorizontalPanel userPanel =
			new HorizontalPanel();
		userPanel.add(new Label("User name :"));
		userPanel.add(userName);
		userName.setText(loginInfo.getAccount().getUserName());
		mainPanel.add(userPanel);

		mainPanel.add(createUserAccount);

		createUserAccount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				updateAccount();
			}
		});

		
		// add friend widget
		HorizontalPanel addFriendPanel = 
			new HorizontalPanel();
		final TextBox friendName = new TextBox();
		addFriendPanel.add(friendName);
		Button addFriend = new Button("Add as a friend");

		addFriend.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				greetingService.addFriend(friendName.getText(), new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {};
					public void onSuccess(Void v) {
						refreshFriendStatus();
					}
				});
			}
		});
		addFriendPanel.add(addFriend);
		mainPanel.add(addFriendPanel);
		
		//sign out link
		Anchor signoutLink = new Anchor("Sign out ," +loginInfo.getEmailAddress());

		signoutLink.setHref(loginInfo.getLogoutUrl());
		mainPanel.add(signoutLink);
		
		mainPanel.add(getFriendsPanel());
		RootPanel.get("nameList").add(mainPanel);
	}

	protected void updateAccount() {
//		final UserAccount account = loginInfo.getAccount();
//		account.setEmail(email.getText());
//		account.setPhoneNumber(phoneNumber.getText());
//		account.setUserName(userName.getText());
//
//		if (UserAccountDto.Status.PENDING.equals(account.getStatus())) {
//			account.setStatus(UserAccountDto.Status.ACCEPTED);
//		}
//
//		greetingService.updateUserAccount(account, new AsyncCallback<Void>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//
//			}
//			@Override
//			public void onSuccess(Void result) {
//				Window.alert("Account created.");
//
//			}
//		});
	}

	private FlowPanel getFriendsPanel() {
		final FlowPanel testPanel = new FlowPanel();
		final FlexTable allUsersTable = new FlexTable();
		testPanel.add(allUsersTable);
		// Get all the users in the system
		greetingService.getUserAccounts(new AsyncCallback<List<UserAccountDto>>() {
			@Override
			public void onFailure(Throwable caught) {}
			
			public void onSuccess(List<UserAccountDto> result) {
				int currentRow = 0;
				for (final UserAccountDto account: result) {
					int currentColumn = 0;
					
					allUsersTable.setText(currentRow, currentColumn++, "" + account.getKey());
					allUsersTable.setText(currentRow, currentColumn++, account.getUserName());
					allUsersTable.setText(currentRow, currentColumn++, account.getPhoneNumber());
					allUsersTable.setText(currentRow, currentColumn++, account.getEmail());
					
					CheckBox isFriend = new CheckBox("Friend?");
					allUsersTable.setWidget(currentRow, currentColumn++, isFriend);
					
					friendshipMap.put(account.getKey(), isFriend);
					
					currentRow++;
//					Button addFriend = new Button("Become Friends with: " + account.getUserName() + " (" + account.getEmail() + ")");
//					testPanel.add(addFriend);
//					addFriend.addClickHandler(new ClickHandler() {
//						@Override
//						public void onClick(ClickEvent event) {
//							greetingService.addFriend(account.getEmail(), new AsyncCallback<Void>() {
//								public void onFailure(Throwable caught) {};
//								public void onSuccess(Void v) {
//									rebuildFriendsPanel();
//								}
//							});
//						}
//					});
				}
				
				
			};
		});
		
		refreshFriendStatus();
		//rebuildFriendsPanel();
		//testPanel.add(friendsList);
		
		return testPanel;
	}
	
	private void refreshFriendStatus() {
		greetingService.getFriends(new AsyncCallback<List<UserAccountDto>>() {
			@Override
			public void onFailure(Throwable caught) {}
			
			public void onSuccess(List<UserAccountDto> result) {
				for(UserAccountDto account: result) {
					CheckBox checkbox = friendshipMap.get(account.getKey());
					checkbox.setValue(true);
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
