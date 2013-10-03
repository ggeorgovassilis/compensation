package org.github.ggeorgovassilis.compensation.spring.transactions.service;


import org.github.ggeorgovassilis.compensation.spring.transactions.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Local, transactional version of a bank service. Transactions over this service instance are managed by spring and any changes made to the
 * persistence store are committed or undone by the spring transaction manager.
 * @author g.georgovassilis@gmail.com
 *
 */
@Transactional
@Service("LocalBankService")
public class LocalTransactionalBankServiceImpl extends AbstractBankServiceImpl{

	protected LocalTransactionalBankServiceImpl() {
		super("TRANSACTBANK-");
	}

	@Autowired
	private AccountDao accountDao;
	
	@Override
	protected AccountDao getDaoFor(String accountNumber) {
		if (!isManagedByThis(accountNumber))
			throw new IllegalArgumentException("Account number "+accountNumber+" is not managed by this service");
		return accountDao;
	}

}
