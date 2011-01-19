package com.testapp.client.api;

import java.util.Date;
import java.util.List;

import com.testapp.client.dto.UserAccountDto;

public interface PaymentAPI {

	public void recordPayment(List<UserAccountDto> whoPayed , List<UserAccountDto> participants, double amount , Date transactionDate , String description);
}
