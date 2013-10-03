package org.github.ggeorgovassilis.compensation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.github.ggeorgovassilis.compensation.api.CompensationManager;
import org.github.ggeorgovassilis.compensation.api.Transaction;
import org.github.ggeorgovassilis.compensation.impl.CompensationManagerImpl;
import org.github.ggeorgovassilis.compensation.onlineshop.model.BankAccountReport;
import org.github.ggeorgovassilis.compensation.onlineshop.services.BankServiceImpl;
import org.github.ggeorgovassilis.compensation.onlineshop.services.BankingCompensationAdvice;
import org.github.ggeorgovassilis.compensation.onlineshop.services.IBankService;
import org.junit.Before;
import org.junit.Test;

public class TestBigCompensation {

	CompensationManager cm;
	final int NUMBER_OF_ACCOUNTS = 100;
	final int NUMBER_OF_BANKS = 5;
	final IBankService[] bankServices = new IBankService[NUMBER_OF_BANKS];

	@Before
	public void setup() {
		cm = new CompensationManagerImpl();
		for (int i = 0; i < NUMBER_OF_BANKS; i++) {
			IBankService service = new BankServiceImpl();
			BankingCompensationAdvice advice = new BankingCompensationAdvice(
					service, cm);
			IBankService proxy = cm.createProxyFor(service,
					new Class[] { IBankService.class }, advice);
			bankServices[i] = proxy;
		}
	}

	@Test
	public void test() {
		// 1. setup some accounts at different banks and distribute a random
		// amount between them
		Transaction tx = cm.startNewAndActivate();
		Random random = new Random();
		for (int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
			bankServices[i % NUMBER_OF_BANKS].createBankAccount(i);
			int amount = random.nextInt(50);
			bankServices[i % NUMBER_OF_BANKS].addAmount(i, amount);
		}

		// 2. remember the original state
		List<BankAccountReport> reports = new ArrayList<BankAccountReport>();
		for (int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
			BankAccountReport report = bankServices[i % NUMBER_OF_BANKS]
					.getReportForAccount(i);
			reports.add(report);
		}
		// 3. commit
		cm.commit(tx);
		tx = cm.startNewAndActivate();

		// 4. shuffle accounts around
		for (int i = 0; i < 1000; i++) {
			int accountNr1 = random.nextInt(NUMBER_OF_ACCOUNTS);
			int bankService1 = accountNr1 % NUMBER_OF_BANKS;
			int accountNr2 = random.nextInt(NUMBER_OF_ACCOUNTS);
			int bankService2 = accountNr2 % NUMBER_OF_BANKS;
			int amount = random.nextInt(50);
			bankServices[bankService1].addAmount(accountNr1, -amount);
			bankServices[bankService2].addAmount(accountNr2, amount);
		}

		// 5. rollback
		cm.rollback(tx);
		tx = cm.startNewAndActivate();

		// 6. check that rollback left the system exactly as in 3
		for (int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
			BankAccountReport report = bankServices[i % NUMBER_OF_BANKS]
					.getReportForAccount(i);
			BankAccountReport oldReport = reports.get(i);
			assertEquals(oldReport.getAccountNumber(),
					report.getAccountNumber());
			assertEquals(oldReport.getBalance(), report.getBalance());
		}

	}

}
