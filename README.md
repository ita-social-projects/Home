<h2 align="center">Home</h2>

[![Home](https://www.chalets-usa.co.uk/res/Ascent%20Beaver%20Creek%20Colorado%20USA%20self-catered%20apartments.jpg)](https://github.com/ita-social-projects/Home)
___

[![Heroku Deploy](https://img.shields.io/website?down_color=red&down_message=heroku%20down&up_color=green&up_message=heroku%20up&url=https%3A%2F%2Fhome-project-academy.herokuapp.com%2Fapi%2F0%2Fapidocs%2Findex.html)](https://home-project-academy.herokuapp.com/api/0/apidocs/index.html)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ita-social-projects-home&metric=alert_status)](https://sonarcloud.io/dashboard?id=ita-social-projects-home)
[![Coverage Status](https://img.shields.io/sonar/coverage/ita-social-projects-home?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/component_measures?id=ita-social-projects-home&metric=coverage&view=treemap)
[![Github Issues](https://img.shields.io/github/issues/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/issues)
[![Pending Pull-Requests](https://img.shields.io/github/issues-pr/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/pulls)
[![Docker Pulls](https://img.shields.io/docker/pulls/homeacademy/home-application)](https://hub.docker.com/r/homeacademy/home-application)
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

__Home__ - is an all-in-one social service that will cover all aspects of your communication with your home and
neighbors.

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
___

## Table of Contents

- [About the project](#About-the-project)
- [Installation](#installation)
  - [Required to install](#Required-to-install)
  - [Environment](#Environment)
  - [How to run local](#How-to-run-local)
  - [Docker images](#Docker-images)
- [Documentation](#Documentation)
- [Contributing](#contributing)
  - [git flow](#git-flow)
  - [issue flow](#git-flow)
- [FAQ](#faq)
- [Support](#support)
- [Contact](#contact)
- [License](#license)

---

## About the project
- You can read more about the project's technologies and modules' description
  in   <a href="https://github.com/ita-social-projects/Home/tree/dev/home-docs" target="_blank">home-docs</a> package.

## Installation

### Required to install
- Java 11
- Docker
- Maven
- PostgreSQL

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

### How to run local

About it you can read
in [hom-dev](https://github.com/ita-social-projects/Home/tree/dev/home-dev) package.

---

###Docker images
Our [images](https://hub.docker.com/u/homeacademy) on Docker Hub:

- 🐳 ***data-migration*** - this image starts a data-migration for database in docker container

- 🐳 ***home-application*** - this image starts an application in docker container

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

- `@mykytam`
- `@reeztl`
- `@Prosperro-de`
- `@alexorlenko`
- `@omelchenkotimur`
- `@Arthurpanda`
- `@alexorlenko`
- `@Dandria1802`
- `@Romanov-Niko`
- `@PoLytvy`
- `@OlTym`
- `@Abhai2016`
- `@tex1988`
- `@denis-litvinov`
- `@VadymSokorenko`
- `@annaprok`
- `@dd994`
- `@likeRewca`
- `@Ilya-Ross`
- `@mitianin`
- `@BenAvleck`
- `@Vladyslav-Frolov`
- `@AriannaLiss`
- `@kizimov`
- `@IhorSamoshost`
- `@Eternal107`

We are also fortunate to have an
amazing [community of developers](https://github.com/ita-social-projects/Home/graphs/contributors) who help us greatly.

## Support


---
## Contact
___

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2020 © <a href="https://softserve.academy/" target="_blank"> SoftServe IT Academy</a>.