package com.revolut.services;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revolut.daos.AccountDAO;
import com.revolut.daos.impl.AccountDAOImpl;
import com.revolut.entities.Account;
import com.revolut.exceptions.AppException;
import com.revolut.exceptions.DAOException;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {

	private static Logger logger = LogManager.getLogger(AccountService.class);
	private final AccountDAO accountDAO = new AccountDAOImpl();
	
	@GET
    @Path("/list")
    public List<Account> findAll(){
		List<Account> list = null;
		try {
			list = accountDAO.findAll();
			if(list.isEmpty()){
				throw new AppException("Empty account list", Response.Status.NOT_FOUND);
			}
			if(logger.isDebugEnabled()){
				logger.debug(list);
			}
		} catch (DAOException e) {
			throw new AppException("An error has occured. " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		return list;
    }
	
	@GET
    @Path("/{id}")
    public Account findById(@PathParam("id") Long id){
		Account account = null;
		try {
			account = accountDAO.findById(id);
			if(account == null){
				logger.debug("Account "  + id + " not found");
				throw new AppException("No account exists with id: " + id, Response.Status.NOT_FOUND);
			}
			if(logger.isDebugEnabled()){
				logger.debug(account);
			}
		} catch (DAOException e) {
			throw new AppException("An error has occured. " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		return account;
    }
	
	@PUT
	@Path("/{id}")
    public Account update(@PathParam("id") Long id, Account account){
		try {
			Account originalAccount = accountDAO.findById(id);
			if(originalAccount == null){
				throw new AppException("No account exists with id: " + id, Response.Status.NOT_FOUND);
			}
			originalAccount.updateAccount(account);
			if(logger.isDebugEnabled()){
				logger.debug(originalAccount);
			}
			return accountDAO.update(originalAccount);
		} catch (DAOException e) {
			logger.error(e);
			throw new AppException("An error has occured while updating account id: " + id + ". " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR,e);
		}
    }
	
	@POST
	@Path("/create")
    public Account create(Account account){
		try {
			account = accountDAO.insert(account);
			if(logger.isDebugEnabled()){
				logger.debug(account);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("An error has occured while creating the account. " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		return account;
    }
	
	@DELETE
	@Path("/{id}")
    public Response delete(@PathParam("id") Long id){
		try {
			accountDAO.delete(id);
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("An error has occured while deleting the account", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		return Response.status(Response.Status.OK).entity("Account deleted").type(MediaType.TEXT_PLAIN).build();
    }
}
