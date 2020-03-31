package com.ab.tools;

public interface ICloudKeyStore {

	public static final String HTTPS = "https://";
	String getSecret(String secretName);
	boolean setSecret(String secretName, String secretValue);
	
}
