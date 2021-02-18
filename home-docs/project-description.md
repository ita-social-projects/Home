## About the HOME project
Home project uses design-first development approach.
That means that we stick to the OpenApi 3.0 specification.
To simplify, get rid of boilerplate code and speed up the development of the server (and client) side, project uses OpenAPI Generator Maven plugin.
MapStruct is also used to generate converters for DTOs.  
To run code generation you simply can build the application with Maven command `mvn clean install`.
Controllers, DTOs and converters will be generated after that. 

### Continuous integration and deployment
Project uses GitHub actions for CI. 
Each commit in Pull Request is checked by CI using `mvn clean install`.
Merging in the `dev` branch provokes CI to build artifacts, then updated `dev` is deployed to Heroku.


## Technologies and tools used in the project
- Java 11
- Maven
- JAX-RS
- Spring Boot
- PostgreSQL
- GitHub Actions
- Heroku
- Docker
