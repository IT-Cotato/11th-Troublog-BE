FROM eclipse-temurin:21-jdk

COPY build/libs/backend-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]