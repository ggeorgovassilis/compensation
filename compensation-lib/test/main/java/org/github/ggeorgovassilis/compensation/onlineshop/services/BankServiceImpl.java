package org.github.ggeorgovassilis.compensation.onlineshop.services;

import java.util.HashMap;
import java.util.Map;

import org.github.ggeorgovassilis.compensation.onlineshop.model.BankAccountReport;

public class BankServiceImpl implements IBankService {

    private Map<Integer, Integer> accountNr2Balance = new HashMap<Integer, Integer>();
    private IClock clock = new ClockImpl();

    @Override
    public BankAccountReport getReportForAccount(int accountNumber) {
	Integer balance = accountNr2Balance.get(accountNumber);
	if (balance == null)
	    return null;
	BankAccountReport report = new BankAccountReport();
	report.setAccountNumber(accountNumber);
	report.setBalance(balance);
	report.setDate(clock.getTime("getReportForAccount"));
	return report;
    }

    @Override
    public BankAccountReport createBankAccount(int accountNumber) {
	if (getReportForAccount(accountNumber)!=null)
	    throw new ServiceException("Account "+accountNumber+" exists");
	accountNr2Balance.put(accountNumber, 0);
	return getReportForAccount(accountNumber);
    }

    @Override
    public BankAccountReport deleteBankAccount(int accountNumber) {
	if (getReportForAccount(accountNumber)==null)
	    throw new ServiceException("Account "+accountNumber+" does not exists");
	accountNr2Balance.remove(accountNumber);
	return getReportForAccount(accountNumber);
    }

    @Override
    public BankAccountReport addAmount(int accountNumber, int amount) {
	if (getReportForAccount(accountNumber)==null)
	    throw new ServiceException("Account "+accountNumber+" does not exists");
	int balance = accountNr2Balance.get(accountNumber);
	balance+=amount;
	accountNr2Balance.put(accountNumber, balance);
	return getReportForAccount(accountNumber);
    }

}
