package compensation.api;

import java.util.ArrayList;
import java.util.List;

public class TransactionException extends RuntimeException{

    protected List<Exception> causes = new ArrayList<Exception>();
    
    public void addCause(Exception e) {
	super.setStackTrace(e.getStackTrace());
	causes.add(e);
    }
    
    public List<Exception> getCauses(){
	return causes;
    }
}
