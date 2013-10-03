package org.github.ggeorgovassilis.compensation.api;


public interface CompensationManager {

    void registerOperation(Operation operation, Transaction transaction);

    void commit(Transaction transaction);

    void rollback(Transaction transaction);

    Transaction startNew();

    void setActiveTransaction(Transaction tx);

    Transaction getActiveTransaction();

    Transaction startNewAndActivate();

    <T> T createProxyFor(T target, Class<?>[] proxyInterfaces,
	    CompensationAdvice advice);
    

}