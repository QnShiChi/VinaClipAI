FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
COPY --from=build /workspace/target/backend-0.0.1-SNAPSHOT.jar /app/backend.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]
