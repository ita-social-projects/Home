# How to run home-api-tests module guide.

There are several options on how to run home-api-tests.

- Use terminal command 'mvn clean install' with api-tests and api-tests-infrastructure profiles. 
  Also path database and application admin-user credentials.
Terminal command should look like this:
  
  `mvn clean install
  -Pjacoco
  -Papi-tests-infrastructure
  -Papi-tests
  -Dpostgres.user={your main postgres superuser}
  -Dpostgres.password={your postgres superuser password}
  -Dhome.application.admin.username={your application admin username}
  -Dhome.application.admin.password={your application admin password}`

  
- Run home-application, add mentioned above variables to your Junit template VM options,
  
  as well as your home-application external port.
  This will allow to run each test separately.
  Don't forget to run home-data-migration after making database schema changes.
  Junit template VM options should look like this:
  
  `-ea 
  -Dhome.application.external.port={your home-application external port} 
  -Dpostgres.user={your postgres superuser} 
  -Dpostgres.password={your postgres superuser password} 
  -Dhome.application.admin.username={your application admin username}
  -Dhome.application.admin.password={your application admin password}`