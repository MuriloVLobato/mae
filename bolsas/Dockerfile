# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jre

WORKDIR /app

ARG JAR_FILE=target/bolsas-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
