FROM openjdk

MAINTAINER Abhai2016

COPY home-application/target/home-application-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
