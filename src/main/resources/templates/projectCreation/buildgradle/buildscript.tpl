buildscript {
    {{#IS_SPRING_BOOT}}
    ext {
        springBootVersion = '{{SPRING_BOOT_VERSION}}'
    }
    {{/IS_SPRING_BOOT}}
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.netflix.nebula:nebula-project-plugin:4.0.1'
        classpath 'com.palantir:jacoco-coverage:0.4.0'
        {{#IS_SPRING_BOOT}}
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        {{/IS_SPRING_BOOT}}
    }
}