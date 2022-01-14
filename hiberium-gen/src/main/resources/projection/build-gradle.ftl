
apply plugin: 'java'
apply plugin: 'idea'
apply plugin: 'war'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = '${package_base}'
version = '${artifact_version}'
sourceCompatibility = '11'

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.4.4")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web') {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-tomcat'
    }
    implementation('org.springframework.boot:spring-boot-starter-jdbc')
    implementation('org.springframework.boot:spring-boot-starter-data-jpa')
    implementation('org.springframework.boot:spring-boot-starter-validation')
    implementation('org.springframework.boot:spring-boot-starter-actuator')

    //hibernate
    implementation("org.hibernate:hibernate-core:5.4.29.Final")
    implementation("org.hibernate:hibernate-entitymanager:5.4.29.Final")

    //utilities
    implementation("javax.xml.bind:jaxb-api:2.3.0")
    implementation("net.sf.supercsv:super-csv:2.4.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.github.tennaito:rsql-jpa:2.0.2")

    //security libraries
//    implementation('org.springframework.cloud:spring-cloud-starter-vault-config')
//    implementation('org.springframework.boot:spring-boot-starter-security')
//    implementation('org.springframework.boot:spring-boot-starter-oauth2-client')
//    implementation('org.thymeleaf.extras:thymeleaf-extras-springsecurity5')
//    implementation("io.jsonwebtoken:jjwt:0.9.1")

    //ui libraries
    implementation('org.springframework.boot:spring-boot-starter-thymeleaf')
    implementation("net.sourceforge.nekohtml:nekohtml:1.9.21")
    implementation("org.webjars:jquery:3.2.1")
    implementation("org.webjars:bootstrap:4.6.1")
//    implementation("org.webjars:font-awesome:5.15.4")

    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")

    //annotations
    compileOnly("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")

    //embedded server
    implementation('org.springframework.boot:spring-boot-starter-${server_type!"tomcat"}')

    //database
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    //unit testing
    testImplementation('org.springframework.boot:spring-boot-starter-test')

}