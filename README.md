[![Home](https://i.postimg.cc/dtdVKck6/photo-2021-06-16-16-06-28.jpg)](https://github.com/ita-social-projects/Home)
___

[![Heroku Deploy](https://img.shields.io/website?down_color=red&down_message=heroku%20down&up_color=green&up_message=heroku%20up&url=https%3A%2F%2Fhome-project-academy.herokuapp.com%2Fapi%2F0%2Fapidocs%2Findex.html)](https://home-project-academy.herokuapp.com/api/0/apidocs/index.html)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ita-social-projects-home&metric=alert_status)](https://sonarcloud.io/dashboard?id=ita-social-projects-home)
[![Coverage Status](https://img.shields.io/sonar/coverage/ita-social-projects-home?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/component_measures?id=ita-social-projects-home&metric=coverage&view=treemap)
[![Github Issues](https://img.shields.io/github/issues/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/issues)
[![Pending Pull-Requests](https://img.shields.io/github/issues-pr/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/pulls)
[![Docker Pulls](https://img.shields.io/docker/pulls/homeacademy/home-application)](https://hub.docker.com/r/homeacademy/home-application)
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)
___

## Table of Contents

- [About the project](#About-the-project)
- [Installation](#installation)
  - [Required to install](#Required-to-install)
  - [Environment](#Environmental-variables)
  - [How to run local](#How-to-run-local)
  - [Docker images](#Docker-images)
- [Documentation](#Documentation)
- [Contributing](#contributing)
- [Team](#Team) 
- [Support](#support)
- [Contact](#contact)
- [License](#license)

---

## About the project
__Home__ - is an all-in-one social service that will cover all
aspects of your communication with your home and neighbors.
- Internal notification and news system


- Private messages, chat with selected residents and general OSBB
  group with all residents or OSBB's modules separately


- Residents independently form a budget, determine
  contributions for the maintenance of the house,
  the sequence of solving problems. This makes it possible
  to quickly respond to emergencies, to decide what needs to be
  done in the house or on the adjacent territory in the first place,
  to ensure the protection of the personal and common property of residents


- The ability to choose a service provider such as
  (water, electricity, gas), and pay utility bills


- The best offers and wishes can be implemented in the OSBB
  by residents through internal voting system


- Transparency of expense. The residents' funds go exclusively to the needs
  of their home and are spent rationally. The head of the OSBB reports
  directly to the residents, so they are always aware of what the funds were spent on
  

You more info about the project's technologies and modules' description
  in   <a href="https://github.com/ita-social-projects/Home/tree/dev/home-docs" target="_blank">home-docs</a> package.

## Installation
### Environmental variables
```properties
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USER}
spring.datasource.password=${DATASOURCE_PASSWORD}
spring.mail.username=${EMAIL_ADDRESS}
spring.mail.password=${EMAIL_PASSWORD}
cloud.name=${CLOUD_NAME}
api.key=${API_KEY}
api.secret=${API_SECRET}
```
### Required to install
- Java 11
- Docker
- Maven
- PostgreSQL
- IntelliJ IDEA (optional)

### How to run local
__Run with Docker__
- use `docker-compose up` command `home-dev/init` 
  in package to create  database
- use `docker-compose up` command `home-dev/launch` in package to run 
  application

__Run with Maven + Intellij IDEA__
- use `mvn clean install` command in the root directory to build project
- use `java -jar ./target/home-data-migration-0.0.1-SNAPSHOT.jar --url=jdbc:postgresql://localhost:5432/postgres -u=user -p=password`
  command for connection to your local DB
- run application with your IDEA

If you did everything correctly, you should be able to access RapiDoc
by this URL: http://localhost:8080/api/0/apidocs/index.html

Detail info about local running, or about other running options, you can read in 
[hom-dev](https://github.com/ita-social-projects/Home/tree/dev/home-dev) package.

---

### Docker images
Our [images](https://hub.docker.com/u/homeacademy) on Docker Hub:

- ***data-migration*** - this image starts a data-migration for database in docker container

- ***home-application*** - this image starts an application in docker container

---

## Documentation

- You can find OpenApi specification for the
  project [here](https://home-project-academy.herokuapp.com/api/0/apidocs/index.html).

---

## Contributing
### Git flow
> To get started...
#### Step 1

- **Option 1**
  - 🍴 Fork this repo!

- **Option 2**
  - 👯 Clone this repo to your local machine using command:

`git clone https://github.com/ita-social-projects/Home.git`

#### Step 2

Create your Feature Branch

`git checkout -b name_for_new_branch`

#### Step 3
Make changes and test

#### Step 4
Open a Pull Request with description of changes

---

## Team

- Technical Expert of the project - `@kh0ma`
- Mentor of SoftServe ITA - `@DzigMS`

Developers that have worked on a project during:
<a href="https://github.com/ita-social-projects/Home/graphs/contributors">
<img src="https://contrib.rocks/image?repo=ita-social-projects/Home" />
</a>
## Support


---
## Contact
___

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2020 © <a href="https://softserve.academy/" target="_blank"> SoftServe IT Academy</a>.