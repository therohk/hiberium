FROM openjdk:11

RUN apt-get update && apt-get install -y procps curl net-tools telnet

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime

ARG USER=hiberium
ARG UID=1000
ARG GID=1000

RUN useradd -ms /bin/bash $USER --uid=$UID

USER $UID:$GID
WORKDIR /home/$USER

COPY build/libs/${project_name}-${artifact_version}.war ${project_name}.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "${project_name}.war"]
