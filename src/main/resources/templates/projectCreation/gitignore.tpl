{{#IS_NODE}}
node_modules/
dist/
coverage/
.nyc_output/
{{/IS_NODE}}
{{#IS_REACT}}
node_modules/
dist/
coverage/
.nyc_output/
{{/IS_REACT}}
{{#IS_JAVA_BASED}}
.gradle
!gradle/wrapper/gradle-wrapper.jar
bin/
### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans

### NetBeans ###
nbproject/private/
build/
nbbuild/
dist/
nbdist/
.nb-gradle/
{{/IS_JAVA_BASED}}
/build/
### Fortify ###
*.fpr
### Mac OSX ###
.DS_Store

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr