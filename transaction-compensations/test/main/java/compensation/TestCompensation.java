package compensation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import compensation.api.CompensationManager;
import compensation.api.Transaction;
import compensation.impl.CompensationManagerImpl;
import compensation.onlineshop.model.BankAccountReport;
import compensation.onlineshop.services.BankServiceImpl;
import compensation.onlineshop.services.BankingCompensationAdvice;
import compensation.onlineshop.services.IBankService;

public class TestCompensation {

    final int accountNumber1 = 1;
    final int accountNumber2 = 2;
    
    IBankService bank1;
    IBankService bank2;
    CompensationManager cm;
    
    @Test
    public void testPlainServices() {
	IBankService service = new BankServiceImpl();
	BankAccountReport report = service.getReportForAccount(accountNumber1);
	assertNull(report);
	report = service.createBankAccount(accountNumber1);
	assertEquals(accountNumber1, report.getAccountNumber());
	assertEquals(0, report.getBalance());
	report = service.addAmount(accountNumber1, 100);
	assertEquals(accountNumber1, report.getAccountNumber());
	assertEquals(100, report.getBalance());
	report = service.addAmount(accountNumber1, -10);
	assertEquals(accountNumber1, report.getAccountNumber());
	assertEquals(90, report.getBalance());
	report = service.getReportForAccount(accountNumber1);
	assertEquals(accountNumber1, report.getAccountNumber());
	assertEquals(90, report.getBalance());
	service.deleteBankAccount(accountNumber1);
	report = service.getReportForAccount(accountNumber1);
	assertNull(report);
    }
    
    @Before
    public void setup() {
	cm = new CompensationManagerImpl();
	IBankService bankService1 = new BankServiceImpl();
	IBankService bankService2 = new BankServiceImpl();
	bank1 = (IBankService)cm.createProxyFor(bankService1, new Class[] {IBankService.class}, new BankingCompensationAdvice(bankService1, cm));
	bank2 = (IBankService)cm.createProxyFor(bankService2, new Class[] {IBankService.class}, new BankingCompensationAdvice(bankService2, cm));
    }
    
    @Test
    public void testCommit() {
	Transaction tx = cm.startNewAndActivate();
	bank1.createBankAccount(accountNumber1);
	bank2.createBankAccount(accountNumber2);
	bank1.addAmount(accountNumber1, 100);
	BankAccountReport report1 = bank1.getReportForAccount(accountNumber1);
	BankAccountReport report2 = bank2.getReportForAccount(accountNumber2);
	cm.commit(tx);
	assertEquals(100, report1.getBalance());
	assertEquals(0, report2.getBalance());
    }
    
    @Test
    public void testRollback() {
	testCommit();
	Transaction tx = cm.startNewAndActivate();
	bank1.addAmount(accountNumber1, -10);
	bank2.addAmount(accountNumber2, 10);
	BankAccountReport report1 = bank1.getReportForAccount(accountNumber1);
	BankAccountReport report2 = bank2.getReportForAccount(accountNumber2);
	assertEquals(90, report1.getBalance());
	assertEquals(10, report2.getBalance());
	cm.rollback(tx);
	cm.startNewAndActivate();
	report1 = bank1.getReportForAccount(accountNumber1);
	report2 = bank2.getReportForAccount(accountNumber2);
	assertEquals(100, report1.getBalance());
	assertEquals(0, report2.getBalance());
    }
    

}
