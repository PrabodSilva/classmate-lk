FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 8083
CMD ["java", "-jar", "target/recommendation-service-0.0.1-SNAPSHOT.jar"]