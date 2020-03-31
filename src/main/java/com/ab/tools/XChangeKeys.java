/**
 * 
 */
package com.ab.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
/**
 * @author tibarar
 *
 */
public class XChangeKeys {
			
    private static final String SOURCE = "source";
	private static final String DESTINATION = "destination";
	private static final String CLI_SECRET_VALUE = "cliSecretValue";
	private static final String AWS_REGION = "awsRegion";
	private static final String AWS_END_POINT = "awsEndPoint";
	private static final String AWS_SECRET_NAME = "awsSecretName";
	private static final String AZURE_SECRET_NAME = "azureSecretName";
	private static final String AZURE_KEY_VAULT_NAME = "azureKeyVaultName";
	private static final Logger logger = Logger.getLogger(XChangeKeys.class.getName());
	private static enum CLOUD_PROVIDER {AWS, AZ, CLI}; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
       
        logger.log(Level.INFO, "Starting XChangeKeys");
        // -azKV <name> -azSecretName <secret name> -azSecret <secret>
        CLOUD_PROVIDER destination;
        CLOUD_PROVIDER source;
        String azKeyVaultName;
        String azSecretName;
        String awsSecretName;
        String awsEndPoint;
        String awsRegion;
        String cliSecretValue;
        
        ABConfiguration config = new ABConfiguration();
        
        Options options = new Options();
        Option destinationOpt = new Option("dest", DESTINATION, true, "Destination Key Store [az/aws/cli]");
        destinationOpt.setRequired(true);
        options.addOption(destinationOpt);

        Option sourceOpt = new Option("src", SOURCE, true, "Source Key Store [az/aws/cli]");
        sourceOpt.setRequired(true);
        options.addOption(sourceOpt);

        Option cliSecretValueOpt = new Option("cliSV", CLI_SECRET_VALUE, true, "Commandline Secret Value");
        cliSecretValueOpt.setRequired(false);
        options.addOption(cliSecretValueOpt);
        
        Option azKeyVaultNameOpt = new Option("azKV", AZURE_KEY_VAULT_NAME, true, "Azure KeyVault Name");
        azKeyVaultNameOpt.setRequired(false);
        options.addOption(azKeyVaultNameOpt);

        Option azSecretNameOpt = new Option("azSN", AZURE_SECRET_NAME, true, "Azure KeyVault Secret Name");
        azSecretNameOpt.setRequired(false);
        options.addOption(azSecretNameOpt);
        
        Option awsSecretNameOpt = new Option("awsSN", AWS_SECRET_NAME, true, "AWS Secrets Manager Secret Name");
        awsSecretNameOpt.setRequired(false);
        options.addOption(awsSecretNameOpt);
        
        Option awsEndPointOpt = new Option("awsEP", AWS_END_POINT, true, "AWS Secrets Manager EndPoint");
        awsEndPointOpt.setRequired(false);
        options.addOption(awsEndPointOpt);
        
        Option awsRegionOpt = new Option("awsR", AWS_REGION, true, "AWS Secrets Manager Region");
        awsRegionOpt.setRequired(false);
        options.addOption(awsRegionOpt);
        
        // Create the command line parser
        CommandLineParser parser = new DefaultParser();
        try {
        	// parse the command line arguments
        	CommandLine cl = parser.parse(options, args);
        	destination = CLOUD_PROVIDER.valueOf(cl.getOptionValue(DESTINATION).toUpperCase());
        	source = CLOUD_PROVIDER.valueOf(cl.getOptionValue(SOURCE).toUpperCase());
        	azKeyVaultName = cl.getOptionValue(AZURE_KEY_VAULT_NAME);
        	azSecretName = cl.getOptionValue(AZURE_SECRET_NAME);
        	awsEndPoint = cl.getOptionValue(AWS_END_POINT);
        	awsRegion = cl.getOptionValue(AWS_REGION);
        	awsSecretName = cl.getOptionValue(AWS_SECRET_NAME);
        	cliSecretValue = cl.getOptionValue(CLI_SECRET_VALUE);
        	// Validate
        	// If input is from command line, both name and value should be passed
        	if (source.compareTo(CLOUD_PROVIDER.CLI) == 0) {
        		if (StringUtils.isEmpty(cliSecretValue)) {
        			logger.log(Level.WARNING, "Source is selected as command line, yet secret value not supplied");
        			return;
        		}
        	}
        	if (source.compareTo(CLOUD_PROVIDER.AWS) == 0 || destination.compareTo(CLOUD_PROVIDER.AWS) == 0) {
        		if (StringUtils.isEmpty(awsRegion) || StringUtils.isEmpty(awsEndPoint) || StringUtils.isEmpty(awsSecretName)) {
        			logger.log(Level.WARNING, "AWS Region, End Point and AWS Secret Name required");
        			return;
        		}
        	}
        	if (source.compareTo(CLOUD_PROVIDER.AZ) == 0 || destination.compareTo(CLOUD_PROVIDER.AZ) == 0) {
        		if (StringUtils.isEmpty(azKeyVaultName) || StringUtils.isEmpty(azSecretName)) {
        			logger.log(Level.WARNING, "Azure KeyVault name and name of Secret is required");
        			return;
        		}
        	}
        } catch (ParseException e) {
        	logger.log(Level.SEVERE, "Parsing commandline arguments failed : " + e.getMessage());
        	return;
        }
        
        if (source.compareTo(CLOUD_PROVIDER.AWS) == 0) {
        	String secret = getAwsSecret(awsEndPoint, awsRegion, awsSecretName);
        	if (destination.compareTo(CLOUD_PROVIDER.AZ) == 0) {
        		putAzureSecret(azKeyVaultName, azSecretName, secret);
        	} else if (destination.compareTo(CLOUD_PROVIDER.CLI) == 0) {
        		logger.log(Level.INFO, String.format("Retrieved AWS Secret : %s / %s", awsSecretName, secret));
        	}
        } else if (source.compareTo(CLOUD_PROVIDER.AZ) == 0) {
        	String secret = getAzureSecret(azKeyVaultName, azSecretName);
        	if (destination.compareTo(CLOUD_PROVIDER.AWS) == 0) {
            	putAwsSecret(awsEndPoint, awsRegion, awsSecretName, secret);
        	} else if (destination.compareTo(CLOUD_PROVIDER.CLI) == 0) {
        		logger.log(Level.INFO, String.format("Retrieved Azure KeyVault Secret : %s / %s", azSecretName, secret));
        	}
        } else if (source.compareTo(CLOUD_PROVIDER.CLI) == 0) {
        	if (destination.compareTo(CLOUD_PROVIDER.AWS) == 0) {
            	putAwsSecret(awsEndPoint, awsRegion, awsSecretName, cliSecretValue);
        	} else if (destination.compareTo(CLOUD_PROVIDER.AZ) == 0) {
            	putAzureSecret(azKeyVaultName, azSecretName, cliSecretValue);
        	}
        }        
	}

	private static String getAwsSecret(String awsEndPoint, String awsRegion, String awsSecretName) {
        // Connect to AWS KMS
		AWSKeyStore awsKeyStore = new AWSKeyStore(awsSecretName, awsEndPoint, awsRegion);
		return awsKeyStore.getSecret(awsSecretName);
	}	

	private static void putAwsSecret(String awsEndPoint, String awsRegion, String awsSecretName, String secret) {
		AWSKeyStore awsKeyStore = new AWSKeyStore(awsSecretName, awsEndPoint, awsRegion);
		awsKeyStore.setSecret(awsSecretName, secret);
	}

	private static String getAzureSecret(String azKeyVaultName, String azSecretName) {
        // Connect to Azure KeyVault
        AzureKeyStore azureKeyStore = new AzureKeyStore(azKeyVaultName);
		return azureKeyStore.getSecret(azSecretName);
	}

	private static void putAzureSecret(String azKeyVaultName, String azSecretName, String secret) {
        // Connect to Azure KeyVault
        AzureKeyStore azureKeyStore = new AzureKeyStore(azKeyVaultName);
        azureKeyStore.setSecret(azSecretName, secret);
	}

}
