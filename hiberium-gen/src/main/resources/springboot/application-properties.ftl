spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
#spring.jpa.hibernate.ddl-auto=validate

spring.jpa.properties.hibernate.jdbc.batch_size=30
spring.jpa.properties.hibernate.order_inserts=true

server.context-path=${context_base}
server.servlet.context-path=${context_base}

logging.level.org.hibernate.SQL=INFO
logging.level.org.hibernate.type=INFO
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
spring.jpa.open-in-view=true

server.servlet.session.timeout=120m
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

spring.thymeleaf.check-template-location=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.content-type=text/html
spring.thymeleaf.cache=false
spring.thymeleaf.mode=LEGACYHTML5
