# Use official Amazon Corretto (Java 17) image
FROM amazoncorretto:17-alpine-jdk

# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/rds-terraform-project-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

