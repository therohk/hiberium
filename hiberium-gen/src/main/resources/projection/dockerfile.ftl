FROM openjdk:11

ENV APPDIR='/opt/app'
COPY build/libs/${project_name}-${artifact_version}.war /opt/app/app.war
WORKDIR /opt/app/

EXPOSE 8080

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime

ENTRYPOINT ["java", "-jar", "/opt/app/app.war", "${package_base}.Application"]
