FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/distributed-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources /app/resources

RUN mkdir -p /app/logs

VOLUME /app/logs

ENTRYPOINT ["java", "-Dspring.config.additional-location=file:/app/resources/", "-jar", "app.jar"]
