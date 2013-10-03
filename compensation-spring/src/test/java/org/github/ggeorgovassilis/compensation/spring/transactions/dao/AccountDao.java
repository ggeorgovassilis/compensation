package org.github.ggeorgovassilis.compensation.spring.transactions.dao;

import org.github.ggeorgovassilis.compensation.spring.transactions.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<Account, String>{
	
}
