#Home-dev

This package created for local development.


##How to run
1. You should create environmental variables that are defined in application.properties file.
2. You should define Java and Maven in your PATH.
3. You should create database and local registry. You can see how to do it below in [Docker](#Docker) section.
4. If you did everything right, you can build and launch the project. How to do it via Docker, see the [Docker](#Docker) section below.
5. If you did everything correctly, you should be able to access RapiDoc by this URL: http://localhost:8080/api/0/apidocs/index.html


##Docker
**How to build**

For building project you should create your local registry where maven will put project images. 
For this use `docker-compose up` command in your terminal in `init` package. 
After that you will have your local registry, database and possibility successfully build the project.


**How to launch**

After successful building you can launch the project in Docker. For this just type `docker-compose pull && docker-compose up` command as mentioned above but in `launch` package.
After that you will have data-migration container for filling database and application container.


**How to close**

To close the application just type `docker-compose down` in `launch` package to stop and remove data migration and application containers.