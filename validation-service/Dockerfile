FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY src/main/java/com/alpex/validation_service .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV TZ=Europe/Zurich

#   Strategy validation
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
