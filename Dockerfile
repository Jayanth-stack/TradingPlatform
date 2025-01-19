# Use Java 17 (LTS version)
FROM eclipse-temurin:17-jdk-alpine

# Add Maintainer Info
LABEL maintainer="jayanthalapati@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available
EXPOSE 8080

# Update the JAR_FILE argument to match your actual jar name
# This should match your pom.xml or build.gradle artifact name
ARG JAR_FILE=target/trading-platform-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]