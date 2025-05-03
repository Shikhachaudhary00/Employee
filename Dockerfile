FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml from the 'employee' directory
COPY employee/pom.xml .  # Change this line to reflect the correct path

# Run Maven to download dependencies
RUN mvn dependency:go-offline

# Copy the source code from the 'employee/src' folder
COPY employee/src ./src  # Make sure 'employee/src' exists

# Package the application
RUN mvn clean package

# Start the application in the final image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/employee-backend.jar employee-backend.jar
ENTRYPOINT ["java", "-jar", "employee-backend.jar"]
