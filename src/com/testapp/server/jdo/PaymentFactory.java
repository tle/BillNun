package com.testapp.server.jdo;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.testapp.client.api.FriendshipAPI;
import com.testapp.client.dto.Payment;
import com.testapp.client.dto.UserAccountDto;
import com.testapp.server.FriendshipImpl;

public class PaymentFactory extends PersistentObjectFactory<Payment> {

	private static FriendshipAPI friendAPI = 
		FriendshipImpl.newInstance();
	
	@Override
	protected Class<Payment> getObjectClass() {
		return Payment.class;
	}
	
	/**
	 * Considering we are passing in a list of UserAccountDto, we can assume that they are registerd
	 * @param whoPayed
	 * @param participants
	 * @param amount
	 * @param transactionDate
	 * @param description
	 */
	public void recordPayment (List<UserAccountDto> whoPayed , List<UserAccountDto> participants ,
			double amount , Date transactionDate , String description) {
		
		//TODO we really need to put shit in transaction 
		//TODO for now, we will just divided the balance evenly amonng the payers, in the future
		//i imagine we would pass in some sort of spec that tells how much each participant owns each payer
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			//record the payment
			Payment payment = new Payment();
			payment.setDescription(description);
			payment.setAmount(amount);
			payment.setWhoParticipated(extractId(participants));
			payment.setWhoPayed(extractId(whoPayed));
			payment.setDate(transactionDate);
			
			//update balances between friends
			for (UserAccountDto payer : whoPayed) {
				for (UserAccountDto payee : participants) {
					if (!payer.getKey().equals(payee.getKey())) {
						FriendFactory.getInstance().updateBalance(
								payer.getKey(), payee.getKey(), amount);
					}
				}
			}
		}
		finally {
			pm.close();
		}
	}
	
	private List<Long> extractId(List<UserAccountDto> userAccountList) {
		List<Long> idList = Collections.EMPTY_LIST;
		for (UserAccountDto account : userAccountList) {
			idList.add(account.getKey());
		}
		return idList;
	}
}
