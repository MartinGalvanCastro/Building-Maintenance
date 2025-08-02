# ---- Build Stage ----
FROM eclipse-temurin:21-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# Build the application
RUN ./gradlew clean build -x test

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre-alpine AS runtime

# Set working directory
WORKDIR /app

# Copy build artifact from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application with the 'docker' Spring profile
CMD ["java","-jar","app.jar","--spring.profiles.active=docker"]
