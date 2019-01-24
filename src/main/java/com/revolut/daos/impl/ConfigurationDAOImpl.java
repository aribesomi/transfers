package com.revolut.daos.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;

import com.revolut.daos.ConfigurationDAO;
import com.revolut.daos.GenericDAO;

public class ConfigurationDAOImpl extends GenericDAO implements ConfigurationDAO{

	private static final Logger logger = LogManager.getLogger(ConfigurationDAOImpl.class);
	
	@Override
	public void loadInitialData() {
		Connection conn = getCurrentConnection();
		try {
			RunScript.execute(conn, new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/initialData.sql"))));
		} catch (SQLException e) {
			logger.error(e);
		}
	}

}
