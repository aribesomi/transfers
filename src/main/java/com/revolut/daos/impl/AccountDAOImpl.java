package com.revolut.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revolut.daos.AccountDAO;
import com.revolut.daos.GenericDAO;
import com.revolut.entities.Account;
import com.revolut.entities.Currency;
import com.revolut.exceptions.DAOException;

public class AccountDAOImpl extends GenericDAO implements AccountDAO {

	Logger logger = LogManager.getLogger(AccountDAOImpl.class);

	@Override
	public Account findById(Long id) throws DAOException {
		logger.debug("Finding account " + id);
		Account account = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = getCurrentConnection();
		try {
			ps = conn.prepareStatement("SELECT * FROM accounts WHERE id = ? FOR UPDATE");
			ps.setLong(1, id);
			rs = ps.executeQuery();
			// I'll have to get only the first element, not necessary to do a
			// loop.
			if (rs.next()) {
				account = new Account();
				account.setId(rs.getLong("id"));
				account.setBalance(rs.getBigDecimal("balance"));
				account.setAccountIdentification(rs.getString("accountID"));
				account.setCurrency(Currency.valueOf(rs.getString("currency")));

			}
		} catch (Exception e) {
			throw new DAOException("An error occure while retrieving the account " + id,e);
		} finally {
			close(rs);
			close(ps);
		}

		return account;
	}

	@Override
	public List<Account> findAll() throws DAOException {
		logger.debug("Listing accounts...");
		List<Account> accounts = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = getCurrentConnection();
		try {
			accounts = new ArrayList<Account>();
			ps = conn.prepareStatement("SELECT * FROM accounts");
			rs = ps.executeQuery();
			while (rs.next()) {
				Account account = new Account();
				account.setId(rs.getLong("id"));
				account.setBalance(rs.getBigDecimal("balance"));
				account.setAccountIdentification(rs.getString("accountID"));
				account.setCurrency(Currency.valueOf(rs.getString("currency")));
				accounts.add(account);
			}
		} catch (Exception e) {
			throw new DAOException("An error occure while retrieving the account list",e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}

		return accounts;
	}

	@Override
	public Account insert(Account account) throws DAOException {
		logger.debug("Creating account " + account);
		PreparedStatement ps = null;
		Connection conn = getCurrentConnection();
		ResultSet insertedKeys = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO accounts (accountID, balance, currency) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, account.getAccountIdentification());
			ps.setBigDecimal(2, account.getBalance());
			ps.setString(3, account.getCurrency().toString());
			ps.executeUpdate();
			insertedKeys = ps.getGeneratedKeys();
			if (insertedKeys.next()) {
				account.setId(insertedKeys.getLong(1));
			} else {
				logger.error("Insert failed.");
				throw new DAOException("Insert failed as no ID has returned");
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception rollbackException) {
				throw new DAOException("Can't rollback account creation transaction", rollbackException);
			}
			throw new DAOException("Error in account creation transaction", e);
		} finally {
			close(ps);
			close(insertedKeys);
			close(conn);
		}

		return account;
	}

	@Override
	public void delete(Long id) throws DAOException {
		logger.debug("Deleting account " + id);
		PreparedStatement ps = null;
		Connection conn = getCurrentConnection();
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("DELETE FROM accounts WHERE id = ?");
			ps.setLong(1, id);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception rollbackException) {
				throw new DAOException("Can't rollback account delete transaction", rollbackException);
			}
			throw new DAOException("Error in account delete transaction", e);
		} finally {
			close(ps);
			close(conn);
		}
	}

	@Override
	public Account update(Account account) throws DAOException {
		logger.debug("Updating account " + account);
		PreparedStatement ps = null;
		Connection conn = getCurrentConnection();
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("UPDATE accounts set accountID = ?, balance = ?, currency = ? WHERE id = ?");
			ps.setString(1, account.getAccountIdentification());
			ps.setBigDecimal(2, account.getBalance());
			ps.setString(3, account.getCurrency().toString());
			ps.setLong(4, account.getId());
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception rollbackException) {
				throw new DAOException("Can't rollback account update transaction", rollbackException);
			}
			throw new DAOException("Error in account update transaction", e);
		} finally {
			close(ps);
			close(conn);
		}
		return account;
	}

}
