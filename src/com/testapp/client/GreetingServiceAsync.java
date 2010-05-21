package com.testapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void login(String requestUri, AsyncCallback<LoginInfo> async);
	void getRecords(AsyncCallback<List<EntryRecord>> async);
}
