## What you need to know about the project

### Starting the project locally
When you have successfully cloned the project, you have to do some steps to start the application.
Make sure you have Java 11.
Firstly, you need to create environmental variables that are defined in `application.properties` file.
Secondly, you need to use Liquibase to run the database migration for the application.
Finally, build the project using `mvn clean install`.  
Now you can run it.

### Continuous integration and deployment
Project uses GitHub actions for CI. 
Each commit in Pull Request is checked by CI using `mvn clean install`.
Merging in the `dev` branch provokes CI to build artifacts, then updated `dev` is deployed to Heroku.

### Issues that you may face
#### Generated classes are not visible in project.  
Solution: in IDE mark packages <br> `home-server-generator/target/generated-sources/openapi/server/src/gen/java/main` <br>
`home-server-generator/target/generated-sources/openapi/server/src/main/java/com/softserveinc/ita/homeproject/api` <br>
`home-service/target/generated-sources/annotations` <br>
 as `Generated sources root`.
