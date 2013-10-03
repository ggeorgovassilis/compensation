package org.github.ggeorgovassilis.compensation.spring.transactions.service;

import org.github.ggeorgovassilis.compensation.spring.transactions.AccountStatement;

public interface BankService {

	boolean isManagedByThis(String accountNumber);
	AccountStatement queryBalance(String accountNumber);
	AccountStatement withdraw(String accountNumber, int amount);
	AccountStatement deposit(String accountNumber, int amount);
	AccountStatement move(String accountNumberFrom, String accountNumberTo, int amount);
	AccountStatement createNewAccount();
	void deleteAccount(String accountNumber);
}
