FROM postgres:14

#ENV POSTGRES_USER hiberium
#ENV POSTGRES_PASSWORD password
#ENV POSTGRES_DB ${project_schema}

COPY ./src/main/resources/database/*.sql /docker-entrypoint-initdb.d/
