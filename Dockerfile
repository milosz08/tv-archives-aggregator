FROM eclipse-temurin:17-jdk-alpine AS build

ENV BUILD_DIR=/build/tv-archives-aggregator

RUN mkdir -p $BUILD_DIR
WORKDIR $BUILD_DIR

# copy only maven-based resources for optimized caching
COPY .mvn $BUILD_DIR/.mvn
COPY mvnw $BUILD_DIR/mvnw
COPY pom.xml $BUILD_DIR/pom.xml
COPY app-backend/pom.xml $BUILD_DIR/app-backend/pom.xml
COPY app-frontend/pom.xml $BUILD_DIR/app-frontend/pom.xml
COPY app-scrapper/pom.xml $BUILD_DIR/app-scrapper/pom.xml

RUN chmod +x $BUILD_DIR/mvnw
RUN cd $BUILD_DIR

RUN ./mvnw dependency:tree -pl app-frontend,app-backend

# copy rest of resources
COPY app-backend $BUILD_DIR/app-backend
COPY app-frontend $BUILD_DIR/app-frontend
COPY docker $BUILD_DIR/docker

RUN ./mvnw clean
RUN ./mvnw package -pl app-frontend,app-backend

FROM eclipse-temurin:17-jre-alpine

ENV BUILD_DIR=/build/tv-archives-aggregator
ENV ENTRY_DIR=/app/tv-archives-aggregator
ENV JAR_NAME=tv-archives-aggregator-client.jar

WORKDIR $ENTRY_DIR

COPY --from=build $BUILD_DIR/.bin/$JAR_NAME $ENTRY_DIR/$JAR_NAME
COPY --from=build $BUILD_DIR/docker/entrypoint $ENTRY_DIR/entrypoint

RUN sed -i \
  -e "s/\$JAR_NAME/$JAR_NAME/g" \
  entrypoint

RUN chmod +x entrypoint

LABEL maintainer="Miłosz Gilga <personal@miloszgilga.pl>"

EXPOSE 8080
ENTRYPOINT [ "./entrypoint" ]
