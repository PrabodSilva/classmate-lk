# Start from a Java 17 base image
FROM eclipse-temurin:17-jdk

# Set the folder inside the container where our app lives
WORKDIR /app

# Copy the Maven wrapper and project files
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src ./src

# Build the app (creates a runnable jar), skipping tests for speed
RUN ./mvnw clean package -DskipTests

# Tell Docker which port this service uses
EXPOSE 8761

# The command that runs when the container starts
CMD ["java", "-jar", "target/eureka-server-0.0.1-SNAPSHOT.jar"]