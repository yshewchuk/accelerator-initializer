

dependencies {
	{{#IS_SPRING_BOOT}}
    compile('org.springframework.boot:spring-boot-starter-web')
    testCompile('org.springframework.boot:spring-boot-starter-test')
    {{/IS_SPRING_BOOT}}
}