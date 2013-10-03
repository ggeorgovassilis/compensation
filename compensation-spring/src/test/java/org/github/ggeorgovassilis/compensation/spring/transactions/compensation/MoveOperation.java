package org.github.ggeorgovassilis.compensation.spring.transactions.compensation;

import org.github.ggeorgovassilis.compensation.api.CompensationAdvice;
import org.github.ggeorgovassilis.compensation.api.Operation;
import org.github.ggeorgovassilis.compensation.spring.transactions.service.BankService;

public class MoveOperation extends AbstractBankOperation{

	private String accountFrom;
	private String accountTo;
	private int amount;
	private BankService service;
	
	public MoveOperation(String accountFrom, String accountTo, int amount, BankService service, CompensationAdvice advice){
		super(advice);
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
		this.service = service;
	}
	
	@Override
	public void undo(){
		service.move(accountTo, accountFrom, amount);
	}
}
