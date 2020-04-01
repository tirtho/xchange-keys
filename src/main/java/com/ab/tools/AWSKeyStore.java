package com.ab.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.EncryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.LimitExceededException;
import com.amazonaws.services.secretsmanager.model.MalformedPolicyDocumentException;
import com.amazonaws.services.secretsmanager.model.PreconditionNotMetException;
import com.amazonaws.services.secretsmanager.model.ResourceExistsException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;

public class AWSKeyStore implements ICloudKeyStore {

	private AWSSecretsManager client;	
    private static final Logger logger = Logger.getLogger(AWSKeyStore.class.getName());

	public AWSKeyStore(String secretName, String endPoints, String awsRegion) {
		super();
		AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endPoints, awsRegion);
		AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
		clientBuilder.setEndpointConfiguration(config);
		this.client = clientBuilder.build();
	}

	/**
	 * Decrypts secret using the associated KMS CMK
	 * 
	 * Assumes -
	 * secret value is a string
	 * @secretName is either the friendly name of the secret or the ARN (Amazon Resource Name)
	 */
	@Override
	public String getSecret(String secretName) {
		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
		GetSecretValueResult getSecretValueResponse = null;
		try {
			getSecretValueResponse = this.client.getSecretValue(getSecretValueRequest);
			getSecretValueResponse.getSecretString();
		} catch(ResourceNotFoundException e) {
			logger.log(Level.SEVERE, String.format("The requested secret %s was not found."
					+ "Check if you created the secret as a string or not.", secretName));
		} catch(InvalidRequestException e) {
			logger.log(Level.SEVERE, String.format("The request was invalid due to: %s", e.getMessage()));
		}
		return null;
	}

	/**
	 * Stores a secret in AWS KMS
	 * Assumes - 
	 * @secretName is the friendly name or the ARN (AMazon Resource Name) 
	 * the secret does not exist already
	 */
	@Override
	public boolean setSecret(String secretName, String secretValue) {
		CreateSecretRequest createSecretRequest = new CreateSecretRequest();
		createSecretRequest.setName(secretName);
		createSecretRequest.setSecretString(secretValue);
		try {
			client.createSecret(createSecretRequest);
		} catch (ResourceExistsException e) {
			logger.log(Level.SEVERE, "A key by this name already exists");
			return false;
		} catch (InvalidParameterException | InvalidRequestException | LimitExceededException | 
				EncryptionFailureException | ResourceNotFoundException | MalformedPolicyDocumentException | 
				InternalServiceErrorException | PreconditionNotMetException e) {
			logger.log(Level.SEVERE, String.format("Failed to save secret: %s", e.getMessage()));
			return false;
		}
		return true;
	}

}
