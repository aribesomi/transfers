package com.revolut.daos;

import java.util.List;

import com.revolut.entities.Transfer;
import com.revolut.exceptions.DAOException;

public interface TransferDAO {
	
	List<Transfer> findAll() throws DAOException;
	Transfer findById(Long id) throws DAOException;
	Transfer makeTransfer(Transfer transfer) throws DAOException;

}
