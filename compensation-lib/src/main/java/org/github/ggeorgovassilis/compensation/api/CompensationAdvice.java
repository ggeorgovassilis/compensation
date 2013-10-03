package org.github.ggeorgovassilis.compensation.api;


public interface CompensationAdvice<T> {

    void commit(Operation operation);
    void rollback(Operation operation);
    void setTarget(T target);
    T getTarget();
}
