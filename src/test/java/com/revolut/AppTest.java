package com.revolut;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.revolut.daos.ConfigurationDAO;
import com.revolut.daos.impl.ConfigurationTestDAOImpl;
import com.revolut.entities.Account;
import com.revolut.entities.Currency;
import com.revolut.entities.Transfer;
import com.revolut.services.AccountService;
import com.revolut.services.TransferService;

public class AppTest extends JerseyTest {
	
	Logger logger = LogManager.getLogger(AppTest.class);
	
	@Override
	public Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		ConfigurationDAO configurationDAO = new ConfigurationTestDAOImpl();
		configurationDAO.loadInitialData();
		return new ResourceConfig(AccountService.class, TransferService.class);
	}
	
	@Test
    public void testListAllAccounts() {
        Response output = target("/accounts/list").request().get();
        assertEquals("should return status 200", 200, output.getStatus());
        assertNotNull("Should return an account list", output.getEntity());
    }
	
	@Test
    public void testFindAccountById() {
        Response output = target("/accounts/1").request().get();
        assertEquals("should return status 200", 200, output.getStatus());
        Account account = output.readEntity(Account.class);
        logger.debug("Account found: " + account);
        assertNotNull("Should return an account", account);
    }
	
	@Test
    public void testAccountUpdate() {
		//Update balance of account id 4 to 50.00. 
		Account accountToUpdate = new Account();
		accountToUpdate.setBalance(new BigDecimal("50"));
        Response output = target("/accounts/4").request().put(Entity.entity(accountToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("should return status 200", 200, output.getStatus());
        Account updated = output.readEntity(Account.class);
        logger.debug("Account update: " + updated);
        assertNotNull("Should return the updated account", updated);
    }
	
	@Test
    public void testAccountDelete() {
        Response output = target("/accounts/5").request().delete();
        assertEquals("should return status 200", 200, output.getStatus());
        String msg = output.readEntity(String.class);
        logger.debug("Account deleted - " + msg);
        assertEquals("Should return the following msg", "Account deleted", msg);
    }
	
	@Test
    public void testAccountCreate() {
		Account accountToCreate = new Account("111111111111111111",BigDecimal.ZERO,Currency.GBP);
        Response output = target("/accounts/create").request().post(Entity.entity(accountToCreate, MediaType.APPLICATION_JSON));
        assertEquals("should return status 200", 200, output.getStatus());
        Account created = output.readEntity(Account.class);
        logger.debug("Account created: " + created);
        assertNotNull("Should return the created account", created);
    }
	
	@Test
    public void testListAllTransfers() {
        Response output = target("/transfers/list").request().get();
        assertEquals("should return status 200", 200, output.getStatus());
        assertNotNull("Should return an account list", output.getEntity());
    }
	
	@Test
    public void testFindTransferById() {
        Response output = target("/transfers/1").request().get();
        assertEquals("should return status 200", 200, output.getStatus());
        Transfer transfer = output.readEntity(Transfer.class);
        logger.debug("Transfer found: " + transfer);
        assertNotNull("Should return an account", transfer);
    }
	
	
	@Test
    public void testMakeTransfer() {
		Transfer transfer = new Transfer(new Account(1L),new Account(2L), BigDecimal.TEN,"JUNIT Transfer test");
		Response output = target("/transfers/make-transfer").request().post(Entity.entity(transfer, MediaType.APPLICATION_JSON));
        assertEquals("should return status 200", 200, output.getStatus());
        Transfer transferMade = (Transfer)output.readEntity(Transfer.class);
        logger.debug("Transfer found: " + transferMade);
        assertNotNull("Should return an account", transferMade);
    }
}
