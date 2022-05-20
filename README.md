[![Home](https://i.postimg.cc/dtdVKck6/photo-2021-06-16-16-06-28.jpg)](https://github.com/ita-social-projects/Home)
___

[![Heroku Deploy](https://img.shields.io/website?down_color=red&down_message=heroku%20down&up_color=green&up_message=heroku%20up&url=https%3A%2F%2Fhome-project-academy.herokuapp.com%2Fapi%2F0%2Fapidocs%2Findex.html)](https://home-project-academy.herokuapp.com/api/0/apidocs/index.html)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ita-social-projects-home&metric=alert_status)](https://sonarcloud.io/dashboard?id=ita-social-projects-home)
[![Coverage Status](https://img.shields.io/sonar/coverage/ita-social-projects-home?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/component_measures?id=ita-social-projects-home&metric=coverage&view=treemap)
[![Github Issues](https://img.shields.io/github/issues/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/issues)
[![Pending Pull-Requests](https://img.shields.io/github/issues-pr/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/pulls)
[![Docker Pulls](https://img.shields.io/docker/pulls/homebayraktar/home-application)](https://hub.docker.com/r/homebayraktar/home-application)
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)
___

## Table of Contents

- [About the project](#About-the-project)
- [Installation](#installation)
  - [Required to install](#Required-to-install)
  - [Environment](#Environmental-variables)
  - [How to run local](#How-to-run-local)
    -  [Git flow](#Git-flow)
  - [Docker images](#Docker-images)
- [Documentation](#Documentation)
- [Contributing](#contributing)
- [Team](#Team)
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
  

## Installation
### Environmental variables

First of all, you need to check and if it would any necessary to set environment variables
at `application-home-data.properties` which contains in `home-application/src/main/java/source` module.

```properties
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USER}
spring.datasource.password=${DATASOURCE_PASSWORD}
```
### Required to install
- Java 11
- Docker
- Maven
- PostgreSQL
- IntelliJ IDEA (optional)

### How to run local
__Run with Docker__

- if you compose it first time, you need to change `home_network external -> false`,
  because it would try to use remote one that doesn't exist.
  File is located in `home-dev/launch` package, named `docker-compose.yml`.

```properties
networks:
  home_network:
    external: false
    name: home_network
    driver: bridge
```

- use command `docker-compose up` in package `home-dev/launch` to run application.

__Run with Maven + Intellij IDEA__

- if you don't have `mvn` like environment variable, you need to install it into your environment.
- use `mvn clean install` command in the `project root` directory to build project.

- when you use the command below, you will need to run your docker image named `launch` with container `launch-mailhog`.
- change directory to `'project root'/home-data-migration/target` and use `java -jar home-data-migration-0.0.1-SNAPSHOT.jar --url=jdbc:postgresql://localhost:5432/postgres -u=user -p=password`
  command for connection to your local DB.
- after using this command you will need to choose - `would you like to receive notifications from community?`, there are three ways:
  but you need to choose between: `enter your email` and `skip the request(default one)`.
- run application with your IDEA.

If you did everything correctly, you should be able to access RapiDoc by this
URL: http://localhost:8080/api/0/apidocs/index.html

**Detail info about other running options/running tests, you can read in
[hom-dev](https://github.com/ita-social-projects/Home/tree/dev/home-dev) package.**

### Authorization
After accessing RapiDoc you are able to enter `Cooperation` section where you could `create Cooperation`:
- there isn`t any need to authorize, because in your DB you don't have any created user and basic authentication will be requested any time you try to do something.
- if you are already authenticated - make sure that you are working in a new session without being authorized(invalidate session).

When you have finished all previous steps:
You need to use your personal email for `admin_email` field and change `iban` field to make it unique (simply change a few numbers in it).

In your email inbox or in table `invitations` of your local DB you should be able to find a `registration token`.

Having completed all previous steps you can use the provided registration token in the `registration_token field`.
You can create a new user in the `User section` where you need to use `the same email` as you entered for `admin_email` and change
`password` on your own using for that `registration token`.

**Pay attention that you wouldn't be able to edit your email/password after registration!**

---

### Docker images
Our [images](https://hub.docker.com/u/homeacademy) on Docker Hub:

- ***data-migration*** - this image starts a data-migration for database in docker container

- ***home-application*** - this image starts an application in docker container

---

## Documentation

- You can find OpenApi specification for the
  project [here](https://home-project-academy.herokuapp.com/api/0/apidocs/index.html)
- More [information](https://github.com/ita-social-projects/Home/tree/dev/home-docs) about the project's 
  technologies and modules' description

---

## Contributing
### Git flow

#### Step 1

- **Option 1**
  - 🍴 Fork this repo!

- **Option 2**
  - 👯 Clone this repo to your local machine using command:

`git clone https://github.com/ita-social-projects/Home.git`

#### Step 2

Create your Feature Branch

`git checkout -b 'name_for_new_branch'`

#### Step 3
Make changes and [test](https://github.com/ita-social-projects/Home/blob/dev/home-docs/home-api-tests.md)

#### Step 4
Open a Pull Request with description of changes


---

## Team

- Technical Expert of the project - [@MrScors](https://github.com/MrScors)
- Mentor of SoftServe ITA - [@DzigMS](https://github.com/DzigMS)

Contributors that have worked on this project:
<a href="https://github.com/ita-social-projects/Home/graphs/contributors">
<img src="https://contrib.rocks/image?repo=ita-social-projects/Home" />
</a>

---
## Contact
<a href="mailto:test.ita.emails@gmail.com">test.ita.emails@gmail.com</a>
___

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2021 © <a href="https://softserve.academy/" target="_blank"> SoftServe IT Academy</a>.