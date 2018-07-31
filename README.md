## Introduction 

[![Build Status](https://travis-ci.com/scotiabank/accelerator-initializer.svg)](https://travis-ci.com/scotiabank/accelerator-initializer)

This Readme file provides instructions on how to run Accelerator, which enables enterprise-quality development practices for projects, big or small. Accelerator empowers developers to set up highly opinionated projects, designed to include all the modern conveniences of CI/CD without any of the legwork.

Currently, developers call an API + daemon through Swagger, which generates the opinionated project. In the future, this will include additional CI/CD hooks with standard tools like Jenkins and Bitbucket.

## How to run Initializer

execute commands
```
./gradlew bootRun
```

navigate to localhost:8086/swagger-ui.html to download sample projects

use

```
{
  "name": "node-app",
  "projectKey": "hopper",
  "type": "NODE"
}
```
to download a node js application

use
```
{
  "name": "java-app",
  "projectKey": "hopper",
  "type": "JAVA_LIBRARY"
}
```
to download a java library application

use
```
{
  "name": "react-app",
  "projectKey": "hopper",
  "type": "REACT"
}
```
to download a react application 

use
```
{
  "name": "boot-app",
  "projectKey": "hopper",
  "type": "JAVA_SPRING_BOOT"
}
```
to download a spring boot applicaition

use
```
{
  "name": "boot-app-2",
  "projectKey": "hopper",
  "type": "JAVA_SPRING_BOOT_2"
}
```
to download a spring boot application version 2

# How to run downloaded apps
Spring Boot
```
chmod +x gradlew
./gradlew bootRun

```
Node
```
npm install
npm run build
npm start
```
React
```
npm install
npm start
```

