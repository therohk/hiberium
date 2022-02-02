FROM postgres:14

COPY ./src/main/resources/database/*.sql /docker-entrypoint-initdb.d/
