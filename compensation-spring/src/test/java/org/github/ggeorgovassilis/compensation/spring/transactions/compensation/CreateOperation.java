package org.github.ggeorgovassilis.compensation.spring.transactions.compensation;

import org.github.ggeorgovassilis.compensation.api.Operation;
import org.github.ggeorgovassilis.compensation.spring.transactions.service.BankService;

public class CreateOperation extends AbstractBankOperation {

	private final String accountNumber;
	private final BankService service;

	public CreateOperation(String accountNumber, BankService service,
			BankServiceCompensationAdvice advice) {
		super(advice);
		this.accountNumber = accountNumber;
		this.service = service;
	}

	@Override
	public void undo() {
		service.deleteAccount(accountNumber);
	}

}
