package org.github.ggeorgovassilis.compensation.onlineshop.services;

import org.github.ggeorgovassilis.compensation.api.Operation;

public class BankOperation extends Operation {

	public enum OPCODE {
		modify, create, delete
	};

	private final int accountNumber;
	private final int amount;
	private final IBankService service;
	private final OPCODE opcode;

	public BankOperation(int accountNumber, int amount, IBankService service,
			BankingCompensationAdvice advice, OPCODE opcode) {
		super(advice);
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.service = service;
		this.opcode = opcode;
	}

	public void undo() {
		if (opcode == OPCODE.create) {
			service.deleteBankAccount(accountNumber);
		}
		if (opcode == OPCODE.delete) {
			service.createBankAccount(accountNumber);
			service.addAmount(accountNumber, amount);
		}
		if (opcode == OPCODE.modify) {
			service.addAmount(accountNumber, -amount);
		}
	}

}
