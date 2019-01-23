package com.revolut.entities;

import java.io.Serializable;
import java.math.BigDecimal;

public class Account implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String accountIdentification;
	private BigDecimal balance;
	private Currency currency;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccountIdentification() {
		return accountIdentification;
	}
	public void setAccountIdentification(String accountIdentification) {
		this.accountIdentification = accountIdentification;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public Account() {
	}
	
	public Account(Long id, String accountIdentification, BigDecimal balance, Currency currency) {
		super();
		this.id = id;
		this.accountIdentification = accountIdentification;
		this.balance = balance;
		this.currency = currency;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountIdentification == null) ? 0 : accountIdentification.hashCode());
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountIdentification == null) {
			if (other.accountIdentification != null)
				return false;
		} else if (!accountIdentification.equals(other.accountIdentification))
			return false;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (currency != other.currency)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Account [id=" + id + ", accountIdentification=" + accountIdentification + ", balance=" + balance
				+ ", currency=" + currency + "]";
	}
	
	public void updateAccount(Account modifiedAccount){
		if(modifiedAccount.getAccountIdentification() != null){
			this.accountIdentification = modifiedAccount.getAccountIdentification();
		}
		if(modifiedAccount.getBalance() != null){
			this.balance = modifiedAccount.getBalance();
		}
		if(modifiedAccount.getCurrency() != null){
			this.currency = modifiedAccount.getCurrency();
		}
	}
	
}
