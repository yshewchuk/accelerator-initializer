

apply plugin: 'java'
apply plugin: 'idea'
apply plugin: 'eclipse'
apply plugin: 'nebula.facet'
apply plugin: 'jacoco'
apply plugin: 'com.palantir.jacoco-coverage'
{{#IS_SPRING_BOOT}}
apply plugin: 'org.springframework.boot'
{{/IS_SPRING_BOOT}}
{{#IS_SPRING_BOOT2}}
apply plugin: 'io.spring.dependency-management'
{{/IS_SPRING_BOOT2}}