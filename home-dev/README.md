#Home-dev

This package created for local development.

## Table of Contents

  - [How to run](#How-to-run)
  - [Docker](#Docker)
  - [Maven](#Maven)
  - [How to run data migration](#How-to-run-data-migration)
  - [How to run tests](#How-to-run-tests)


## How to run
1. You should define Java and Maven in your PATH.
2. You should build the project. Check [Maven](#Maven) section with **How to build** title.
3. You should create database. You can see how to do it in [Docker](#Docker).
4. If you did everything right, you will be able to launch the project. How to do it with Docker, check the [Docker](#Docker).
How to do it with IDEA and Maven, check the [Maven](#Maven).
5. If you did everything correctly, you should be able to access RapiDoc by this URL: http://localhost:8080/api/0/apidocs/index.html


## Docker


**How to create database**

For that purpose just use `docker-compose up` command in your terminal in `home-dev/init` package. 
After that you will have your container with database.


**How to launch**

After successful building and creating database you can launch the project in Docker. 
For that purpose just type `docker-compose up` command in `home-dev/launch` package.
After that you will have data-migration container for filling database and running application container.


**How to close**

To close the application just type `docker-compose down` in `home-dev/launch` package to stop and remove data migration and application containers.

## Maven

**How to build**

Type in the terminal `mvn clean install` command in the root directory of the project or choose `install` lifecycle of the root module in Maven tab in IDEA.

**How to launch**

If you launch it at first then you have to initialize your database with data-migration script. For this check [How to run data migration](#How-to-run-data-migration).

### How to run data migration
You can update DB with CLI application. For that purpose you should run it from home-data-migration module if you use terminal.
- Use full URL(--url) for connection to your DB

   `java -jar ./target/home-data-migration-0.0.1-SNAPSHOT.jar --url=jdbc:postgresql://localhost:5432/postgres?user=user&password=password`

- Use URL(--url), username(-u or --user) and password(-p or --password) for connection to your DB

    `java -jar ./target/home-data-migration-0.0.1-SNAPSHOT.jar --url=jdbc:postgresql://localhost:5432/postgres -u=user -p=password`

Also, you can write "-h" option to get help. 
  
  `java -jar ./target/home-data-migration-0.0.1-SNAPSHOT.jar -h`

You can run it with Maven using command:
  
  `mvn clean install -Pliquibase.migration`

You can run it with `install` lifecycle of the data-migration module in maven tab in IDEA with turned on `liquibase-migration` profile.

### How to run tests


### Issues that you may face
#### Generated classes are not visible in project.  
Solution: in IDE mark packages <br> `home-server-generator/target/generated-sources/openapi/server/src/gen/java/main` <br>
<br> `home-clients/target/generated-sources/openapi/client/src/gen/java/main` <br>
 as `Generated sources root`.