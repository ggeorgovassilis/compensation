package org.github.ggeorgovassilis.compensation.spring.transactions.compensation;

import org.github.ggeorgovassilis.compensation.api.CompensationAdvice;
import org.github.ggeorgovassilis.compensation.api.CompensationManager;
import org.github.ggeorgovassilis.compensation.api.Operation;
import org.github.ggeorgovassilis.compensation.spring.transactions.AccountStatement;
import org.github.ggeorgovassilis.compensation.spring.transactions.service.BankService;

public class BankServiceCompensationAdvice implements BankService, CompensationAdvice<BankService>{

	private CompensationManager cm;
	private BankService target;
	
	@Override
	public BankService getTarget(){
		return target;
	}
	
	@Override
	public void setTarget(BankService target){
		this.target = target;
	}
	
	public BankServiceCompensationAdvice(){
	}
	
	public BankServiceCompensationAdvice(CompensationManager cm){
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
		return getTarget().isManagedByThis(accountNumber);
	}

	@Override
	public AccountStatement queryBalance(String accountNumber) {
		return getTarget().queryBalance(accountNumber);
	}

	@Override
	public AccountStatement withdraw(String accountNumber, int amount) {
		WithdrawOperation op = new WithdrawOperation(accountNumber, amount, getTarget(), this);
		cm.registerOperation(op, cm.getActiveTransaction());
		return getTarget().withdraw(accountNumber, amount);
	}

	@Override
	public AccountStatement deposit(String accountNumber, int amount) {
		DepositOperation op = new DepositOperation(accountNumber, amount, getTarget(), this);
		cm.registerOperation(op, cm.getActiveTransaction());
		return getTarget().deposit(accountNumber, amount);
	}

	@Override
	public AccountStatement move(String accountNumberFrom,
			String accountNumberTo, int amount) {
		MoveOperation op = new MoveOperation(accountNumberFrom, accountNumberTo, amount, getTarget(), this);
		cm.registerOperation(op, cm.getActiveTransaction());
		return getTarget().move(accountNumberFrom, accountNumberTo, amount);
	}

	@Override
	public AccountStatement createNewAccount() {
		AccountStatement stmt = getTarget().createNewAccount();
		CreateOperation op = new CreateOperation(stmt.getAccountNumber(), getTarget(), this);
		cm.registerOperation(op, cm.getActiveTransaction());
		return stmt;
	}
	
	@Override
	public void deleteAccount(String accountNumber) {
		//cannot be undone
		getTarget().deleteAccount(accountNumber);
	}

	public void setCompensationManager(CompensationManager manager) {
		this.cm = manager;
	}

}
