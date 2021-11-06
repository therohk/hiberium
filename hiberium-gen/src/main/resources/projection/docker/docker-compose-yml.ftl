version: "3"
services:
  pgsql:
    build:
      context: .
      dockerfile: Dockerfile-pgsql
    image: pgsql-${project_schema}
    container_name: postgres-local
    volumes:
      - pgsql-data:/var/lib/postgresql/data:rw
    environment:
      - POSTGRES_USER=hiberium
      - POSTGRES_PASSWORD=password
      - POSTGRES_DB=${project_schema}
    ports:
      - "5432:5432"
    networks:
      - dev
  hiberium:
    build: .
    image: ${project_name}
    container_name: ${project_name}-local
    networks:
      - dev
    ports:
      - "8080:8080"
    environment:
      - "SPRING_PROFILES_ACTIVE=local"
    depends_on:
      - pgsql
    links:
      - pgsql

networks:
  dev:

volumes:
  pgsql-data:
