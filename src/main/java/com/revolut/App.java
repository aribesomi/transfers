package com.revolut;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import com.revolut.daos.impl.TestDAOImpl;

public class App {
	private static final Logger logger = LogManager.getLogger(App.class);
	
	public static void main(String[] args) throws Exception {
		TestDAOImpl testDAO = new TestDAOImpl();
		testDAO.loadInitialData();

		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/api/*");
//		servletHolder.setInitParameter("jersey.config.server.provider.classnames",
//				UserService.class.getCanonicalName() + "," + AccountService.class.getCanonicalName() + ","
//						+ ServiceExceptionMapper.class.getCanonicalName() + ","
//						+ TransactionService.class.getCanonicalName());
		
		
		servletHolder.setInitParameter("jersey.config.server.provider.packages", "com.revolut.services");
		try {
			server.start();
			server.join();
		} catch (Exception ex) {
			logger.error(ex);
        } finally {
			server.destroy();
		}
	}
}
