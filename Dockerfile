FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/lucky-king-1.0.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]