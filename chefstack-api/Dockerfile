# build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

# runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/chefstack-api-1.0.0.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8080} -jar app.jar"]
