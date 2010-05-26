package com.testapp.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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
			//TODO ask for user info
		} else {
			Window.alert("WELCOME back ,"+loginInfo.getAccount().getUserName() +" !!! " );
		}
		
		
		
		Anchor signoutLink = new Anchor("Sign out ," +loginInfo.getEmailAddress());

		signoutLink.setHref(loginInfo.getLogoutUrl());
		RootPanel.get("nameList").add(signoutLink);
	}

	protected void loadLogin() {
		signinLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signinLink);
		RootPanel.get("nameList").add(loginPanel);
	}
}
