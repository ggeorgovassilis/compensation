package org.github.ggeorgovassilis.compensation.spring.transactions.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Account implements Serializable {

	private static final long serialVersionUID = -6102677271834368215L;

	@Id
	private String id;

	@Column(nullable = false)
	private int balance;

	@Version
	@Column(nullable = false)
	private int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

}
