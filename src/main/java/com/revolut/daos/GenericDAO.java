package com.revolut.daos;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public abstract class GenericDAO{

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:revolut_tf;DB_CLOSE_DELAY=-1;MVCC=TRUE";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";
    private static final ComboPooledDataSource poolConnection = new ComboPooledDataSource();
    private static final Logger logger = LogManager.getLogger(GenericDAO.class);
    
    static{
    	try {
			poolConnection.setDriverClass(DB_DRIVER);
			poolConnection.setJdbcUrl(DB_URL);
			poolConnection.setUser(DB_USER);
			poolConnection.setPassword(DB_PASSWORD);
		} catch (PropertyVetoException e) {
			logger.error(e);
		}
		
    }
    
    protected static Connection getCurrentConnection(){
    	try {
	    	return poolConnection.getConnection();
    	} catch (Exception e) {
        	logger.error(e);
        	return null;
        }
    }
    
    protected void close(Object obj) {
		if (obj != null) {
			try {
				if (obj instanceof PreparedStatement) {
					((PreparedStatement) obj).close();
				} else if (obj instanceof ResultSet) {
					((ResultSet) obj).close();
				} else if (obj instanceof Connection) {
					((Connection) obj).close();
				}
			} catch (Exception e) {

			}

		}
	}
	
	
}
