version: "3"
services:
  pgsql:
    image: postgres:14
    container_name: postgres-local
    volumes:
      - pgsql-data:/var/lib/postgresql/data:rw
      - ./resources/database:/docker-entrypoint-initdb.d
    user: pgsql
    environment:
      - POSTGRES_USER=sa
      - POSTGRES_PASSWORD=
      - POSTGRES_DB=${project_schema}
    ports:
      - "5432:5432"
    networks:
      - dev
  hiberium:
    build: .
    container_name: ${project_name}-local
    networks:
      - dev
    ports:
      - "8080:8080"
    environment:
      - "SPRING_PROFILES_ACTIVE=${r"$"}{ENV}"
    depends_on:
      - pgsql

networks:
  dev:

volumes:
  pgsql-data:
