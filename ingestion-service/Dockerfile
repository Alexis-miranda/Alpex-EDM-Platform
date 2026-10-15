
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
# Compilamos el archivo JAR saltándonos los test para agilizar el despliegue Cloud
RUN ./mvnw clean package -DskipTests

# Creamos el contenedor final de producción
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV TZ=Europe/Zurich

# Exponemos el puerto de la ingesta
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
