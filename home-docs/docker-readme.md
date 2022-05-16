## How to run in docker
- This image starts a data-migration for database in docker container:
  `homeacademy/data-migration`


- This image starts an application in docker container:  
  `homeacademy/home-application`

This image starts an authorization server in docker container
  `homeacademy/home-oauth-server`

- This variable allows you to specify datasource URL and database name:
  `DATASOURCE_URL`
  

- These variables allow you to set user and password of your database:
  `DATASOURCE_USER`, `DATASOURCE_PASSWORD`


## How to run data migration in container

 `$ docker pull homeacademy/data-migration`

`$ docker run -e DATASOURCE_URL='jdbc:postgresql://{host_or_ip}:{port}/{db_name}' -e DATASOURCE_USER='{user}' -e DATASOURCE_PASSWORD='{password}' -d homeacademy/data-migration`

## How to launch application in container

`$ docker pull homeacademy/home-application`

`$ docker run -e DATASOURCE_URL='jdbc:postgresql://{host_or_ip}:{port}/{db_name}'-e DATASOURCE_USER='{user}' -e DATASOURCE_PASSWORD='{password}' -d homeacademy/home-application`

## How to launch application in container

`$ docker pull homeacademy/home-oauth-server`

`$ docker run -e DATASOURCE_URL='jdbc:postgresql://{host_or_ip}:{port}/{db_name}'-e DATASOURCE_USER='{user}' -e DATASOURCE_PASSWORD='{password}' -d homeacademy/home-oauth-server`

## Java Heap size configuration inside of container

`-Xms<size>` - Set initial heap size

`-Xmx<size>` - Set maximum heap size	

`-Xss<size>` - Set thread stack size

 Example: `-e JAVA_OPTS="-Xmx300m"`