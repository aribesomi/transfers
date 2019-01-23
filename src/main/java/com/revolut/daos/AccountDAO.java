package com.revolut.daos;

import java.util.List;

import com.revolut.entities.Account;
import com.revolut.exceptions.DAOException;

public interface AccountDAO {

	List<Account> findAll() throws DAOException;
	Account findById(Long id) throws DAOException;
	Account insert(Account account) throws DAOException;
	void delete(Long id) throws DAOException;
	Account update(Account account) throws DAOException;

}
