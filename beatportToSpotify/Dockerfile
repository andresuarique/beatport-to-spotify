# Usa la imagen oficial de Maven para compilar el proyecto
FROM maven:3.8.4-openjdk-8-slim AS build

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo pom.xml para descargar las dependencias
COPY pom.xml .

# Copia el resto de los archivos
COPY src src

# Ejecuta la fase de compilación y construcción del proyecto
RUN mvn clean package -DskipTests

# Usa la imagen oficial de Java 8 como base
FROM openjdk:8-jdk-alpine

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR de la aplicación en el contenedor
COPY target/beatportToSpotify-0.0.1-SNAPSHOT.war /app/b2s-java-app.war

# Expone el puerto 8080 (o el puerto que esté utilizando tu aplicación)
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicia
CMD ["java", "-jar", "b2s-java-app.war"]