package org.github.ggeorgovassilis.compensation.onlineshop.model;

import java.util.Date;

public class MoneyOrder {

    private Date date;
    private int accountNumberFrom;
    private int accountNumberTo;
    private int amount;
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getAccountNumberFrom() {
        return accountNumberFrom;
    }
    public void setAccountNumberFrom(int accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }
    public int getAccountNumberTo() {
        return accountNumberTo;
    }
    public void setAccountNumberTo(int accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
