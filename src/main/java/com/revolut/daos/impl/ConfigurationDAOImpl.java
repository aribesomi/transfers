package com.revolut.daos.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;

import com.revolut.daos.GenericDAO;
import com.revolut.daos.ConfigurationDAO;

public class ConfigurationDAOImpl extends GenericDAO implements ConfigurationDAO{

	private static final Logger logger = LogManager.getLogger(ConfigurationDAOImpl.class);
	
	@Override
	public void loadInitialData() {
		Connection conn = getCurrentConnection();
		try {
			RunScript.execute(conn, new FileReader("src/main/resources/initialData.sql"));
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
	}

}
