# Use an official Maven image as the base image
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app


COPY pom.xml .

COPY src ./src

RUN mvn clean install -DskipTests

FROM openjdk:21-ea-2-jdk-slim

WORKDIR /app
COPY --from=build /app/target/hopital-1.0.jar .
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "hopital-1.0.jar"]
