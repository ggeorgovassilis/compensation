package org.github.ggeorgovassilis.compensation.onlineshop.model;

import java.util.Date;

public class BankAccountReport {

    private Date date;
    private int accountNumber;
    private int balance;

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public int getAccountNumber() {
	return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
	this.accountNumber = accountNumber;
    }

    public int getBalance() {
	return balance;
    }

    public void setBalance(int balance) {
	this.balance = balance;
    }
}
