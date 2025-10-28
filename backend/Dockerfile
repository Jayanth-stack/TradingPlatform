FROM maven:3.9.7-jdk-17-alpine AS build

WORKDIR /app


COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM maven:3.9-eclipse-temurin-17 AS build
VOLUME /tmp
EXPOSE 8080
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]