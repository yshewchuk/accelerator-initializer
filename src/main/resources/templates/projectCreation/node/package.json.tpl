{
  "name": "{{APP_NAME}}",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "test": "mocha --exit --require babel-register test",
    "start": "node dist/index.js",
    "build": "rimraf dist/ && babel src/ --out-dir dist/ --copy-files",
    "check": "cross-env NODE_ENV=test nyc npm test",
    "prepack": "npm run build"
  },
  "nyc": {
    "require": [
      "babel-register"
    ],
    "all": true,
    "sourceMap": false,
    "instrument": false,
    "include": [
      "src"
    ],
    "reporter": [
      "lcov"
    ]
  },
  "keywords": [],
  "author": "",
  "license": "ISC",
  "dependencies": {
    "express": "^4.16.3"
  },
  "devDependencies": {
    "babel-cli": "^6.26.0",
    "babel-plugin-istanbul": "^4.1.5",
    "babel-preset-env": "^1.6.1",
    "babel-register": "^6.26.0",
    "chai": "^4.1.2",
    "chai-as-promised": "^7.1.1",
    "chai-http": "^3.0.0",
    "cross-env": "^5.1.4",
    "mocha": "^5.0.4",
    "nyc": "^11.6.0",
    "protractor": "^5.3.0",
    "rimraf": "^2.6.2"
  },
  "properties": {
    "artifactory_contextUrl": "http://af.cds.bns/artifactory",
    "artifactory_projectRepoKey": "local-npm-bns",
    "artifactory_user": "",
    "artifactory_password": "",
    "artifactory_npm_repo": "virtual-npm-bns",
    "pcf_app_url": "",
    "cdp_vault_name": "",
    "cdp_environment_name": "",
    "cdp_region_name": "",
    "cdp_vault_clientId": "",
    "cdp_vault_clientSecret": "",
    "pipeline_plugin_version": "1.1.2",
    "sonar_host_url": "http://sonar.agile.bns/",
    "sonar_sources": "src",
    "sonar_tests": "test",
    "sonar_report_paths": "coverage/lcov.info",
    "sonar_language": "js"
  },
  "settings": {
    "rootProject.name": "{{REPO_NAME}}"
  },
  "repository": {
    "type": "git",
    "url": "ssh://git@bitbucket.agile.bns:7999/{{PROJECT_NAME}}/{{REPO_NAME}}"
  }
}
