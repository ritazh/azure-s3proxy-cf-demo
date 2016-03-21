This application was derived from [azure-doge](https://github.com/asaikali/azure-doge)

To run this application you need to first obtain the following: 

* Azure Storage Account name
* Azure Storage Account access key
* AWS S3 Account key
* AWS S3 Account secret

Create a container in your storage account, you will need this later.

Upload `aws.pdf` to S3 and `azure.pdf` to Azure storage

Deploy 2 instances of s3proxy to CF, follow [these steps](https://github.com/ritazh/s3proxydocker/blob/master/Deploy-to-Cloud-Foundry.md)

* S3Proxy for Azure:
App url: `s3proxy.app.<cf instance public ip>.xip.io`
Create a wildcard route for this instance as: `*.app.<cf instance public ip>.xip.io`
Note to use `s3proxy.app.<cf instance public ip>.xip.io` for `s3proxy.virtual-host` in the `s3proxy.conf` file before you push it to docker hub
* S3Proxy for AWS:
App url: s3proxyaws.app.<cf instance public ip>.xip.io
Create a domain `s3proxyapp.<cf instance public ip>.xip.io`
Create a wildcard route for this instance as: `*.s3proxyapp.<cf instance public ip>.xip.io`
Note to use `s3proxy.s3proxyapp.<cf instance public ip>.xip.io` for `s3proxy.virtual-host` in the `s3proxy.conf` file before you push it to docker hub
Make sure you provide AWS values for `jclouds.region`, `jclouds.provider=aws-s3`,`jclouds.identity`,`jclouds.credential`

Create a file `application.properties` at the project root with the following properties and values:
azure.storage.account.name=
azure.storage.account.key=
s3proxy.azureendpoint= <this is the endpoint for the s3proxy for azure>
s3proxy.awsendpoint= <this is the endpoint for the s3proxy for aws>
containername= <this is the container already created in your storage account for both azure and aws>
AWS_ACCESS_KEY=
AWS_SECRET_ACCESS_KEY=

Create a file `manifest.yml` from `manifest.template.yml`. Fill in the same properties/values as `application.properties`.

To build this application:
`mvn clean package`

To push this application to CF:
`cf push <appname>`

To test this deployment:
Hit the following URL:
http://<appname>.app.<cf instance public ip>.xip.io/imagesaws
http://<appname>.app.<cf instance public ip>.xip.io/imagesazure
http://<appname>.app.<cf instance public ip>.xip.io/images3proxyaws
http://<appname>.app.<cf instance public ip>.xip.io/images3proxyazure


Additional Resources

* [Introduction to Microsoft Azure Storage](https://azure.microsoft.com/en-us/documentation/articles/storage-introduction)
* [How to use Blob storage from Java](https://azure.microsoft.com/en-us/documentation/articles/storage-java-how-to-use-blob-storage/)
