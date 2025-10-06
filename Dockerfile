# Temel JDK image
FROM eclipse-temurin:17-jdk-alpine

# Çalışma dizini
WORKDIR /app

# Jar dosyasını kopyala
COPY target/hospitalappointmantsystem-1.0.0.jar app.jar

# Uygulamayı çalıştır
ENTRYPOINT ["java","-jar","/app/app.jar"]
