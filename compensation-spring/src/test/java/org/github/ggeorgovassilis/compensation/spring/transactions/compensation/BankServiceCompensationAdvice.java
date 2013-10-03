package org.github.ggeorgovassilis.compensation.spring.transactions.compensation;

import org.github.ggeorgovassilis.compensation.api.CompensationAdvice;
import org.github.ggeorgovassilis.compensation.api.CompensationManager;
import org.github.ggeorgovassilis.compensation.api.Operation;
import org.github.ggeorgovassilis.compensation.spring.transactions.AccountStatement;
import org.github.ggeorgovassilis.compensation.spring.transactions.service.BankService;

public class BankServiceCompensationAdvice implements BankService, CompensationAdvice{

	private BankService target;
	private CompensationManager cm;
	
	public BankServiceCompensationAdvice(BankService target, CompensationManager cm){
		this.target = target;
		this.cm = cm;
	}
	
	@Override
	public void commit(Operation operation) {
	}

	@Override
	public void rollback(Operation operation) {
		((AbstractBankOperation)operation).undo();
	}

	@Override
	public boolean isManagedByThis(String accountNumber) {
		return target.isManagedByThis(accountNumber);
	}

	@Override
	public AccountStatement queryBalance(String accountNumber) {
		return target.queryBalance(accountNumber);
	}

	@Override
	public AccountStatement withdraw(String accountNumber, int amount) {
		WithdrawOperation op = new WithdrawOperation(accountNumber, amount, target, this);
		cm.registerOperation(op, cm.getActiveTransaction());
		return target.withdraw(accountNumber, amount);
	}

	@Override
	public AccountStatement deposit(String accountNumber, int amount) {
		DepositOperation op = new DepositOperation(accountNumber, amount, target, this);
		cm.registerOperation(op, cm.getActiveTransaction());
		return target.deposit(accountNumber, amount);
	}

	@Override
	public AccountStatement move(String accountNumberFrom,
			String accountNumberTo, int amount) {
		MoveOperation op = new MoveOperation(accountNumberFrom, accountNumberTo, amount, target, this);
		cm.registerOperation(op, cm.getActiveTransaction());
		return target.move(accountNumberFrom, accountNumberTo, amount);
	}

	@Override
	public AccountStatement createNewAccount() {
		AccountStatement stmt = target.createNewAccount();
		CreateOperation op = new CreateOperation(stmt.getAccountNumber(), target, this);
		cm.registerOperation(op, cm.getActiveTransaction());
		return stmt;
	}
	
	@Override
	public void deleteAccount(String accountNumber) {
		//cannot be undone
		target.deleteAccount(accountNumber);
	}

}
