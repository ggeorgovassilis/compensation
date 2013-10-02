package org.github.ggeorgovassilis.compensation.api;

import java.util.ArrayList;
import java.util.List;

public class TransactionException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6959190095101373388L;
	protected List<Exception> causes = new ArrayList<Exception>();
    
    public void addCause(Exception e) {
	super.setStackTrace(e.getStackTrace());
	causes.add(e);
    }
    
    public List<Exception> getCauses(){
	return causes;
    }
}
