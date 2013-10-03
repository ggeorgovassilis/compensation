package org.github.ggeorgovassilis.compensation.spring.transactions.service;

import java.util.Date;

import org.github.ggeorgovassilis.compensation.spring.transactions.AccountStatement;
import org.github.ggeorgovassilis.compensation.spring.transactions.dao.AccountDao;
import org.github.ggeorgovassilis.compensation.spring.transactions.model.Account;

/**
 * Base functions for bank service implementations
 * @author g.georgovassilis@gmail.com
 *
 */
public abstract class AbstractBankServiceImpl implements BankService{

	private int counter=0;
	private String accountNumberPrefix;
	
	protected abstract AccountDao getDaoFor(String accountNumber);

	protected AbstractBankServiceImpl(String accountNumberPrefix){
		this.accountNumberPrefix = accountNumberPrefix;
	}
	
	protected String getNextAccountId(){
		return accountNumberPrefix+(counter++);
	}

	protected AccountStatement toStatement(Account account){
		if (account == null)
			return null;
		AccountStatement statement = new AccountStatement();
		statement.setAccountNumber(account.getId());
		statement.setBalance(account.getBalance());
		statement.setDate(new Date());
		return statement;
	}

	@Override
	public AccountStatement queryBalance(String accountNumber) {
		AccountDao dao = getDaoFor(accountNumber);
		Account account = dao.findOne(accountNumber);
		return toStatement(account);
	}

	@Override
	public AccountStatement withdraw(String accountNumber, int amount) {
		AccountDao dao = getDaoFor(accountNumber);
		Account account = dao.findOne(accountNumber);
		if (account.getBalance()<amount)
			throw new IllegalArgumentException("Not performing withdrawal which would result in negative balance on account "+accountNumber);
		if (amount<0)
			throw new IllegalArgumentException("Not performing negative withdrawal on account "+accountNumber);
		account.setBalance(account.getBalance()-amount);
		account = dao.save(account);
		return toStatement(account);
	}

	@Override
	public AccountStatement deposit(String accountNumber, int amount) {
		AccountDao dao = getDaoFor(accountNumber);
		Account account = dao.findOne(accountNumber);
		if (amount<0)
			throw new IllegalArgumentException("Not performing negative deposit on account "+accountNumber);
		account.setBalance(account.getBalance()+amount);
		account = dao.save(account);
		return toStatement(account);
	}

	@Override
	public AccountStatement move(String accountNumberFrom, String accountNumberTo, int amount) {
		AccountStatement accountFrom = withdraw(accountNumberFrom, amount);
		deposit(accountNumberTo, amount);
		return accountFrom;
	}
	
	@Override
	public AccountStatement createNewAccount() {
		Account account = new Account();
		account.setId(getNextAccountId());
		account.setBalance(0);
		AccountDao dao = getDaoFor(account.getId());
		dao.save(account);
		return toStatement(account);
	}

	@Override
	public boolean isManagedByThis(String accountNumber) {
		return (accountNumber+"").startsWith(accountNumberPrefix);
	}
	
	@Override
	public void deleteAccount(String accountNumber) {
		getDaoFor(accountNumber).delete(accountNumber);
	}
	
}
