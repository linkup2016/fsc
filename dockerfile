# Step 1: Use a base image with Java installed
FROM openjdk:21-jdk-slim

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the application's JAR file into the container
COPY target/fsc-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the application's port
EXPOSE 8080

# Step 5: Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
