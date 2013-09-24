package compensation.api;


public interface CompensationAdvice {

    void commit(Operation operation);
    void rollback(Operation operation);
}
