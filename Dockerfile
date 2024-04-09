# Use the official OpenJDK image as the base image
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build directory to the container
COPY build/libs/*.jar app.jar

# Specify the command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
