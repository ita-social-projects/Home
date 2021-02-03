FROM openjdk

MAINTAINER Abhai2016

COPY home-data-migration/target/home-data-migration-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["sh", "-c", "java -jar /app.jar --url=$DATASOURCE_URL"]
