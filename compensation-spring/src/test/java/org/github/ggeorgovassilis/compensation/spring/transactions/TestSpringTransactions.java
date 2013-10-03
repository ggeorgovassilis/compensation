package org.github.ggeorgovassilis.compensation.spring.transactions;

import javax.annotation.Resource;

import org.github.ggeorgovassilis.compensation.api.CompensationManager;
import org.github.ggeorgovassilis.compensation.spring.transactions.compensation.BankServiceCompensationAdvice;
import org.github.ggeorgovassilis.compensation.spring.transactions.service.BankService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
@TransactionConfiguration(defaultRollback = true)
public class TestSpringTransactions {

	@Resource(name = "LocalBankService")
	private BankService localService;

	@Resource(name = "RemoteBankService")
	private BankService remoteService;

	@Resource(name = "InternationalBankService")
	private BankService internationalService;

	@Autowired
	private CompensationManager compensationManager;

	@Test
	@Transactional
	public void testLocalBankService() {
		AccountStatement localAccount = localService.createNewAccount();
		assertNotNull(localAccount.getAccountNumber());
		localAccount = localService.deposit(localAccount.getAccountNumber(),
				100);
		assertEquals(100, localAccount.getBalance());

		localAccount = localService
				.deposit(localAccount.getAccountNumber(), 10);
		assertEquals(110, localAccount.getBalance());

		localAccount = localService
				.withdraw(localAccount.getAccountNumber(), 9);
		assertEquals(101, localAccount.getBalance());

		localAccount = localService.queryBalance(localAccount
				.getAccountNumber());
		assertEquals(101, localAccount.getBalance());

	}

	@Test
	@Transactional
	public void testRemoteBankService() {
		AccountStatement remoteAccount = remoteService.createNewAccount();
		remoteAccount = remoteService.deposit(remoteAccount.getAccountNumber(),
				100);
		assertEquals(100, remoteAccount.getBalance());

		remoteAccount = remoteService.deposit(remoteAccount.getAccountNumber(),
				10);
		assertEquals(110, remoteAccount.getBalance());

		remoteAccount = remoteService.withdraw(
				remoteAccount.getAccountNumber(), 9);
		assertEquals(101, remoteAccount.getBalance());

		remoteAccount = remoteService.queryBalance(remoteAccount
				.getAccountNumber());
		assertEquals(101, remoteAccount.getBalance());
	}

	private void setupCompensation() {
		internationalService = compensationManager.createProxyFor(
				internationalService, new Class[] { BankService.class },
				new BankServiceCompensationAdvice(internationalService,
						compensationManager));
	}

	@Test
	@Transactional
	public void testInternationalBankService() {
		setupCompensation();
		compensationManager.startNewAndActivate();
		
		AccountStatement localAccount = localService.createNewAccount();
		AccountStatement remoteAccount = remoteService.createNewAccount();
		remoteAccount = internationalService.deposit(
				remoteAccount.getAccountNumber(), 100);
		assertEquals(100, remoteAccount.getBalance());

		internationalService.move(remoteAccount.getAccountNumber(),
				localAccount.getAccountNumber(), 30);
		localAccount = internationalService.queryBalance(localAccount
				.getAccountNumber());
		assertEquals(30, localAccount.getBalance());

		remoteAccount = internationalService.queryBalance(remoteAccount
				.getAccountNumber());
		assertEquals(70, remoteAccount.getBalance());
		
		compensationManager.rollback(compensationManager.getActiveTransaction());
		
		localAccount = localService.queryBalance(localAccount.getAccountNumber());
		assertEquals(0, localAccount.getBalance());

		remoteAccount = remoteService.queryBalance(remoteAccount.getAccountNumber());
		assertEquals(0, remoteAccount.getBalance());

	}

}
