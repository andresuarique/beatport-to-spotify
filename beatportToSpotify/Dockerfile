# Usa la imagen oficial de Maven para compilar el proyecto
FROM maven:3.8.4-openjdk-8-slim AS build

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el directorio de controladores
#COPY drivers /app/drivers

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

# Copia el controlador de Chrome desde tu proyecto al contenedor
COPY drivers/chromedriver /usr/bin/chromedriver
COPY drivers/chromedriver.exe /usr/bin/chromedriver.exe
RUN chmod +x /usr/bin/chromedriver

# Instala dependencias necesarias para Chrome
RUN apk --no-cache add chromium

# Copia el archivo JAR de la aplicación en el contenedor
COPY target/beatportToSpotify-0.0.1-SNAPSHOT.war java-app.war

# Expone el puerto 8080 (o el puerto que esté utilizando tu aplicación)
EXPOSE 8081

# Configura la propiedad del sistema para el controlador de Chrome
ENV CHROME_DRIVER_PATH=/usr/lib/chromium/chromedriver

# Comando para ejecutar la aplicación cuando el contenedor se inicia
CMD ["java", "-jar", "java-app.war"]
