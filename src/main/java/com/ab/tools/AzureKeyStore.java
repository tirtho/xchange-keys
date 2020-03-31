package com.ab.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.core.exception.HttpRequestException;
import com.azure.core.exception.ResourceModifiedException;
import com.azure.identity.DefaultAzureCredentialBuilder;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

/**
 *  Need to set System Environment variables below first, before running this in jvm
 * 	export AZURE_CLIENT_ID=<your-clientID>
 * 	export AZURE_CLIENT_SECRET=<your-clientSecret>
 * 	export AZURE_TENANT_ID=<your-tenantId>
 * 	export KEY_VAULT_NAME=<your-key-vault-name>
 */
public class AzureKeyStore implements ICloudKeyStore {

	private static final String VAULT_AZURE_NET = ".vault.azure.net";
	private SecretClient secretClient;
    private static final Logger logger = Logger.getLogger(AzureKeyStore.class.getName());
	
	public AzureKeyStore(String keyVaultName) throws IllegalStateException {
		super();

		StringBuffer s = new StringBuffer(HTTPS);
		String keyVaultUri = s.append(keyVaultName).append(VAULT_AZURE_NET).toString();	
		this.secretClient = new SecretClientBuilder()
				.vaultUrl(keyVaultUri)
				.credential(new DefaultAzureCredentialBuilder().build())
				.buildClient();
	}

	@Override
	public String getSecret(String secretName) {
		if (secretName == null) {
			return null;
		}
		KeyVaultSecret secret = secretClient.getSecret(secretName);
		if (secret == null) {
			logger.log(Level.INFO, "Null value found for Secret named " + secretName);
			return null;
		}
		return secret.getValue();
	}

	@Override
	public boolean setSecret(String secretName, String secretValue) {
		try {
			secretClient.setSecret(new KeyVaultSecret(secretName, secretValue));
		} catch (ResourceModifiedException | HttpRequestException e) {
			logger.log(Level.SEVERE, String.format("Failed to set Secret: %s", e.getMessage()));
			return false;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to set secret to Azure KeyVault: %s", e.getMessage());
		}
		return true;
	}
}
