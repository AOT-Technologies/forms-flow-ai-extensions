# Download and Installation

In the following document, we’ll describe about the different project dependencies, and the installation options being supported.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Download the Camunda RPA Client Extension](#download-the-camunda-rpa-client-extension)
3. [Installation](#installation)
   * [Standalone Installation](#standalone-installation)
   * [Docker Based Installation](#docker-based-installation)

## Prerequisites

* Admin access to a local or remote server (can be local Windows PC or Mac) 
* For docker based installation [Docker](https://docker.com) needs to be installed.

## Download the Camunda RPA Client Extension

* Clone this github repo:  https://github.com/shibin-aot/formsflow-ai-extensions.git

## Installation

There are multiple options for installing Camunda RPA Client Extension. They are given below

- [Standalone Installation](#standalone-installation)
- [Docker Based Installation](#docker-based-installation)
  
## Standalone Installation

------------------
### Project Dependencies

- [>= JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven ](https://maven.apache.org)
- [Robocorp RCC](https://robocorp.com/docs/rcc/installation) / [Robocorp Cloud](cloud.robocorp.com/) account

### Configuration Setup
 
  * Modify the variables in application.yaml file.
 
 ##### Keycloak Integration
--------------------------

   Variable name | Meaning | Possible values | Default value |
 --- | --- | --- | ---
 `keycloak.url`:triangular_flag_on_post:| URL to your Keycloak server || `http://localhost:8080`
 `keycloak.url.realm`|	The Keycloak realm to use|eg. forms-flow-ai | `forms-flow-ai`
 `keycloak.clientId`|Your Keycloak Client ID within the realm| eg. forms-flow-bpm | `forms-flow-bpm`
 `keycloak.clientSecret`:triangular_flag_on_post:|The secret for your Keycloak Client Id|eg. 22ce6557-6b86-4cf4-ac3b-42338c7b1ac12|`must be set to your Keycloak client secret`
 
 
##### RPA_JDBC : Dedicated rpa-client database.
-----------------------------------------------------------------------

   Variable name | Meaning | Possible values | Default value |
 --- | --- | --- | ---
 `spring.datasource.url`|H2 JDBC DB Connection URL|Used on installation to create the database. Choose your own|`jdbc:h2:./camunda-rpa-client-db`
 `spring.datasource.driverClassName`|H2 JDBC Database Driver||`org.h2.Driver`
 `spring.datasource.username`|H2 Database Username|Used on installation to create the database. Choose your own|`sa`
 `spring.datasource.password`|H2 Database Password|Used on installation to create the database. Choose your own|`sa`
 `spring.datasource.connectionTimeout`|Hikari Connection optimization setting||`30000`
 `spring.datasource.idleTimeout`|Hikari Connection optimization setting||`600000`
 `spring.datasource.maximumPoolSize`|Hikari Connection optimization setting||`10`
 `spring.datasource.validationTimeout`|Hikari Connection optimization setting||`5000`
 
   ##### Formsflow-ai Settings 
-----------------------------------------------------------------------

   Variable name | Meaning | Possible values | Default value |
 --- | --- | --- | ---
 `formsflow.formio.enabled`|||`true`
 `formsflow.formio.url`|Formio URL||`https://{your ip address}:3001`
 `formsflow.security.username`|Formio Username||`admin@example.com`
 `formsflow.security.password`|Formio Password||`changeme`
 
  ##### General Settings 
 
-----------------------------------------------------------------------

   Variable name | Meaning | Possible values | Default value |
 --- | --- | --- | ---
 `robot.base-dir`|Base directory of robot||`/opt/robots`
 `robot.cloud.api-url`|Robocorp Cloud base url||
 `robot.cloud.api-key`|Robocorp Cloud api key||
 `client.base-url`|Camunda Base URL||`https://localhost:8000`
 `client.max-tasks`|Specifies the maximum amount of tasks that can be fetched within one request.||`10`
 `disable-backoff-strategy`|||`true` 
 `async-response-timeout`|Specifies the response timeout.||`10000` ms


 
#### Running the application

We can run the application by below command.

```       
mvn spring-boot:run
```  
## Health Check

The application should be up and available for use at port defaulted to 8090 in http://localhost:8090/api/robot/healthcheck. By this step standalone based installation is completed.


## Docker Based Installation

------------------

### Project Dependencies

- [Docker](https://docker.com)

### Solution Setup
 
#### Installation

   * Make sure you have a Docker machine up and running.
   * Rename the file [sample.env](./sample.env) to **.env**.
   * Modify the environment variables in the newly created **.env** file if needed. Environment variables are given in the table below,
   * **NOTE : {your-ip-address} given inside the .env file should be changed to your host system IP address. Please take special care to identify the correct IP address if your system has multiple network cards**
 
> :information_source: Variables with trailing :triangular_flag_on_post: in below table should be updated in the .env file
     
##### Keycloak Integration
--------------------------

   Variable name | Meaning | Possible values | Default value |
 --- | --- | --- | ---
 `KEYCLOAK_URL`:triangular_flag_on_post:| URL to your Keycloak server || `http://{your-ip-address}:8080`
 `KEYCLOAK_URL_REALM`|	The Keycloak realm to use|eg. forms-flow-ai | `forms-flow-ai`
 `KEYCLOAK_BPM_CLIENT_ID`|Your Keycloak Client ID within the realm| eg. forms-flow-bpm | `forms-flow-bpm`
 `KEYCLOAK_BPM_CLIENT_SECRET`:triangular_flag_on_post:|The secret for your Keycloak Client Id|eg. 22ce6557-6b86-4cf4-ac3b-42338c7b1ac12|`must be set to your Keycloak client secret`
##### RPA_JDBC : Dedicated rpa-client database.
-----------------------------------------------------------------------

   Variable name | Meaning | Possible values | Default value |
 --- | --- | --- | ---
 `RPA_JDBC_URL`|Postgres JDBC DB Connection URL|Used on installation to create the database. Choose your own|`jdbc:postgresql://camunda-rpa-client-db:5432/rpa-client`
 `RPA_JDBC_DRIVER`|Postgres JDBC Database Driver||`org.postgresql.Driver`
 `RPA_POSTGRES_USER`|Postgres Database Username|Used on installation to create the database. Choose your own|`postgres`
 `RPA_POSTGRES_PASSWORD`|Postgres Database Password|Used on installation to create the database. Choose your own|`password`
 `RPA_JDBC_DB_NAME`|Postgres Database Name|Used on installation to create the database. Choose your own|`rpa-client`
 `RPA_HIKARI_CONN_TIMEOUT`|Hikari Connection optimization setting||`30000`
 `RPA_HIKARI_IDLE_TIMEOUT`|Hikari Connection optimization setting||`600000`
 `RPA_HIKARI_MAX_POOLSIZE`|Hikari Connection optimization setting||`10`
 `RPA_HIKARI_VALID_TIMEOUT`|Hikari Connection optimization setting||`5000`
 
  ##### Formsflow-ai Settings 
-----------------------------------------------------------------------

   Variable name | Meaning | Possible values | Default value |
 --- | --- | --- | ---
 `FORMIO_ENABLED`|||`true`
 `FORMIO_URL`|Formio URL||`https://{your ip address}:3001`
 `FORMIO_USERNAME`|Formio Username||`admin@example.com`
 `FORMIO_PASSWORD`|Formio Password||`changeme`
 
 ##### General Settings 
-----------------------------------------------------------------------

   Variable name | Meaning | Possible values | Default value |
 --- | --- | --- | ---
 `ROBOT_BASE_DIR`|Base directory of robot||`/opt/robots`
 `ROBOCORP_CLOUD_BASE_URL`|Robocorp Cloud base url||
 `ROBOCORP_CLOUD_APIKEY`|Robocorp Cloud api key||
 `CAMUNDA_BPM_URL`|Camunda Base URL||`https://{your ip address}:8000`
 `MAX_TASKS`|Specifies the maximum amount of tasks that can be fetched within one request.||`10`
 `LOCK_DURATION`|Specifies the duration for the next task pull.||`3000` ms
 

 
 
 #### Running the application

* camunda-rpa-client service uses port 8090, make sure the port is available.

Run
```       
docker-compose up -d
```  

*NOTE: Use --build command with the start command to reflect any future **.env** changes eg : `docker-compose -f docker-compose.yml up --build -d`*

#### To stop the application

Run
```       
docker-compose stop
```  

## Health Check

The application should be up and available for use at port defaulted to 8090 in http://localhost:8090/api/robot/healthcheck. By this step docker based installation is completed.


