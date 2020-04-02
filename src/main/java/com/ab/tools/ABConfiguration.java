package com.ab.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ABConfiguration {
	private static String PROPERTIES_FILE = "META-INF/service.properties";
	private static String VERSION = "com.ab.tools.xchangekeys.version";
	private String version;
	
		
    private static final Logger logger = Logger.getLogger(ABConfiguration.class.getName());

	public ABConfiguration() {
		super();
		try {
			Configuration config = new PropertiesConfiguration(PROPERTIES_FILE);
			setVersion(config.getString(VERSION));
		} catch (ConfigurationException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
