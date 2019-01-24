package com.revolut.daos.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;

import com.revolut.daos.GenericDAO;
import com.revolut.daos.ConfigurationDAO;

public class ConfigurationTestDAOImpl extends GenericDAO implements ConfigurationDAO{

	private static final Logger logger = LogManager.getLogger(ConfigurationTestDAOImpl.class);
	
	@Override
	public void loadInitialData() {
		Connection conn = getCurrentConnection();
		try {
			RunScript.execute(conn, new FileReader("src/test/resources/initialTestData.sql"));
		} catch (FileNotFoundException e) {
			try {
				RunScript.execute(conn, new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/initialTestData.sql"))));
			} catch (SQLException sqlException) {
				logger.error(e);
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

}
