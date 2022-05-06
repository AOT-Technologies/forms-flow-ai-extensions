FROM maven:3.6.1-jdk-11-slim AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY settings-docker.xml /usr/share/maven/ref/
WORKDIR /tmp/

RUN mvn -s /usr/share/maven/ref/settings-docker.xml dependency:resolve-plugins dependency:resolve dependency:go-offline -B
COPY src /tmp/src/
RUN mvn -s /usr/share/maven/ref/settings-docker.xml package


# Final custom slim java image (for apk command see jdk-11.0.3_7-alpine-slim)
FROM adoptopenjdk/openjdk11:jdk-11.0.3_7-alpine

ENV JAVA_VERSION jdk-11.0.3+7
ENV JAVA_HOME=/opt/java/openjdk \
    PATH="/opt/java/openjdk/bin:$PATH"

EXPOSE 8090

ARG JAR_FILE=camunda-rpa-client.jar 

WORKDIR /opt/app

RUN mkdir -p /opt/robots
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/${JAR_FILE} /opt/app/
COPY src/main/resources/robots/ /opt/robots/
RUN chmod a+rwx -R  /opt/app

ENV ROBOT_BASE_DIR=/opt/robots

RUN apk add bash
RUN apk add --no-cache curl
RUN curl -o rcc https://downloads.robocorp.com/rcc/releases/latest/linux64/rcc
RUN chmod a+x rcc
RUN mv rcc /usr/local/bin/
RUN apk add chromium
RUN apk add chromium-chromedriver

ENTRYPOINT ["java","-jar","/opt/app/camunda-rpa-client.jar"]