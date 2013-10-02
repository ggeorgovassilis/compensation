package org.github.ggeorgovassilis.compensation.onlineshop.services;

import org.github.ggeorgovassilis.compensation.api.CompensationAdvice;
import org.github.ggeorgovassilis.compensation.api.CompensationManager;
import org.github.ggeorgovassilis.compensation.api.Operation;
import org.github.ggeorgovassilis.compensation.onlineshop.model.BankAccountReport;
import org.github.ggeorgovassilis.compensation.onlineshop.services.BankOperation.OPCODE;

public class BankingCompensationAdvice implements IBankService, CompensationAdvice{

    IBankService target;
    CompensationManager cm;
    
    public BankingCompensationAdvice(IBankService target, CompensationManager cm) {
	this.target = target;
	this.cm = cm;
    }
    
    @Override
    public BankAccountReport getReportForAccount(int accountNumber) {
	return target.getReportForAccount(accountNumber);
    }

    @Override
    public BankAccountReport addAmount(int accountNumber, int amount) {
	BankAccountReport report = target.addAmount(accountNumber, amount);
	cm.registerOperation(new BankOperation(accountNumber, amount, target, this, OPCODE.modify), cm.getActiveTransaction());
	return report;
    }

    @Override
    public BankAccountReport createBankAccount(int accountNumber) {
	BankAccountReport report = target.createBankAccount(accountNumber);
	cm.registerOperation(new BankOperation(accountNumber, report.getBalance(), target, this, OPCODE.create), cm.getActiveTransaction());
	return report;
    }

    @Override
    public BankAccountReport deleteBankAccount(int accountNumber) {
	BankAccountReport report = target.deleteBankAccount(accountNumber);
	cm.registerOperation(new BankOperation(accountNumber, report.getBalance(), target, this, OPCODE.delete), cm.getActiveTransaction());
	return report;
    }

    @Override
    public void commit(Operation operation) {
    }

    @Override
    public void rollback(Operation operation) {
	BankOperation op = (BankOperation)operation;
	op.undo();
    }

}
