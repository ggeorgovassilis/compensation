package org.github.ggeorgovassilis.compensation.spring.transactions.service;

import java.util.HashMap;
import java.util.Map;

import org.github.ggeorgovassilis.compensation.spring.transactions.dao.AccountDao;
import org.github.ggeorgovassilis.compensation.spring.transactions.model.Account;
import org.springframework.stereotype.Service;

/**
 * Remote, non-transactional version of a bank service. For the purposes of the demo a simple, in-memory, non-mt-safe implementation suffices.
 * @author g.georgovassilis@gmail.com
 *
 */
@Service("RemoteBankService")
public class RemoteNonTransactionalBankServiceImpl extends AbstractBankServiceImpl{

	protected RemoteNonTransactionalBankServiceImpl() {
		super("NONTRABANK-");
	}

	private AccountDao dao = new AccountDao() {
		
		private Map<String, Account> db = new HashMap<String, Account>();
		
		@Override
		public <S extends Account> Iterable<S> save(Iterable<S> account) {
			throw new RuntimeException("Not implemented");
		}
		
		@Override
		public <S extends Account> S save(S account) {
			if (account.getId() == null){
				account.setId(getNextAccountId());
			}
			db.put(account.getId(), account);
			return account;
		}
		
		@Override
		public Account findOne(String accountNr) {
			return db.get(accountNr);
		}
		
		@Override
		public Iterable<Account> findAll(Iterable<String> account) {
			throw new RuntimeException("Not implemented");
		}
		
		@Override
		public Iterable<Account> findAll() {
			throw new RuntimeException("Not implemented");
		}
		
		@Override
		public boolean exists(String accountNr) {
			return db.containsKey(accountNr);
		}
		
		@Override
		public void deleteAll() {
			db.clear();
		}
		
		@Override
		public void delete(Iterable<? extends Account> account) {
			throw new RuntimeException("Not implemented");
		}
		
		@Override
		public void delete(Account account) {
			throw new RuntimeException("Not implemented");
		}
		
		@Override
		public void delete(String accountNr) {
			throw new RuntimeException("Not implemented");
		}
		
		@Override
		public long count() {
			return db.size();
		}
	};

	@Override
	protected AccountDao getDaoFor(String accountNumber) {
		if (!isManagedByThis(accountNumber))
			throw new RuntimeException("Account number is not managed by this service: "+accountNumber);
		return dao;
	}
	
}
