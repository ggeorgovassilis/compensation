package compensation.api;


public abstract class Operation {

    protected CompensationAdvice compensationAdvice;

    protected Operation(CompensationAdvice advice) {
	this.compensationAdvice = advice;
    }

    public CompensationAdvice getCompensationAdvice() {
        return compensationAdvice;
    }
}
