FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY src/main/java/com/alpex/golden_copy_service .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV TZ=Europe/Zurich

# Puerto de persistencia Postgres
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
