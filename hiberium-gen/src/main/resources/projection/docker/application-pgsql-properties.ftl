
#for docker pgsql
spring.datasource.driverClassName=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.hibernate.hbm2ddl.auto=validate

spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/${project_schema}
spring.datasource.username=hiberium
spring.datasource.password=password
