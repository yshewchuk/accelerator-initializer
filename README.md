# Accelerator Initializer

[![Build Status](https://travis-ci.com/scotiabank/accelerator-initializer.svg)](https://travis-ci.com/scotiabank/accelerator-initializer)

Accelerator Initializer is a service that exposes a simple HTTP interface to initialize project file and directory structures for new projects based on standard templates.

## Vision

The intent of the Accelerator Initializer is to simplify the creation of standard and opinionated projects within an organization.

This provides value in a number of ways:
* Reduces the time it takes for a developer to bootstrap and configure a project that adheres to specific guidelines
* Allows organizations to improve compliance to particular standards through opinionated project configurations

## Running the Application

Execute commands
```
./gradlew bootRun
```

## Generating Projects

Once the application is running, either navigate to the Swagger UI in the browser:
```
http://localhost:8086/swagger-ui.html
```
Or: 
Use a tool to execute an HTTP POST request to
```
http://localhost:8086/api/projects/components/download
```

Including one of the following in the request body:

#### NodeJS Application
```
{
  "name": "node-app",
  "projectKey": "hopper",
  "type": "NODE"
}
```

#### React Application
```
{
  "name": "react-app",
  "projectKey": "hopper",
  "type": "REACT"
}
```

#### Java Spring Boot Application
```
{
  "name": "boot-app",
  "projectKey": "hopper",
  "type": "JAVA_SPRING_BOOT"
}
```

#### Java Spring Boot 2 Application
```
{
  "name": "boot-app-2",
  "projectKey": "hopper",
  "type": "JAVA_SPRING_BOOT_2"
}
```

#### Java Library
```
{
  "name": "java-app",
  "projectKey": "hopper",
  "type": "JAVA_LIBRARY"
}
```

### To Run the Generated Project

Once you've downloaded one of the projects, navigate to the project directory and execute:

#### NodeJS Application
```
npm install
npm run build
npm start
```

#### React Application
```
npm install
npm start
```

#### Spring Boot Application
```
chmod +x gradlew
./gradlew bootRun

```

## Feedback and Questions

Join us on [Slack](https://plato-open-source.slack.com/) by [requesting an invite](https://plato-open-source-slack-invite.herokuapp.com/)

## So... You've Found a Bug...

Before submitting an issue, please do a quick search through the issues to check if it may have already been submitted.

Once you've done that, go ahead and submit an issue. Please include:
* The endpoint you were using
* The HTTP method you were using
* The request body you used
* Any other details you think may be relevant

## Need a Feature?

Either:

Submit a pull request yourself with the feature
* Keep in mind that if the feature is out of the project's scope it may not be accepted

or:

Take a quick look through the [Roadmap](https://github.com/scotiabank/accelerator-initializer/blob/master/ROADMAP.md) and list of issues, and see if it (or something similar) has already been requested.

If not, submit a new issue with a description of the desired feature to be reviewed and potentially prioritized.

## Contribute

See our [Contribution Guide](https://github.com/scotiabank/accelerator-initializer/blob/master/CONTRIBUTING.md)