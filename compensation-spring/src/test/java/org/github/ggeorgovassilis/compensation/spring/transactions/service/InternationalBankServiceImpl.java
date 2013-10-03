package org.github.ggeorgovassilis.compensation.spring.transactions.service;

import javax.annotation.Resource;

import org.github.ggeorgovassilis.compensation.spring.transactions.AccountStatement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation that delegates to one or more {@link BankService}
 * implementations. It uses local transactions where available and compensation
 * for non-transactional services
 * 
 * @author g.georgovassilis@gmail.com
 * 
 */
@Service("InternationalBankService")
@Transactional
public class InternationalBankServiceImpl implements BankService {

	@Resource(name = "LocalBankService")
	private BankService localBankService;

	@Resource(name = "RemoteBankService")
	private BankService remoteBankService;

	private BankService getServiceFor(String accountNumber) {
		if (localBankService.isManagedByThis(accountNumber))
			return localBankService;
		if (remoteBankService.isManagedByThis(accountNumber))
			return remoteBankService;
		throw new IllegalArgumentException("No service manages account number "
				+ accountNumber);
	}

	@Override
	public boolean isManagedByThis(String accountNumber) {
		return getServiceFor(accountNumber).isManagedByThis(accountNumber);
	}

	@Override
	public AccountStatement queryBalance(String accountNumber) {
		return getServiceFor(accountNumber).queryBalance(accountNumber);
	}

	@Override
	public AccountStatement withdraw(String accountNumber, int amount) {
		return getServiceFor(accountNumber).withdraw(accountNumber, amount);
	}

	@Override
	public AccountStatement deposit(String accountNumber, int amount) {
		return getServiceFor(accountNumber).deposit(accountNumber, amount);
	}

	@Override
	public AccountStatement move(String accountNumberFrom,
			String accountNumberTo, int amount) {
		BankService bankFrom = getServiceFor(accountNumberFrom);
		BankService bankTo = getServiceFor(accountNumberTo);
		if (bankFrom == bankTo)
			return bankFrom.move(accountNumberFrom, accountNumberTo, amount);
		AccountStatement statementFrom = bankFrom.withdraw(accountNumberFrom, amount);
		bankTo.deposit(accountNumberTo, amount);
		return statementFrom;
	}

	@Override
	public AccountStatement createNewAccount() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void deleteAccount(String accountNumber) {
		getServiceFor(accountNumber).deleteAccount(accountNumber);
	}

}
