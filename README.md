# Digital Permissions Passenger Trial Project

* [Access the applications here](#access-the-applications-here)
* [General](#general)
* [Building and running locally](#building-and-running-locally)
  * [Pre-requisites](#pre-requisites)
  * [How-to](#how-to)
    * [Setup the databases](#setup-the-databases)
    * [Resetting the databases](#resetting-the-databases)
    * [Run the apps in an IDE](#run-the-apps-in-an-ide)
    * [Build and run the apps without an IDE](#build-and-run-the-apps-without-an-ide)
    * [Admin app credentials](#admin-app-credentials)
* [Build pipeline](#build-pipeline)
  * [Continuous Integration](#continuous-integration)
  * [Deployment](#deployment)
* [Environments](#environments)
  * [Basic inspection of the apps](#basic-inspection-of-the-apps)
  * [App specific config](#app-specific-config)
  * [IP whitelisting](#ip-whitelisting)
  * [dev and qa databases](#dev-and-qa-databases)
      * [Configuration](#configuration)
      * [Connecting to dev and qa databases from your local machine](#connecting-to-dev-and-qa-databases-from-your-local-machine)
      * [Resetting dev and qa databases](#resetting-dev-and-qa-databases)
         * [Resetting passenger](#resetting-passenger)
         * [Resetting accounts](#connecting-to-preprod-and-prod-databases)
  * [preprod and prod databases](#preprod-and-prod-databases)       
      * [Details](#details)
      * [Connecting to preprod and prod databases](#details)             
  * [Email-sending](#email-sending)      
  * [Application heath checks](#application-health-checks)      
      * [admin](#admin)      
      * [public](#public) 
* [Misc](#misc)
  * [Logging](#logging)
  * [Metrics](#metrics)  
  * [Making Drone, Kubernetes, Artifactory and Docker play together](#making-drone-kubernetes-artifactory-and-docker-play-together)
  
## Access the applications here:

### Dev

* https://admin-dev.app-notprod.digitalpermissions.homeoffice.gov.uk/
* https://passenger-dev.app-notprod.digitalpermissions.homeoffice.gov.uk/

### QA

* https://admin-qa.app-notprod.digitalpermissions.homeoffice.gov.uk/
* https://passenger-qa.app-notprod.digitalpermissions.homeoffice.gov.uk/

### Pre-prod

* https://admin-uat.app-notprod.digitalpermissions.homeoffice.gov.uk/
* https://passenger-uat.app-notprod.digitalpermissions.homeoffice.gov.uk/

### prod

TBA.

# General

The source for this project can be found on [GitHub](https://github.com/UKHomeOffice/passenger/passenger.git).

It is hosted on the ```Docker``` and ```Kubernetes``` based Application Container Platform (ACP), much useful documentation for which can be found [here](https://github.com/UKHomeOffice/application-container-platform). 

**New developers, and anyone providing operational support, are highly recommended to go through the ACP [Developer Documentation](https://github.com/UKHomeOffice/application-container-platform/tree/master/developer-docs), which will walk you through setting up the tools and accesses needed for connecting to the platform.** 

The project itself is  a ```maven``` managed project containing the following modules: 
 - ```admin```: a springboot webapp for internal use with functionality for managing the passenger trial
 - ```public```: a springboot webapp with the public-facing passenger trial functionality
 - ```modules```: containing modules of code shared by the webapps, mainly domain model classes
 

Two ```postgres``` databases are required to run the webapps:
 - ```accounts```: contains user-account information for the ```admin``` app
 - ```passenger```: contains passenger-trial data and is accessed by both webapps 

# Building and running locally 

## Pre-requisites

- ```Java 10```
- ```Maven 3+```
- ```Docker```

## How-to

### Setup the databases 

The root of the project contains a ```docker-compose.yml``` file that can be used to make the required database instance available when running the apps locally by executing
```
docker-compose up
```
in the root of the passenger project (make sure Docker is running first).

### Resetting the databases
Flyway is used to manage database changes. The change scripts are run when the ```admin``` app starts.

If you need to reset the state of the DBs locally, run ```docker-compose down -v``` on the root of the project
```
docker-compose down -v
```
followed by
```
docker-compose up
```

### Run the apps in an IDE

If you are using an IDE, such as IntelliJ, the webapps can be run by running the ```AdminApplication``` main class in the ```admin``` module and the ```PassengerApplication``` in the ```public``` module. 
This will make the ```admin``` and ```public``` apps available on 
```
http://localhost:8082
http://localhost:8080
```
respectively.

### Run the integration tests in an IDE

To run the integration tests in a IDE, Postgres must be started before running the tests (and stopped afterwards).  
To start and stop Postgres use the `prepare-*-it-db` execution of the `docker-maven-plugin` within a module.  
For example, for the `admin` app, from within `/admin`, start Postgres with:
```
mvn docker:start@prepare-admin-it-db
```
and stop it with:
```
mvn docker:stop@prepare-admin-it-db
```


### Build and run the apps without an IDE
If you do not have, or do not wish to use, an IDE to run the apps, build the entire project by running
```
mvn clean verify
```
in the root of the project. 

Start the ```admin``` and ```public``` apps by executing

```
java -jar admin/target/admin-0.0.1-SNAPSHOT.jar
java -jar public/target/public-0.0.1-SNAPSHOT.jar
```
in the root of the project, which will then make the apps available on 
```
http://localhost:8082
http://localhost:8080
```
respectively.

To stop the apps, identify the running processes by executing

```
lsof -i:8082 | grep LISTEN
lsof -i:8080 | grep LISTEN
```

respectively, which should output something like
```
java    9427 ........
```

Terminate the process by executing

```
kill -9 <pid> 
```
where <pid> is the number from the line above.

### Admin app credentials
If you are running the ```admin``` for the first time, or you have reset the DBs, you will need to setup credentials to access the app.  Credentials are managed through Keycloak
on the ACP platform.  In your browser, navigate to
```
http://localhost:8082/
```
You will be prompted with a login page.  If you have an O365 account, click the O365 button next to the username and password fields.  
Select either the O365 or Poise buttons to continue dependent upon your network.

After you have done this, you will need a Keycloak administrator to grant the correct role for you before you can use the admin application.   

# Build pipeline

The project is using [Jenkins](https://digperms-jenkins.ci.acp.homeoffice.gov.uk/) to manage its build pipeline.  You need to be on the ACP CI VPN to access it.  You should login via your O365 credentials.

The build-pipeline configuration is within Jenkins.  There is a job for the build and push for the Docker image and another for deployment of each application to the environments. 

For connecting to the VPN, please follow the instructions [here](https://github.com/UKHomeOffice/application-container-platform/blob/master/developer-docs/dev_setup.md#connect-to-the-vpn) if you haven't done this before.

## Continuous Integration

On every ```push``` to ```master```, ```Jenkins``` will

- checkout and build the project using ```maven```
- create ```Docker``` images for the webapps using the ```Dockerfile```s in each module
- push the ```Docker``` images to [Artifactory](https://artifactory.digital.homeoffice.gov.uk/)
- pull and deploy the ```Docker``` images to the [dev environment](#environments) 

## Deployment

To deploy a particular build to a different environment, e.g. ```qa``` you must perform a promotion through Jenkins.
 
 
# Environments

This project at present have four environments set up: ```dev```, ```qa```, ```preprod``` and ```prod``` (although at the time of writing nothing has yet been deployed to ```prod```).

Each environment corresponds to a ```Kubernetes``` namespace: ```dig-perms-dev```, ```dig-perms-qa```, ```dig-perms-preprod``` and ```dig-perms-prod```.
 
```dig-perms-dev```, ```dig-perms-qa```, ```dig-perms-preprod``` are in the ACP ```Kubernetes``` dev cluster; ```dig-perms-prod``` is in the production cluster.

In order to access the non-prod namespaces, please follow the instructions [here](https://github.com/UKHomeOffice/application-container-platform/blob/master/developer-docs/dev_setup.md#configure-kubectl) to obtain a ```Kubernetes``` token.

If you need access to ```dig-perms-prod``` as well, please get your team-leader to add a comment to that effect when you raise the ticket.

## Basic inspection of the apps
Once you have access, verify it works correctly by running e.g the following commands

```
kubectl config use-context dig-perms-dev
kubectl get pods
```
to see the pods in the ```dig-perms-dev``` namespace. The output should look something like
```
NAME                               READY     STATUS    RESTARTS   AGE
accounts-db-757280772-6skzb        1/1       Running   0          14d
passenger-3772179319-597m9         3/3       Running   1          12m
passenger-admin-3443640616-4v1vb   3/3       Running   1          13m
passenger-db-1758054328-hffld      1/1       Running   0          22d
```

Please note that you need to be connected via the dev VPN to access the non-prod namespaces, and the prod VPN to access the prod namespace.

If e.g. an app isn't available after deployment, use the above command to check the corresponding pod is running OK. If if isn't it may be useful to run
```
kubectl describe pod <pod-name> 
```
to get more info.

It may also be useful to look at the logs directly, e.g. to check that the application has started OK, by running e.g 

```
kubectl logs <passenger-pod-name> passenger
kubectl logs <passenger-admin-pod-name> passenger-admin
```

## App specific config

The ```Kubernetes``` config files for the ```admin``` and ```public``` apps are in the ```kube``` subfolders for each module.

These comprise

- ```deployment.yaml``` defining a pod with three containers: 
   - the ```Docker``` image with out webapp listening on ```http``` port ```8080``` or ```8082```
   - a [nginx proxy](https://github.com/UKHomeOffice/docker-nginx-proxy) image acting as a proxy for our app 
- ```service.yaml``` defining a service for the deployment
- ```ingress.yaml``` defining the host names and IP white-listings for the service. Also retrieves a SSL certificate from LetsEncrypt.

## ```dev``` and ```qa``` databases
The ```dig-perms-dev``` and ```dig-perms-qa``` namespaces contain pods with corresponding services for two ```postgres``` images providing the ```accounts``` and ```passenger``` databases for these environments.

### Configuration 
The configurations for these are defined in the following files in the ```kube``` folder in the root of the project.

- ```passenger-db-deployment.yaml``` 
- ```passenger-db-service.yaml``` 
- ```postgres-passenger-data-persistentvolumeclaim.yaml```
- ```accounts-db-deployment.yaml``` 
- ```accounts-db-service.yaml``` 
- ```postgres-accounts-data-persistentvolumeclaim.yaml```
 
### Connecting to ```dev``` and ```qa``` databases from your local machine
Identify the pods containing the relevant db, e.g ```passenger``` by running
```
kubectl get pods
```
which should show something similar to

```
NAME                               READY     STATUS    RESTARTS   AGE
accounts-db-757280772-6skzb        1/1       Running   0          14d
passenger-3772179319-597m9         3/3       Running   1          12m
passenger-admin-3443640616-4v1vb   3/3       Running   1          13m
passenger-db-1758054328-hffld      1/1       Running   0          22d
```

then use port forwarding to a port on your local machine, e.g. ```5348``` 

```
kubectl port-forward passenger-db-1758054328-hffld  5438:5432

```
and use your usual client, i.e IntelliJ or SQuirreL, to connect to ```jdbc:postgresql://localhost:5438/passenger```.

The credentials for the databases are stored as secrets in the namespaces. To view the secrets for ```passenger``` and ```accounts``` respectively, execute
```
kubectl get secret db-nonprod-secrets -o yaml
kubectl get secret accounts-db-nonprod-secrets -o yaml
```

The displayed values are Base64 encoded. Use your usual Base64 encoding/decoding tool to get the actual values.


### Resetting ```dev``` and ```qa``` databases
To reset a database, e.g. in the case of Flyway conflicts, delete the deployment, services, and persistent volume claim

####  Resetting ```passenger```
```
kubectl delete deployment passenger-db
kubectl delete service passenger-db
kubectl delete pvc postgres-passenger-data 
```
and then redeploy by executing the following in the root of the project:
```
export DB_SECRET=db-nonprod-secrets
kd -f kube/postgres-passenger-data-persistentvolumeclaim.yaml -f kube/passenger-db-deployment.yaml -f kube/passenger-db-service.yaml
```

####  Resetting ```accounts```
```
kubectl delete deployment accounts-db
kubectl delete service accounts-db
kubectl delete pvc postgres-accounts-data 
```
and then redeploy by executing the following in the root of the project:
```
export ACCOUNTS_DB_SECRET=accounts-db-nonprod-secrets
kd -f kube/postgres-accounts-data-persistentvolumeclaim.yaml -f kube/accounts-db-deployment.yaml -f kube/accounts-db-service.yaml
```


## ```preprod``` and ```prod``` databases
### Details
The apps in ```dig-perms-preprod``` and ```dig-perms-prod``` use  ```postgres``` RDS instances.

To facilitate connection to these, a ```postgres``` image is deployed in each of these namespaces (see in ```postgres-image-deployment.yaml``` in the ```kube``` subfolder in the project root).

The details of the RDS instance for each of the ```dig-perms-preprod``` and ```dig-perms-prod``` namespaces are stored as ```Kubernetes``` secrets in the respective namespace.

To view the secrets, run

```
kubectl get secret digperms-preprod-rds-access -o yaml
kubectl get secret accounts-db-preprod-secrets -o yaml
```
 
in ```dig-perms-preprod``` and 

```
kubectl get secret digperms-prod-rds-access -o yaml
kubectl get secret accounts-db-prod-secrets -o yaml
```
in ```dig-perms-prod```.

The displayed values are Base64 encoded. Use your usual Base64 encoding/decoding tool to get the actual values.

### Connecting to ```preprod``` and ```prod``` databases

Run ```kubectl get pods``` in the relevant namespace to identify the pod running the ```postgres``` image, e.g.  ```postgres-image-1569034793-n267j```.
SSH into the pod by executing
```kubectl exec -ti <pod-name> -- /bin/sh```

Now use ```psql``` to connect:

```
psql -h <endpoint> -p <port> -U <username> -d <databasename>
```
using the details found in the secrets, and enter password when prompted. 

## Email-sending
The ```admin``` app uses [Notify](https://www.notifications.service.gov.uk/services/d5308254-ebc7-4e20-b3f1-79ddefea46da/dashboard) for email sending.

Please ask Stuart Cullum (Stuart.Cullum@digital.homeoffice.gov.uk) to grant you access if you do not already have access.

An API key for ```Notify``` is passed to the  ```admin``` app as a environment variable by ```Drone``` on deployment; the value is pulled from a ```Kubernetes``` secret called ```notify-secrets```.

In  ```dig-perms-dev```,  ```dig-perms-qa```, and ```dig-perms-preprod```, the API key permits email sending to a ```Notify```-managed whitelist only.

## Application health checks

### ```admin```

The ```admin``` app exposes [Spring actuator endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html) under ```/health```, ```/metrics``` etc.

These endpoints are accessible to any authenticated user.

### ```public```

The ```public``` app exposes [Spring actuator endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html) under context ```/admin``` e.g ```/admin/health``` on port ```10802``` which is not exposed by any ```ingress```.

To view theses endpoints, identify the pod running the ```public``` app by running ```kubectl get pods```, and look for ```passenger-xxx-xxx```,  e.g. ```passenger-3864912795-hvsx0```.

SSH into the pod by executing

```
kubectl exec -ti <pod-name> -- /bin/sh
```

and then e.g.

```
curl <username>:<password>@localhost:10802/admin/health

```
from inside the pod.

The values for the username and password can be found a ```Kubernetes``` secret called  ```admin-secrets```.

# Misc
## Logging
Logging is done via the ACP ELK stack, see e.g. [the following Kibana link](https://kibana.ops.digital.homeoffice.gov.uk/app/kibana#/discover?_g=(refreshInterval:(display:Off,pause:!f,value:0),time:(from:now-30d,mode:quick,to:now))&_a=(columns:!(_source),index:'kubernetes-hod-dsp-dev*',interval:auto,query:(query_string:(analyze_wildcard:!t,query:'%22dig-perms-dev%22%20AND%20exception')),sort:!('@timestamp',desc))) for exceptions in ```dig-perms-dev```. 

## Metrics
Metrics for this project can be found in the ACP [sysdig](https://sysdig.digital.homeoffice.gov.uk/) instance.

Please [raise a ticket](https://github.com/UKHomeOffice/application-container-platform-bau) if you do not have access. 

## Making ```Jenkins```, ```Kubernetes```, ```Artifactory``` and  ```Docker``` play together

During builds, ```Jenkins``` uses a ```Docker``` image to Dockerise our webapps and push them to ```Artifactory```. 

This requires the existence of an ```Artifactory``` robot token, as described [here](https://github.com/UKHomeOffice/application-container-platform/blob/master/how-to-docs/drone-how-to.md#publishing-to-artifactory).

The token currently being used, was issued to Karen Paludan for a robot named ```digperms``` and was added to ```Drone``` using the following command:

```
drone secret add --image=docker digitalpermissions/passenger ARTIFACTORY_TOKEN <token>
```

On deployment to the various ```Kubernetes``` namespaces, ```Kubernetes``` needs authorisation to pull our webapp images from ```Artifactory```. To this end, a ```Kubernetes``` secret has been created as per [here](https://kubernetes.io/docs/reference/generated/kubectl/kubectl-commands#-em-secret-docker-registry-em-)

```
kubectl create secret docker-registry artifactory-ro --docker-server=docker.digital.homeoffice.gov.uk --docker-username=digperms --docker-password=<token> --docker-email=karen.paludan@digital.homeoffice.gov.uk
```

This, together with the following snippet added to the app's ```deployment.yaml``` files
```
 imagePullSecrets:
 - name: artifactory-ro
```

allows  ```Kubernetes``` to successfully pull our images from ```Artifactory```.


## Still to be added: 
- DNS registrations
- Certificates


