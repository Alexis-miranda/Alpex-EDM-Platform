FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
# COPY ALL FILES INCLUDING MAVEN WRAPPER AND POM
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV TZ=Europe/Zurich

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
