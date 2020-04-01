Exchange keys across public cloud key stores 
This java application will allow you to copy keys/secrets from AWS KMS to Azure Key Vault and vice versa
You can also create keys/secrets from command line to AWS KMS or Azure KeyVault

# Set Environment Variables for Azure Service Principal Credentials
export AZURE_CLIENT_ID=<your-clientID>
export AZURE_CLIENT_SECRET=<your-clientSecret>
export AZURE_TENANT_ID=<your-tenantId>
export KEY_VAULT_NAME=<your-key-vault-name>

# Set Environment Variables for AWS SDK calls
# https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html
export AWS_ACCESS_KEY_ID=your_access_key_id
export AWS_SECRET_ACCESS_KEY=your_secret_access_key
export AWS_REGION=your desired AWS region (e.g. us-east-2)
# replace 'export' with 'set' for Windows, to set above environment
# variables
# In Windows commandline, run 'set' to view all environment variables