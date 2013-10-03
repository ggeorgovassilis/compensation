package org.github.ggeorgovassilis.compensation.spring.transactions.service;

import java.util.Date;

import org.github.ggeorgovassilis.compensation.spring.transactions.AccountStatement;
import org.github.ggeorgovassilis.compensation.spring.transactions.dao.AccountDao;
import org.github.ggeorgovassilis.compensation.spring.transactions.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("BankService")
public class BankServiceImpl implements BankService{

	@Autowired
	private AccountDao accountDao;
	
	private AccountStatement toStatement(Account account){
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
		Account account = accountDao.findOne(accountNumber);
		return toStatement(account);
	}

	@Override
	public AccountStatement withdraw(String accountNumber, int amount) {
		Account account = accountDao.findOne(accountNumber);
		account.setBalance(account.getBalance()-amount);
		accountDao.save(account);
		return toStatement(account);
	}

	@Override
	public AccountStatement deposit(String accountNumber, int amount) {
		Account account = accountDao.findOne(accountNumber);
		account.setBalance(account.getBalance()+amount);
		accountDao.save(account);
		return toStatement(account);
	}

	@Override
	public AccountStatement move(String accountNumberFrom, String accountNumberTo, int amount) {
		withdraw(accountNumberFrom, amount);
		deposit(accountNumberTo, amount);
		return queryBalance(accountNumberFrom);
	}

}
