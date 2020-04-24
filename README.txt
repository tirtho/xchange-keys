Exchange keys across public cloud key stores 
This java application will allow you to copy keys/secrets between Azure Key Vault, AWS KMS or HasiCorp Vault
You can also pass a secret from command line to Azure Key Vault, AWS KMS or HashiCorp Vault and vice versa

# You can download the jar artifact from - 
# https://dev.azure.com/TRDemoAzureDevOpsOrg/xchange-keys/_packaging?_a=package&feed=XChange-Keys&package=com.ab.tools%3Axchangekeys&protocolType=maven&version=0.1-SNAPSHOT&view=overview
# Or pull the code from GitHub, build and run

Before running the program, make sure you have these System Environment Variables set -
# Set Environment Variables for ---Azure--- Service Principal Credentials
export AZURE_CLIENT_ID=<your-clientID>
export AZURE_CLIENT_SECRET=<your-clientSecret>
export AZURE_TENANT_ID=<your-tenantId>
export KEY_VAULT_NAME=<your-key-vault-name>

# Set Environment Variables for ---AWS--- SDK calls
# https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html
export AWS_ACCESS_KEY_ID=your_access_key_id
export AWS_SECRET_ACCESS_KEY=your_secret_access_key
export AWS_REGION=your desired AWS region (e.g. us-east-2)

# Set Environment Variables for ---HashiCorp Vault--- SDK calls
export VAULT_ADDR=your-vault-address (e.g. http://127.0.0.1:8200)
export VAULT_TOKEN="your-token"
# Below variables are optional
export VAULT_OPEN_TIMEOUT=your-open-timeout
export VAULT_READ_TIMEOUT=your-read-timeout
# If Vault is running over SSL
export VAULT_SSL_VERIFY=true if Vault server uses SSL Certificate
export VAULT_SSL_CERT=path to an X.509 certificate in unencrypted PEM format, using UTF-8 encoding
# If Vault's TLS Certificate auth backend for SSL client auth is setup
# you need to pass the command line argument -hcvCCert <the path to the file 
# containing your client certificate and private key in unencrypted PEM format, using UTF-8 encoding>
export VAULT_CLIENT_SSL_CERT=path-to-your-client-certificate (PEM UTF-8 encoded)
export VAULT_CLIENT_SSL_CERT_KEY=path-to-your-client-certificate-private-key (PEM UTF-8 encoded)
# replace 'export' with 'set' for Windows, to set above environment
# variables
# In Windows command line, run 'set' to view all environment variables

EXAMPLE command line arguments ---
From Azure KeyVault to AWS Secrets Manager
> java -jar xchangekeys-0.1-SNAPSHOT.jar -src AZ -azKV TBDemoKeyVault -azSN cliSentSecret -dest AWS -awsR us-east-2 -awsEP secretsmanager.us-east-2.amazonaws.com -awsSN fromAzureCliSentSecret

From HashiCorp Vault to AWS Secrets Manager
> java -jar target\xchangekeys-0.1-SNAPSHOT.jar -src hcv -dest aws -awsR us-east-2 -awsEP secretsmanager.us-east-2.amazonaws.com -awsSN testFromHashi-secret-test-testSecretName -hcvP secret/test -hcvSN testSecretName

From Command Line to Azure KeyVault
> java -jar target\xchangekeys-0.1-SNAPSHOT.jar -src cli -cliSV qwe123 -dest az -azKV TBDemoKeyVault -azSN cliSentSecret123
