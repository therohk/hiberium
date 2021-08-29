
apply plugin: 'java'
apply plugin: 'idea'
apply plugin: 'war'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = '${package_base}'
version = '${artifact_version}'
sourceCompatibility = 1.11

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.4.4")
    }
}

sourceSets {
    main {
        resources {
            exclude '**/database/*'
            exclude '**/elastic/*'
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compile('org.springframework.boot:spring-boot-starter-web') {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-tomcat'
    }
    compile('org.springframework.boot:spring-boot-starter-jdbc')
    compile('org.springframework.boot:spring-boot-starter-data-jpa')
    compile('org.springframework.boot:spring-boot-starter-validation')
    compile('org.springframework.boot:spring-boot-starter-actuator')

    compile("javax.xml.bind:jaxb-api:2.3.0")

    //hibernate
    compile("org.hibernate:hibernate-core:5.4.29.Final")
    compile("org.hibernate:hibernate-entitymanager:5.4.29.Final")

    //utilities
    compile("net.sf.supercsv:super-csv:2.4.0")
    compile("org.apache.commons:commons-lang3:3.12.0")

    //security libraries
//    compile('org.springframework.boot:spring-boot-starter-security')
//    compile('org.springframework.boot:spring-boot-starter-oauth2-client')
//    compile('org.thymeleaf.extras:thymeleaf-extras-springsecurity5')
//    compile('org.springframework.boot:spring-boot-starter-thymeleaf')
//    compile("net.sourceforge.nekohtml:nekohtml:1.9.21")
    compile("io.jsonwebtoken:jjwt:0.9.1")

    compile("org.webjars:bootstrap:3.3.7")
    compile("org.webjars:jquery:3.2.1")

    compile("io.springfox:springfox-swagger2:2.9.2")
    compile("io.springfox:springfox-swagger-ui:2.9.2")

    //annotations
    providedCompile("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")

    //embedded server
    compile('org.springframework.boot:spring-boot-starter-${server_type!tomcat}')

    //default database
    runtimeOnly("com.h2database:h2")

    testCompile('org.springframework.boot:spring-boot-starter-test')

}