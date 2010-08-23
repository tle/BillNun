package com.testapp.client.api;

import java.util.Date;
import java.util.List;

import com.testapp.client.pos.UserAccount;

public interface PaymentAPI {

	public void recordPayment(List<UserAccount> whoPayed , List<UserAccount> participants, double amount , Date transactionDate , String description);
}
