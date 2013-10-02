package org.github.ggeorgovassilis.compensation.onlineshop.services;

import org.github.ggeorgovassilis.compensation.onlineshop.model.BankAccountReport;

public interface IBankService {

    BankAccountReport getReportForAccount(int accountNumber);
    BankAccountReport addAmount(int accountNumber, int amount);
    BankAccountReport createBankAccount(int accountNumber);
    BankAccountReport deleteBankAccount(int accountNumber);
}
