package org.github.ggeorgovassilis.compensation.impl;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.github.ggeorgovassilis.compensation.api.CompensationAdvice;
import org.github.ggeorgovassilis.compensation.api.CompensationManager;
import org.github.ggeorgovassilis.compensation.api.Operation;
import org.github.ggeorgovassilis.compensation.api.Transaction;
import org.github.ggeorgovassilis.compensation.api.TransactionException;

public class CompensationManagerImpl implements CompensationManager {

    private ThreadLocal<Transaction> activeTransaction = new ThreadLocal<Transaction>();

    @Override
    public void registerOperation(Operation operation, Transaction transaction) {
	transaction.add(operation);
    }

    @Override
    public void commit(Transaction transaction) {
	transaction.setStatus(Transaction.Status.committed);
	TransactionException exception = null;
	for (Operation operation : transaction.getOperations())
	    try {
		operation.getCompensationAdvice().commit(operation);
	    } catch (Exception e) {
		exception = exception==null?new TransactionException():exception;
		exception.addCause(e);
	    }
	if (exception!=null)
	    throw exception;
    }

    @Override
    public void rollback(Transaction transaction) {
	transaction.setStatus(Transaction.Status.rolledback);
	TransactionException exception = null;
	List<Operation> ops = new ArrayList<Operation>(transaction.getOperations());
	Collections.reverse(ops);
	for (Operation operation : ops)
	    try {
		operation.getCompensationAdvice().rollback(operation);
	    } catch (Exception e) {
		exception = exception==null?new TransactionException():exception;
		exception.addCause(e);
	    }
	if (exception!=null)
	    throw exception;
    }

    @Override
    public Transaction startNew() {
	return new Transaction(this);
    }

    @Override
    public void setActiveTransaction(Transaction tx) {
	activeTransaction.set(tx);
    }

    @Override
    public Transaction getActiveTransaction() {
	return activeTransaction.get();
    }

    @Override
    public Transaction startNewAndActivate() {
	Transaction tx = startNew();
	setActiveTransaction(tx);
	return tx;
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T createProxyFor(T target, Class<?>[] proxyInterfaces,
	    final CompensationAdvice advice) {
	if (target == null)
	    throw new IllegalArgumentException("target cannot be null");
	if (advice == null)
	    throw new IllegalArgumentException("advice cannot be null");

	if (proxyInterfaces == null)
	    proxyInterfaces = new Class[0];

	return (T)Proxy.newProxyInstance(getClass().getClassLoader(),
		proxyInterfaces, new AdviceProxy(advice));
    }
}
