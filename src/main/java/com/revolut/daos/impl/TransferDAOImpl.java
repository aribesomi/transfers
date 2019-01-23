package com.revolut.daos.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revolut.daos.GenericDAO;
import com.revolut.daos.TransferDAO;
import com.revolut.entities.Account;
import com.revolut.entities.Currency;
import com.revolut.entities.Transfer;
import com.revolut.exceptions.DAOException;

public class TransferDAOImpl extends GenericDAO implements TransferDAO {

	Logger logger = LogManager.getLogger(TransferDAOImpl.class);
	AccountDAOImpl accountDAO = new AccountDAOImpl();

	@Override
	public List<Transfer> findAll() throws DAOException {
		logger.debug("Listing transfers...");
		List<Transfer> transfers = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = getCurrentConnection();
		try {
			transfers = new ArrayList<Transfer>();
			ps = conn.prepareStatement("SELECT * FROM transfers");
			rs = ps.executeQuery();
			while (rs.next()) {
				Transfer transfer = new Transfer();
				Account from = accountDAO.findById(rs.getLong("idAccountFrom"));
				if (from == null) {
					throw new DAOException("Can not find the from account");
				}
				Account to = accountDAO.findById(rs.getLong("idAccountTo"));
				if (to == null) {
					throw new DAOException("Can not find the to account");
				}
				transfer.setTo(to);
				transfer.setFrom(from);
				transfer.setCreated(rs.getObject( "created" , Instant.class ));
				transfer.setDescription(rs.getString("description"));
				transfer.setId(rs.getLong("id"));
				transfer.setValue(rs.getBigDecimal("value"));
				transfers.add(transfer);
			}
		} catch (SQLException e) {
			throw new DAOException("An error occured while retrieving transfer list",e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}

		return transfers;
	}

	@Override
	public Transfer findById(Long id) throws DAOException {
		logger.debug("Finding transfer " + id);
		Transfer transfer = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = getCurrentConnection();
		try {
			ps = conn.prepareStatement("SELECT * FROM transfers WHERE id = ?");
			ps.setLong(1, id);
			rs = ps.executeQuery();
			// I'll have to get only the first element, not necessary to do a loop.
			if (rs.next()) {
				transfer = new Transfer();
				Account from = accountDAO.findById(rs.getLong("idAccountFrom"));
				if (from == null) {
					throw new DAOException("Can not find the from account");
				}
				Account to = accountDAO.findById(rs.getLong("idAccountTo"));
				if (to == null) {
					throw new DAOException("Can not find the to account");
				}
				transfer.setTo(to);
				transfer.setFrom(from);
				transfer.setCreated(rs.getObject("created", Instant.class));
				transfer.setDescription(rs.getString("description"));
				transfer.setId(rs.getLong("id"));
				transfer.setValue(rs.getBigDecimal("value"));

			}
		} catch (SQLException e) {
			throw new DAOException("An error occured while retrieving transfer by id",e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}

		return transfer;
	}

	@Override
	public Transfer makeTransfer(Transfer transfer) throws DAOException {
		logger.debug("Making a transfer ...");
		ResultSet selectRS = null;
		ResultSet insertRS = null;
		PreparedStatement selectPS = null;
		PreparedStatement updatePS = null;
		PreparedStatement insertPS = null;
		Connection conn = getCurrentConnection();
		Account currentFromAccount = null;
		Account currentToAccount = null;
		try {
			conn.setAutoCommit(false);
			
			/** We need to lock out the "to" and "from" accounts in order to verify if both accounts can make the transaction **/
			
			logger.debug("Finding FROM account...");
			selectPS = conn.prepareStatement("SELECT * FROM accounts where id = ? FOR UPDATE");
			selectPS.setLong(1, transfer.getFrom().getId());
			selectRS = selectPS.executeQuery();
			if(selectRS.next()){
				currentFromAccount = new Account(selectRS.getLong("id"), selectRS.getString("accountID"), selectRS.getBigDecimal("balance"), Currency.valueOf(selectRS.getString("currency")));
				logger.debug("From account found " + currentFromAccount);
			}else{
				throw new DAOException("Can not get account information. The transfer Can not be processed at this moment");
			}
			
			logger.debug("Finding TO account...");
			selectPS = conn.prepareStatement("SELECT * FROM accounts where id = ? FOR UPDATE");
			selectPS.setLong(1, transfer.getTo().getId());
			selectRS = selectPS.executeQuery();
			if(selectRS.next()){
				currentToAccount = new Account(selectRS.getLong("id"), selectRS.getString("accountID"), selectRS.getBigDecimal("balance"), Currency.valueOf(selectRS.getString("currency")));
				logger.debug("To account found " + currentToAccount);
			}else{
				throw new DAOException("Can not get account information. The transfer Can not be processed at this moment");
			}
			
			/** Check if balance from the "fromAccount" is enough **/
			if(transfer.getValue().compareTo(currentFromAccount.getBalance()) > 0){
				throw new DAOException("The from account hasn't enough money to make this transfer.");
			}
			
			/** Check if currency is the same **/
			if(currentFromAccount.getCurrency().compareTo(currentToAccount.getCurrency()) != 0){
				throw new DAOException("Accounts currencies must be the same to make this transfer.");
			}
			
			BigDecimal updatedFromBalance = currentFromAccount.getBalance().subtract(transfer.getValue());
			currentFromAccount.setBalance(updatedFromBalance);
			BigDecimal updatedToBalance = currentToAccount.getBalance().add(transfer.getValue());		
			currentToAccount.setBalance(updatedToBalance);
			
			logger.debug("Updating accounts ...");
			updatePS = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?");
			updatePS.setBigDecimal(1, currentFromAccount.getBalance());
			updatePS.setLong(2, currentFromAccount.getId());
			updatePS.executeUpdate();
			
			updatePS = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?");
			updatePS.setBigDecimal(1, currentToAccount.getBalance());
			updatePS.setLong(2, currentToAccount.getId());
			updatePS.executeUpdate();
			
			transfer.setFrom(currentFromAccount);
			transfer.setTo(currentToAccount);
			
			/** After all balances are updated, we register the transaction **/
			logger.debug("Registering transfer ...");
			insertPS = conn.prepareStatement("INSERT INTO transfers(idAccountFrom,idAccountTo,description,value,created) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			insertPS.setLong(1, transfer.getFrom().getId());
			insertPS.setLong(2, transfer.getTo().getId());
			insertPS.setString(3, transfer.getDescription());
			insertPS.setBigDecimal(4, transfer.getValue());
			insertPS.setObject(5,transfer.getCreated());
			insertPS.executeUpdate();
			insertRS = insertPS.getGeneratedKeys();
			if (insertRS.next()) {
				transfer.setId(insertRS.getLong(1));
				logger.debug("Transfer registered " + transfer);
			} else {
				throw new DAOException("Can not register the transfer.");
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception rollbackException) {
				throw new DAOException("Can not rollback account update transaction", rollbackException);
			}
			logger.error(e);
			throw new DAOException("Error in registering a transfer. " + e.getMessage(), e);
		} finally {
			close(selectPS);
			close(insertPS);
			close(updatePS);
			close(selectRS);
			close(insertRS);
			close(conn);
		}
		
		return transfer;
	}

}
