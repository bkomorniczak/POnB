FROM openjdk:17-jdk-slim
COPY build/libs/server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
ENV ALGORITHM_TYPE=leader
