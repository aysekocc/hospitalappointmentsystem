# JDK 17 tabanlı imaj
FROM eclipse-temurin:17-jdk-alpine

# Çalışma dizini
WORKDIR /app

# Jar dosyasını kopyala
COPY target/hospitalappointmantsystem-1.0.0.jar app.jar

# Portu expose et (isteğe bağlı ama iyi bir pratik)
EXPOSE 8082

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]
