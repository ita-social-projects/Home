# Home

“Home” - is an all-in-one social service will cover all aspects of your communication with your home and neighbors.
It includes next modules: 
- Communication with residents of the house, notification
- Suggestions / wishes residents of the house
- Bots for social chats and networks
- Reporting system
- Voting system
- Communal payments
- OSBB module
- Volunteer module to help people with disabilities 
- Parking module


**Badges will go here**

- build status
- coverage
- issues (waffle.io maybe)
- devDependencies
- npm package
- slack
- downloads
- gitter chat
- license
- etc.

[![Build Status](https://img.shields.io/travis/ita-social-projects/Home/master?style=flat-square)](https://travis-ci.org/github/ita-social-projects/Home)
[![Coverage Status](https://img.shields.io/gitlab/coverage/ita-social-projects/Home/master?style=flat-square)](https://coveralls.io)
[![Github Issues](https://img.shields.io/github/issues/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/issues)
[![Pending Pull-Requests](https://img.shields.io/github/issues-pr/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/pulls)
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- For more on these wonderful  badges, refer to <a href="https://shields.io/" target="_blank">shields.io</a>.

---

## Table of Contents

- [About the project](#About-the-project)
- [Installation](#installation)
  - [Required to install](#Required-to-install)
  - [Environment](#Environment)
  - [Clone](#Clone)
  - [Setup](#Setup)
  - [How to run local](#How-to-run-local)
  - [How to run Docker](#How-to-run-Docker)
- [Usage](#Usage)
  - [How to work with swagger UI](#How-to-work-with-swagger-UI)
  - [How to run tests](#How-to-run-tests)
  - [How to Checkstyle](#How-to-Checkstyle)
  - [How to run data migration](#How-to-run-data-migration)
- [Documentation](#Documentation)
- [Contributing](#contributing)
  - [git flow](#git-flow)
  - [issue flow](#git-flow)
- [FAQ](#faq)
- [Support](#support)
- [License](#license)

---

## About the project
- You can read more about the project's technologies and modules' description  in   <a href="https://github.com/ita-social-projects/Home/tree/dev/home-docs" target="_blank">home-docs</a> package.

## Installation

- All the `code` required to get started
- Images of what it should look like

### Required to install
- Java 11
- PostgreSQL
- Maven

### Environment
environmental variables
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

### Clone

- Clone this repo to your local machine using `https://github.com/ita-social-projects/Home`

### Setup

### How to run local
1. You should create environmental variables that are defined in application.properties file.
2. You should create database `home`.
3. If you did everything correctly, you should be able to access RapiDoc by this URL: http://localhost:8080/api/0/apidocs/index.html

### How to run Docker

---

## Usage
### How to work with swagger UI
### How to run tests
### How to Checkstyle
### How to run data migration
You can update DB with this CLI application. For that purpose you should run it from home-data-migration directory in two ways.
1. Use full URL(--url) for connection to your DB
   
   `java -jar ./target/home-data-migration-0.0.1-SNAPSHOT.jar --url=jdbc:postgresql://localhost:5432/?user=user&password=password`
   
2. Use URL(--url), username(-u or --user) and password(-p or --password) for connection to your DB

    `java -jar ./target/home-data-migration-0.0.1-SNAPSHOT.jar --url=jdbc:postgresql://localhost:5432 -u=user -p=password`

- Also, you can write "-h" option to get help. 
  
  `java -jar ./target/home-data-migration-0.0.1-SNAPSHOT.jar -h`

- You can run it with Maven using command:
  
  `mvn clean package -Pliquibase.migration`

---

## Documentation
- You can find OpenApi specification for the project [here](https://home-project-academy.herokuapp.com/api/0/apidoc.html)

---

## Contributing

### Git flow
> To get started...
#### Step 1

- **Option 1**
    - 🍴 Fork this repo!

- **Option 2**
    - 👯 Clone this repo to your local machine using `https://github.com/ita-social-projects/Home.git`

#### Step 2

- **HACK AWAY!** 🔨🔨🔨

#### Step 3

- 🔃 Create a new pull request using <a href="https://github.com/ita-social-projects/Home/compare/" target="_blank">github.com/ita-social-projects/Home</a>.

### Issue flow

---

## Team
- Technical Expert of the project - @kh0ma
- Mentor of SoftServe ITA - @DzigMS

Developers that have worked on a project during:
#### September - December 2020
- @mykytam
- @reeztl
- @Prosperro-de
- @alexorlenko
- @omelchenkotimur
- @Arthurpanda

#### December 2020 - March 2021

- @alexorlenko
- @Dandria1802
- @Romanov-Niko
- @PoLytvy
- @OlTym
- @Abhai2016

## FAQ

---

## Support

Reach out to me at one of the following places!

- Website at <a href="http://Website.com" target="_blank">`Website.com`</a>
- Facebook at <a href="https://www.facebook.com/LiubomyrHalamaha/" target="_blank">`Liubomyr Halamaha`</a>
- Insert more social links here.

---

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2020 © <a href="https://softserve.academy/" target="_blank"> SoftServe IT Academy</a>.
