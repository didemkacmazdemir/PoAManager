FROM openjdk:17-jdk-slim
MAINTAINER rabobank.nl
COPY api/target/rabobank-assignment-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]