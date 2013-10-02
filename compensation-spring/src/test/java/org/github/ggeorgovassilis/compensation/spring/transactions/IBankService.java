package org.github.ggeorgovassilis.compensation.spring.transactions;

public interface IBankService {

	AccountStatement queryBalance(String accountNumber);
	AccountStatement withdraw(String accountNumber, int amount);
	AccountStatement deposit(String accountNumber, int amount);
	AccountStatement move(String accountNumberFrom, String accountNumberTo, int amount);
}
