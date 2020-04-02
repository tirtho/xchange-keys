# xchange-keys #
This Java Application allows you to write/read secrets to or between Public Cloud Providers.
This could be integrated into your automated CI/CD pipelines in a multi-cloud environment.
Supported Public Cloud Service Providers are 

- Azure Key Vault
- Amazon KMS
- HashiCorp Vault

# Table of Contents #
- [xchange-keys](#xchange-keys)
  * [BUILD](#build)
  * [SETUP](#setup)
      - [Azure KeyVault](#azure-keyvault)
      - [AWS Secrets Manager (KMS)](#aws-secrets-manager--kms-)
      - [HashiCorp Vault](#hashicorp-vault)
  * [RUN](#run)
      - [From Azure KeyVault to AWS Secrets Manager](#from-azure-keyvault-to-aws-secrets-manager)
      - [From HashiCorp Vault to AWS Secrets Manager](#from-hashicorp-vault-to-aws-secrets-manager)
      - [From Command Line to Azure KeyVault](#from-command-line-to-azure-keyvault)
      - [Run commmand with --help](#run-commmand-with---help)
      - [Command line arguments](#command-line-arguments)
  * [License](#license)


## BUILD ##

Prerequisites are:

- Maven 3.6.3 or above
- JDK 8 (tested with 1.8.0_241)
- git (tested with version 2.23.0 in Windows 10)

In your local machine go to your working folder for this project and run

```sh
> git clone https://github.com/tirtho/xchange-keys.git
> cd xchange-keys
> mvn clean package
```

## SETUP ##

Before you run the program, you need to setup the Cloud Services and System Environment Variables

#### Azure KeyVault ####

You need an Azure KeyVault that can be accessed using an Azure Service Principal. 
Follow this [document](https://docs.microsoft.com/en-us/azure/key-vault/quick-create-java) to setup in Azure.
Set the following environment variables before running this Java Application

```sh
> set AZURE_CLIENT_ID=<your-clientID>
> set AZURE_CLIENT_SECRET=<your-clientSecret>
> set AZURE_TENANT_ID=<your-tenantId>
> set KEY_VAULT_NAME=<your-key-vault-name>
```

#### AWS Secrets Manager (KMS) ####

You need an AWS account and access to the Secrets Manager Console. 
Learn more [here](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)
Check your secrets from the Secrets Manager Console. 
[Example link](https://us-east-2.console.aws.amazon.com>/secretsmanager/home?region=us-east-2#/listSecrets) 
Set the following environment variables before running this Java Application

```sh
> set AWS_ACCESS_KEY_ID=<your access key>
> set AWS_REGION=<your region>
> set AWS_SECRET_ACCESS=<your secret access key>
```

#### HashiCorp Vault ####

You need a Hashi Corp Vault Server to read/write secrets. More details on setting up a Vault Server are [here](https://www.vaultproject.io/) 
The Java client for HashiCorp Vault secrets was taken from GitHub - [BetterCloud/vault-java-driver][hashiLib] 
Set the following environment variables before running this Java Application

```sh
> set VAULT_ADDR=your-vault-address (e.g. http://127.0.0.1:8200)
> set VAULT_TOKEN="your-token"
```

Below variables are optional

```sh
> set VAULT_OPEN_TIMEOUT=your-open-timeout
> set VAULT_READ_TIMEOUT=your-read-timeout
```

If Vault is running over SSL

```sh
> set VAULT_SSL_VERIFY=true if Vault server uses SSL Certificate
> set VAULT_SSL_CERT=path to an X.509 certificate in unencrypted PEM format, using UTF-8 encoding
```

If Vault's TLS Certificate auth backend for SSL client auth is setup
you need to pass the command line argument -hcvCCert <the path to the file 
containing your client certificate and private key in unencrypted PEM format, using UTF-8 encoding>

```sh
> set VAULT_CLIENT_SSL_CERT=path-to-your-client-certificate (PEM UTF-8 encoded)
> set VAULT_CLIENT_SSL_CERT_KEY=path-to-your-client-certificate-private-key (PEM UTF-8 encoded)
```

## RUN ##

After the above setup is complete, you can run the application from command line. 
Make sure Java 8 is in the PATH.
Your application is in the target folder, with the name xchangekeys-0.1-SNAPSHOT.jar
EXAMPLE run with command line arguments are below:

#### From Azure KeyVault to AWS Secrets Manager ####
```sh
> java -jar xchangekeys-0.1-SNAPSHOT.jar -src AZ -azKV TBDemoKeyVault -azSN cliSentSecret -dest AWS -awsR us-east-2 -awsEP secretsmanager.us-east-2.amazonaws.com -awsSN fromAzureCliSentSecret
```

#### From HashiCorp Vault to AWS Secrets Manager ####
```sh
> java -jar target\xchangekeys-0.1-SNAPSHOT.jar -src hcv -dest aws -awsR us-east-2 -awsEP secretsmanager.us-east-2.amazonaws.com -awsSN testFromHashi-secret-test-testSecretName -hcvP secret/test -hcvSN testSecretName
```

#### From Command Line to Azure KeyVault ####
```sh
> java -jar target\xchangekeys-0.1-SNAPSHOT.jar -src cli -cliSV qwe123 -dest az -azKV TBDemoKeyVault -azSN cliSentSecret123
```

#### Run commmand with --help ####
```sh
> java -jar target\xchangekeys-0.1-SNAPSHOT.jar --help
```

#### Command line arguments ####
```sh
    -h, --help
            help message
    -src, --source
            Source Key Store [cli/az/aws/hcv]
    -dest, --destination
            Destination Key Store [cli/az/aws/hcv]
    -cliSV, --cliSecretValue
            Commandline Secret Value
    -azKV, --azureKeyVaultName
            Azure KeyVault Name
    -azSN, --azureSecretName
            Azure KeyVault Secret Name
    -awsEP, --awsEndPoint
            AWS Secrets Manager EndPoint
    -awsR, --awsRegion
            AWS Secrets Manager Region
    -awsSN, --awsSecretName
            AWS Secrets Manager Secret Name
    -hcvP, --hcvPath
            HashiCorp Vault Path
    -hcvSN, --hcvSecretName
            HashiCorp Vault Secret Name
```


License
----

Copyright (c) 2020-2025 Tirthankar Barari

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


[//]: # (Comments in Markdown. See details here - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[hashiLib]: <https://github.com/BetterCloud/vault-java-driver>
