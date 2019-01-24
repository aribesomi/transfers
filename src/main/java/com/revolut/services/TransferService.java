package com.revolut.services;

import java.time.Instant;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revolut.daos.TransferDAO;
import com.revolut.daos.impl.TransferDAOImpl;
import com.revolut.entities.Transfer;
import com.revolut.exceptions.AppException;
import com.revolut.exceptions.DAOException;

@Path("/transfers")
@Produces(MediaType.APPLICATION_JSON)
public class TransferService {

	private static Logger logger = LogManager.getLogger(AccountService.class);
	private final TransferDAO transferDAO = new TransferDAOImpl();
	
	
	@GET
    @Path("/list")
    public List<Transfer> findAll(){
		List<Transfer> list = null;
		try {
			list = transferDAO.findAll();
			if(list.isEmpty()){
				throw new AppException("Empty transfer list", Response.Status.NOT_FOUND);
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
    public Transfer findById(@PathParam("id") Long id){
		Transfer transfer = null;
		try {
			transfer = transferDAO.findById(id);
			if(transfer == null){
				logger.debug("Transfer "  + id + " not found");
				throw new AppException("No transfer exists with id: " + id, Response.Status.NOT_FOUND);
			}
			if(logger.isDebugEnabled()){
				logger.debug(transfer);
			}
		} catch (DAOException e) {
			throw new AppException("An error has occured. " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		return transfer;
    }
	
	@POST
	@Path("/make-transfer")
    public Transfer makeTransfer(Transfer transfer){
		try {
			if(transfer == null){
				logger.error("Missing transfer information");
				throw new AppException("Missing transfer information", Response.Status.NOT_FOUND);
			}
			if(transfer.getFrom() == null || transfer.getFrom().getId() == null){
				logger.error("Missing parameter: ID of FROM account");
				throw new AppException("Missing parameter: ID of FROM account", Response.Status.NOT_FOUND);
			}
			if(transfer.getTo() == null || transfer.getTo().getId() == null){
				logger.error("Missing parameter: ID of TO account");
				throw new AppException("Missing parameter: ID of TO account", Response.Status.NOT_FOUND);
			}
			if(transfer.getValue() == null){
				logger.error("Missing parameter: VALUE");
				throw new AppException("Missing parameter: VALUE", Response.Status.NOT_FOUND);
			}
			if(transfer.getDescription() == null){
				logger.error("Missing parameter: DESCRIPTION");
				throw new AppException("Missing parameter: DESCRIPTION", Response.Status.NOT_FOUND);
			}
			transfer.setCreated(Instant.now());
			transfer.setId(null);
			transfer = transferDAO.makeTransfer(transfer);
			if(logger.isDebugEnabled()){
				logger.debug(transfer);
			}
		} catch (DAOException e) {
			logger.error(e);
			throw new AppException("An error has occured while making a transfer. " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		
		return transfer;
    }
}
