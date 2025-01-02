# Usa un'immagine base per Java 21
FROM eclipse-temurin:21-jdk-jammy AS build

# Copia il codice sorgente nel container
WORKDIR /app
COPY . .

# Aggiungi i permessi di esecuzione per mvnw
RUN chmod +x mvnw

# Compila l'applicazione usando Maven
RUN ./mvnw clean package -DskipTests

# Fase di runtime
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copia il file .jar dalla fase di build
COPY --from=build /app/target/bookingManagement-0.0.1-SNAPSHOT.jar app.jar

# Esponi la porta usata dall'app
EXPOSE 8080

# Comando per avviare l'app
ENTRYPOINT ["java", "-jar", "app.jar"]