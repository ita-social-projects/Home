FROM adoptopenjdk/openjdk11:latest
COPY home-application/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
