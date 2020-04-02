package com.ab.tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.bettercloud.vault.SslConfig;
import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;

public class HCVKeyStore implements ICloudKeyStore {

    private static final int HTTP_STATUS_200 = 200;
	private static final String VAULT_CLIENT_SSL_CERT_KEY = "VAULT_CLIENT_SSL_CERT_KEY";
	private static final String VAULT_CLIENT_SSL_CERT = "VAULT_CLIENT_SSL_CERT";
	private static final String FALSE = "false";
	private static final String VAULT_SSL_VERIFY = "VAULT_SSL_VERIFY";
	private static final String VAULT_READ_TIMEOUT = "VAULT_READ_TIMEOUT";
	private static final String VAULT_OPEN_TIMEOUT = "VAULT_OPEN_TIMEOUT";

	private static final int TIME_OUT_VALUE = 30;

    
	private static final Logger logger = Logger.getLogger(HCVKeyStore.class.getName());
    private final Vault vault;
    private String path;
    
	public HCVKeyStore() throws VaultException {
		final VaultConfig config;
		if (StringUtils.equalsIgnoreCase(getEnvironmentVariable(VAULT_SSL_VERIFY, FALSE), FALSE)) {
			config = new VaultConfig()
				.openTimeout(StringUtils.isNumeric(getEnvironmentVariable(VAULT_OPEN_TIMEOUT, String.valueOf(TIME_OUT_VALUE))) ? 
						Integer.valueOf(getEnvironmentVariable(VAULT_OPEN_TIMEOUT,  String.valueOf(TIME_OUT_VALUE))) : TIME_OUT_VALUE)
				.readTimeout(StringUtils.isNumeric(getEnvironmentVariable(VAULT_READ_TIMEOUT, String.valueOf(TIME_OUT_VALUE))) ? 
						Integer.valueOf(getEnvironmentVariable(VAULT_READ_TIMEOUT,  String.valueOf(TIME_OUT_VALUE))) : TIME_OUT_VALUE)
				.sslConfig(new SslConfig().verify(false).build())
				.build();
		} else {
			SslConfig sslConfig = new SslConfig().build();
			sslConfig.verify(true);
			// If SSL Client auth is setup at the Vault Server and the 
			// VAULT_CLIENT_SSL_CERT and VAULT_CLIENT_SSL_CERT_KEY paths are provided
			// in system environment
			if (!StringUtils.isBlank(VAULT_CLIENT_SSL_CERT) && !StringUtils.isBlank(VAULT_CLIENT_SSL_CERT_KEY)) {
				sslConfig.clientPemFile(new File(System.getenv(VAULT_CLIENT_SSL_CERT)))
				.clientKeyPemFile(new File(System.getenv(VAULT_CLIENT_SSL_CERT_KEY)));
			}
			config = new VaultConfig()
					.openTimeout(StringUtils.isNumeric(getEnvironmentVariable(VAULT_OPEN_TIMEOUT, String.valueOf(TIME_OUT_VALUE))) ? 
							Integer.valueOf(getEnvironmentVariable(VAULT_OPEN_TIMEOUT,  String.valueOf(TIME_OUT_VALUE))) : TIME_OUT_VALUE)
					.readTimeout(StringUtils.isNumeric(getEnvironmentVariable(VAULT_READ_TIMEOUT, String.valueOf(TIME_OUT_VALUE))) ? 
							Integer.valueOf(getEnvironmentVariable(VAULT_READ_TIMEOUT,  String.valueOf(TIME_OUT_VALUE))) : TIME_OUT_VALUE)
					.build();
		}
		vault = new Vault(config);
	}

	@Override
	public String getSecret(String secretName) {
		if (StringUtils.isBlank(path)) {
			logger.log(Level.SEVERE, "First set the path, before attempting to read secret");
			return null;
		}
		try {
			return vault.logical().read(path).getData().get(secretName);
		} catch (VaultException e) {
			logger.log(Level.SEVERE, "Failed to get the secret from Vault: " + e.getMessage());
			return null;
		}
	}

	public void setPath(String vaultPath) {
		this.path = vaultPath;
	}
	
	@Override
	public boolean setSecret(String secretName, String secretValue) {
		Map<String, Object> kvMap = new HashMap<>();
		kvMap.put(secretName, secretValue);
		try {
			final LogicalResponse writeResponse = vault.logical().write(path, kvMap);
			int httpStatus = writeResponse.getRestResponse().getStatus();
			if ( httpStatus == HTTP_STATUS_200) {
				return true;
			} else {
				logger.log(Level.SEVERE, "Failed to set the secret into Vault. HTTP RESPONSE STATUS = " + httpStatus);
			}
		} catch (VaultException e) {
			logger.log(Level.SEVERE, "Failed to set the secret into Vault: " + e.getMessage());
		}
		return false;
	}

	private String getEnvironmentVariable(String name, String defaultValue) {
		String value = System.getenv(name);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		} else {
			return value;
		}
	}
}
