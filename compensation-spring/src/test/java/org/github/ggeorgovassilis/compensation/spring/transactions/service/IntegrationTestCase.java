package org.github.ggeorgovassilis.compensation.spring.transactions.service;

import javax.annotation.Resource;

import org.github.ggeorgovassilis.compensation.api.CompensationManager;
import org.github.ggeorgovassilis.compensation.spring.transactions.AccountStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class IntegrationTestCase {

	@Resource(name = "LocalBankService")
	private BankService localService;

	@Resource(name = "RemoteBankService")
	private BankService remoteService;

	@Resource(name = "InternationalBankService")
	private BankService internationalService;
	@Autowired
	private CompensationManager compensationManager;

	public static AccountStatement localAccount;
	public static AccountStatement remoteAccount;

	public void testLocalAndRemoteService() {
		localAccount = localService.createNewAccount();
		remoteAccount = remoteService.createNewAccount();
		remoteAccount = internationalService.deposit(
				remoteAccount.getAccountNumber(), 100);

		internationalService.move(remoteAccount.getAccountNumber(),
				localAccount.getAccountNumber(), 30);
		throw new ExceptionOnPurpose();
	}
}
