package org.github.ggeorgovassilis.compensation.spring.transactions.service;

import org.github.ggeorgovassilis.compensation.spring.transactions.AccountStatement;

public interface BankService {

	AccountStatement queryBalance(String accountNumber);
	AccountStatement withdraw(String accountNumber, int amount);
	AccountStatement deposit(String accountNumber, int amount);
	AccountStatement move(String accountNumberFrom, String accountNumberTo, int amount);
}
