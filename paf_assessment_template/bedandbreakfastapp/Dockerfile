FROM openjdk:21-bookworm AS builder
WORKDIR /src

COPY .mvn .mvn
COPY src src
COPY mvnw .
COPY pom.xml .

RUN ./mvnw package -Dmaven.test.skip=true

FROM openjdk:21-bookworm

WORKDIR /app

COPY --from=builder src/target/assessment-0.0.1-SNAPSHOT.jar app.jar
ENV PORT=8080
ENV SPRING_DATA_MONGODB_DATABASE=bedandbreakfast
ENV SPRING_DATA_MONGODB_URI=

ENTRYPOINT SERVER_PORT=${PORT} java -jar ./app.jar