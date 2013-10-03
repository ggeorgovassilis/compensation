package org.github.ggeorgovassilis.compensation.spring.transactions.compensation;

import org.github.ggeorgovassilis.compensation.api.CompensationAdvice;
import org.github.ggeorgovassilis.compensation.api.Operation;

public abstract class AbstractBankOperation extends Operation{

	protected AbstractBankOperation(CompensationAdvice advice) {
		super(advice);
	}
	
	public abstract void undo();

}
