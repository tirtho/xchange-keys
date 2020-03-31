package com.ab.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ABConfiguration {
	private static String PROPERTIES_FILE = "META-INF/service.properties";
	private static String AWS_URL = "com.ab.tools.aws";
	private static String AZURE_URL = "com.ab.tools.azure";
	private String awsUrl;
	private String azureUrl;
	
		
    private static final Logger logger = Logger.getLogger(ABConfiguration.class.getName());

	public ABConfiguration() {
		super();
		try {
			Configuration config = new PropertiesConfiguration(PROPERTIES_FILE);
			setAwsUrl(config.getString(AWS_URL));
			setAzureUrl(config.getString(AZURE_URL));
		} catch (ConfigurationException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	public String getAwsUrl() {
		return awsUrl;
	}

	public void setAwsUrl(String awsUrl) {
		this.awsUrl = awsUrl;
	}

	public String getAzureUrl() {
		return azureUrl;
	}

	public void setAzureUrl(String azureUrl) {
		this.azureUrl = azureUrl;
	}
}
