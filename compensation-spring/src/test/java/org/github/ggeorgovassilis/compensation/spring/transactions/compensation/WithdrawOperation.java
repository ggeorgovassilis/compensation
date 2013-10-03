package org.github.ggeorgovassilis.compensation.spring.transactions.compensation;

import org.github.ggeorgovassilis.compensation.api.Operation;
import org.github.ggeorgovassilis.compensation.spring.transactions.service.BankService;

public class WithdrawOperation extends AbstractBankOperation {

	private final String accountNumber;
	private final int amount;
	private final BankService service;

	public WithdrawOperation(String accountNumber, int amount,
			BankService service, BankServiceCompensationAdvice advice) {
		super(advice);
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.service = service;
	}

	@Override
	public void undo() {
		service.deposit(accountNumber, amount);
	}

}
