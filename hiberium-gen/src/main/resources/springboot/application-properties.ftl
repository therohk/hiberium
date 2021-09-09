spring.datasource.url=jdbc:h2:mem:test;MODE=Mysql;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;INIT=CREATE SCHEMA IF NOT EXISTS ${project_schema}
spring.datasource.username=sa
spring.datasource.password=

spring.datasource.driverClassName=org.h2.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
#spring.jpa.hibernate.ddl-auto=validate
#spring.jpa.hibernate.hbm2ddl.auto=validate
spring.h2.console.enabled=false

spring.datasource.type=com.zaxxer.hikari.HikariDataSource
spring.datasource.hikari.maximum-pool-size=12
spring.datasource.hikari.minimum-idle=4
spring.datasource.hikari.pool-name=hikari-${project_name}

spring.jpa.properties.hibernate.jdbc.batch_size=30
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
spring.jpa.properties.template.model=hiberium
spring.jpa.show-sql=true
spring.jpa.open-in-view=true

server.context-path=${context_base}
server.servlet.context-path=${context_base}

server.port=${server_port!8080}
server.connection-timeout=5s
server.servlet.session.timeout=120m
server.tomcat.max-swallow-size=20MB
server.tomcat.max-http-post-size=20MB
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

management.endpoints.web.exposure.include=health
management.health.db.enabled=true

spring.thymeleaf.check-template-location=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.content-type=text/html
spring.thymeleaf.cache=false
spring.thymeleaf.mode=LEGACYHTML5

logging.level.org.hibernate.SQL=INFO
logging.level.org.hibernate.type=INFO
