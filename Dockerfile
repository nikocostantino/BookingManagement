# Usa un'immagine base per Java 21
FROM eclipse-temurin:21-jdk-jammy

# Imposta la directory di lavoro dentro il container
WORKDIR /app

# Copia il file .jar generato nel container
COPY target/*.jar app.jar

# Esponi la porta usata dalla tua applicazione
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]