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

import com.bettercloud.vault.VaultException;
/**
 * @author tibarar
 *
 */
public class XChangeKeys {

	private static final String HELP = "--help";
	private static final String HELP_SHORT = "-h";

	private static enum CLOUD_PROVIDER {AWS, AZ, HCV, CLI}; 
	private static final String cloudProviders = String.format("[%s/%s/%s/%s]", 
			CLOUD_PROVIDER.CLI.toString().toLowerCase(),
			CLOUD_PROVIDER.AZ.toString().toLowerCase(),
			CLOUD_PROVIDER.AWS.toString().toLowerCase(),
			CLOUD_PROVIDER.HCV.toString().toLowerCase());
	// Source arguments
	private static final String SOURCE = "source";
	private static final String SOURCE_SHORT = "src";
	private static final String SOURCE_HELP_MSG = "Source Key Store " + cloudProviders;

	// Destination arguments
	private static final String DESTINATION_SHORT = "dest";
	private static final String DESTINATION = "destination";
	private static final String DESTINATION_HELP_MSG = "Destination Key Store " + cloudProviders;
	
	// CLI arguments
	private static final String CLI_SECRET_VALUE_SHORT = "cliSV";
	private static final String CLI_SECRET_VALUE = "cliSecretValue";
	private static final String CLI_SECRET_VALUE_HELP_MSG = "Commandline Secret Value";

	// AZURE arguments
	private static final String AZURE_SECRET_NAME_SHORT = "azSN";
	private static final String AZURE_SECRET_NAME = "azureSecretName";
	private static final String AZURE_KEY_VAULT_SECRET_NAME_HELP_MSG = "Azure KeyVault Secret Name";

	private static final String AZURE_KEY_VAULT_NAME_SHORT = "azKV";
	private static final String AZURE_KEY_VAULT_NAME = "azureKeyVaultName";
	private static final String AZURE_KEY_VAULT_NAME_HELP_MSG = "Azure KeyVault Name";

	// AWS arguments
	private static final String AWS_REGION_SHORT = "awsR";
	private static final String AWS_REGION = "awsRegion";
	private static final String AWS_SECRETS_MANAGER_REGION_HELP_MSG = "AWS Secrets Manager Region";

	private static final String AWS_END_POINT_SHORT = "awsEP";
	private static final String AWS_END_POINT = "awsEndPoint";
	private static final String AWS_SECRETS_MANAGER_END_POINT_HELP_MSG = "AWS Secrets Manager EndPoint";
	
	private static final String AWS_SECRET_NAME_SHORT = "awsSN";
	private static final String AWS_SECRET_NAME = "awsSecretName";
	private static final String AWS_SECRETS_MANAGER_SECRET_NAME_HELP_MSG = "AWS Secrets Manager Secret Name";

	// HashiCorp arguments
	private static final String HCV_PATH_SHORT = "hcvP";
	private static final String HCV_PATH = "hcvPath";
	private static final String HASHI_CORP_VAULT_PATH_HELP_MSG = "HashiCorp Vault Path";

	private static final String HCV_SECRET_NAME_SHORT = "hcvSN";
	private static final String HCV_SECRET_NAME = "hcvSecretName";
	private static final String HASHI_CORP_VAULT_SECRET_NAME_HELP_MSG = "HashiCorp Vault Secret Name";
	
	private static final Logger logger = Logger.getLogger(XChangeKeys.class.getName());

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
        String hcvPath;
        String hcvSecretName;
        
        // First check if help should be displayed
        if (args.length == 0) {
        	displayHelpMessage();
        	return;
        }
        for (String arg : args) {
        	if (StringUtils.equalsIgnoreCase(arg, HELP_SHORT) || StringUtils.equalsIgnoreCase(arg, HELP)) {
        		displayHelpMessage();
        		return;
        	}
        }
        
        ABConfiguration config = new ABConfiguration();
        logger.log(Level.INFO, String.format("%s version %s", XChangeKeys.class.getName(), config.getVersion()));
        
        Options options = new Options();
        Option destinationOpt = new Option(DESTINATION_SHORT, DESTINATION, true, DESTINATION_HELP_MSG);
        destinationOpt.setRequired(true);
        options.addOption(destinationOpt);

        Option sourceOpt = new Option(SOURCE_SHORT, SOURCE, true, SOURCE_HELP_MSG);
        sourceOpt.setRequired(true);
        options.addOption(sourceOpt);

        Option cliSecretValueOpt = new Option(CLI_SECRET_VALUE_SHORT, CLI_SECRET_VALUE, true, CLI_SECRET_VALUE_HELP_MSG);
        cliSecretValueOpt.setRequired(false);
        options.addOption(cliSecretValueOpt);
        
        Option hcvSecretNameOpt = new Option(HCV_SECRET_NAME_SHORT, HCV_SECRET_NAME, true, HASHI_CORP_VAULT_SECRET_NAME_HELP_MSG);
        hcvSecretNameOpt.setRequired(false);
        options.addOption(hcvSecretNameOpt);
        
        Option hcvPathOpt = new Option(HCV_PATH_SHORT, HCV_PATH, true, HASHI_CORP_VAULT_PATH_HELP_MSG);
        hcvPathOpt.setRequired(false);
        options.addOption(hcvPathOpt);
                
        Option azKeyVaultNameOpt = new Option(AZURE_KEY_VAULT_NAME_SHORT, AZURE_KEY_VAULT_NAME, true, AZURE_KEY_VAULT_NAME_HELP_MSG);
        azKeyVaultNameOpt.setRequired(false);
        options.addOption(azKeyVaultNameOpt);

        Option azSecretNameOpt = new Option(AZURE_SECRET_NAME_SHORT, AZURE_SECRET_NAME, true, AZURE_KEY_VAULT_SECRET_NAME_HELP_MSG);
        azSecretNameOpt.setRequired(false);
        options.addOption(azSecretNameOpt);
        
        Option awsSecretNameOpt = new Option(AWS_SECRET_NAME_SHORT, AWS_SECRET_NAME, true, AWS_SECRETS_MANAGER_SECRET_NAME_HELP_MSG);
        awsSecretNameOpt.setRequired(false);
        options.addOption(awsSecretNameOpt);
        
        Option awsEndPointOpt = new Option(AWS_END_POINT_SHORT, AWS_END_POINT, true, AWS_SECRETS_MANAGER_END_POINT_HELP_MSG);
        awsEndPointOpt.setRequired(false);
        options.addOption(awsEndPointOpt);
        
        Option awsRegionOpt = new Option(AWS_REGION_SHORT, AWS_REGION, true, AWS_SECRETS_MANAGER_REGION_HELP_MSG);
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
        	hcvPath = cl.getOptionValue(HCV_PATH);
        	hcvSecretName = cl.getOptionValue(HCV_SECRET_NAME);
        	
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
        	if (source.compareTo(CLOUD_PROVIDER.HCV) == 0 || destination.compareTo(CLOUD_PROVIDER.HCV) == 0) {
        		if (StringUtils.isEmpty(hcvSecretName) || StringUtils.isEmpty(hcvPath)) {
        			logger.log(Level.WARNING, "HashiCorp Vault path and secret name required");
        			return;
        		}
        	}
        } catch (ParseException e) {
        	logger.log(Level.SEVERE, "Parsing commandline arguments failed : " + e.getMessage());
        	displayHelpMessage();
        	return;
        }
        
        if (source.compareTo(CLOUD_PROVIDER.AWS) == 0) {
        	String secret = getAwsSecret(awsEndPoint, awsRegion, awsSecretName);
        	if (destination.compareTo(CLOUD_PROVIDER.AZ) == 0) {
        		putAzureSecret(azKeyVaultName, azSecretName, secret);
        	} else if (destination.compareTo(CLOUD_PROVIDER.CLI) == 0) {
        		logger.log(Level.INFO, String.format("Retrieved AWS Secret : %s / %s", awsSecretName, secret));
        	} else if (destination.compareTo(CLOUD_PROVIDER.HCV) == 0) {
        		putHashiCorpVaultSecret(hcvPath, hcvSecretName, secret);
        	}
        } else if (source.compareTo(CLOUD_PROVIDER.AZ) == 0) {
        	String secret = getAzureSecret(azKeyVaultName, azSecretName);
        	if (destination.compareTo(CLOUD_PROVIDER.AWS) == 0) {
            	putAwsSecret(awsEndPoint, awsRegion, awsSecretName, secret);
        	} else if (destination.compareTo(CLOUD_PROVIDER.CLI) == 0) {
        		logger.log(Level.INFO, String.format("Retrieved Azure KeyVault Secret : %s / %s", azSecretName, secret));
        	} else if (destination.compareTo(CLOUD_PROVIDER.HCV) == 0) {
        		putHashiCorpVaultSecret(hcvPath, hcvSecretName, secret);
        	}
        } else if (source.compareTo(CLOUD_PROVIDER.CLI) == 0) {
        	if (destination.compareTo(CLOUD_PROVIDER.AWS) == 0) {
            	putAwsSecret(awsEndPoint, awsRegion, awsSecretName, cliSecretValue);
        	} else if (destination.compareTo(CLOUD_PROVIDER.AZ) == 0) {
            	putAzureSecret(azKeyVaultName, azSecretName, cliSecretValue);
        	} else if (destination.compareTo(CLOUD_PROVIDER.HCV) == 0) {
        		putHashiCorpVaultSecret(hcvPath, hcvSecretName, cliSecretValue);
        	}
        } else if (source.compareTo(CLOUD_PROVIDER.HCV) == 0) {
        	String secret = getHashiCorpVaultSecret(hcvPath, hcvSecretName);
        	if (destination.compareTo(CLOUD_PROVIDER.AWS) == 0) {
            	putAwsSecret(awsEndPoint, awsRegion, awsSecretName, secret);
        	} else if (destination.compareTo(CLOUD_PROVIDER.AZ) == 0) {
            	putAzureSecret(azKeyVaultName, azSecretName, secret);
        	} else if (destination.compareTo(CLOUD_PROVIDER.CLI) == 0) {
        		logger.log(Level.INFO, String.format("Retrieved HashiCorp Vault Path & Secret : %s - %s %s", hcvPath, hcvSecretName, secret));
        	}
        }
	}

	private static String getHashiCorpVaultSecret(String hcvPath, String hcvSecretName) {
		try {
			HCVKeyStore hcvKeyStore = new HCVKeyStore();
			hcvKeyStore.setPath(hcvPath);
			return hcvKeyStore.getSecret(hcvSecretName);
		} catch (VaultException e) {
			logger.log(Level.SEVERE, "Failed to connect to HashiCorp Vault");
			return null;
		}
	}

	private static void putHashiCorpVaultSecret(String hcvPath, String hcvSecretName, String secret) {
		try {
			HCVKeyStore hcvKeyStore = new HCVKeyStore();
			hcvKeyStore.setPath(hcvPath);
			hcvKeyStore.setSecret(hcvSecretName, secret);
		} catch (VaultException e) {
			logger.log(Level.SEVERE, "Failed to connect to HashiCorp Vault");
			return;
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

	private static void displayHelpMessage() {
		StringBuffer message = new StringBuffer(String.format("%s command line arguments - ", XChangeKeys.class.getName()));
		message
		.append(String.format("\n\t%s, %s\n\t\thelp message", HELP_SHORT, HELP))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", SOURCE_SHORT, SOURCE, SOURCE_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", DESTINATION_SHORT, DESTINATION, DESTINATION_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", CLI_SECRET_VALUE_SHORT, CLI_SECRET_VALUE, CLI_SECRET_VALUE_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", AZURE_KEY_VAULT_NAME_SHORT, AZURE_KEY_VAULT_NAME, AZURE_KEY_VAULT_NAME_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", AZURE_SECRET_NAME_SHORT, AZURE_SECRET_NAME, AZURE_KEY_VAULT_SECRET_NAME_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", AWS_END_POINT_SHORT, AWS_END_POINT, AWS_SECRETS_MANAGER_END_POINT_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", AWS_REGION_SHORT, AWS_REGION, AWS_SECRETS_MANAGER_REGION_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", AWS_SECRET_NAME_SHORT, AWS_SECRET_NAME, AWS_SECRETS_MANAGER_SECRET_NAME_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", HCV_PATH_SHORT, HCV_PATH, HASHI_CORP_VAULT_PATH_HELP_MSG))
		.append(String.format("\n\t-%s, --%s\n\t\t%s", HCV_SECRET_NAME_SHORT, HCV_SECRET_NAME, HASHI_CORP_VAULT_SECRET_NAME_HELP_MSG));

		logger.log(Level.INFO, message.toString());
		return;
	}

}
