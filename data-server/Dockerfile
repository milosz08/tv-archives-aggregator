FROM maven:3.8.3-eclipse-temurin-17 as build

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

WORKDIR /opt/data-server

COPY pom.xml .
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean install -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine

WORKDIR /opt/data-server

COPY --from=build /opt/data-server/target/data-server-1.0.0.jar /opt/data-server

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/data-server/data-server-1.0.0.jar"]
