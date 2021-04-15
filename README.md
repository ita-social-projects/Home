# Home

“Home” - is an all-in-one social service that will cover all aspects of your communication with your home and neighbors.
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

[![Heroku Deploy](https://img.shields.io/website?down_color=red&down_message=heroku%20down&up_color=green&up_message=heroku%20up&url=https%3A%2F%2Fhome-project-academy.herokuapp.com%2Fapi%2F0%2Fapidocs%2Findex.html)](https://home-project-academy.herokuapp.com/api/0/apidocs/index.html)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ita-social-projects-home&metric=alert_status)](https://sonarcloud.io/dashboard?id=ita-social-projects-home)
[![Coverage Status](https://img.shields.io/sonar/coverage/ita-social-projects-home?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/component_measures?id=ita-social-projects-home&metric=coverage&view=treemap)
[![Github Issues](https://img.shields.io/github/issues/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/issues)
[![Pending Pull-Requests](https://img.shields.io/github/issues-pr/ita-social-projects/Home?style=flat-square)](https://github.com/ita-social-projects/Home/pulls)
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

---

## Table of Contents

- [About the project](#About-the-project)
- [Installation](#installation)
  - [Required to install](#Required-to-install)
  - [Environment](#Environment)
  - [Clone](#Clone)
  - [Setup](#Setup)
  - [How to run local](#How-to-run-local)
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

### Required to install
- Java 11
- Docker
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

### How to run local
About it you can read in `hom-dev` package.

---

## Documentation
- You can find OpenApi specification for the project [here](https://home-project-academy.herokuapp.com/api/0/apidocs/index.html)

---

## Contributing

Our images on Docker Hub

    https://hub.docker.com/u/homeacademy

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

## Support

Reach out to me at one of the following places!

- Website at <a href="http://Website.com" target="_blank">`Website.com`</a>
- Facebook at <a href="https://www.facebook.com/LiubomyrHalamaha/" target="_blank">`Liubomyr Halamaha`</a>
---

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2020 © <a href="https://softserve.academy/" target="_blank"> SoftServe IT Academy</a>.
