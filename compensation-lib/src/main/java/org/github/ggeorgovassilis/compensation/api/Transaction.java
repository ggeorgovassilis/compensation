package org.github.ggeorgovassilis.compensation.api;

import java.util.ArrayList;
import java.util.List;

public class Transaction {

    public enum Status{
	active, committed, rolledback;
    }

    protected Status status = Status.active;
    protected List<Operation> operations = new ArrayList<Operation>();
    protected CompensationManager cm;
    
    public Transaction(CompensationManager cm) {
	this.cm = cm;
    }
    
    public List<Operation> getOperations() {
	return operations;
    }

    public void add(Operation operation) {
	operations.add(operation);
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    public Status getStatus() {
	return status;
    }
}
