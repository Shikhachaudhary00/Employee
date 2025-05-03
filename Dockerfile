FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the Docker image
WORKDIR /app

# Copy the pom.xml from the 'employee' directory to the current working directory in the image
COPY employee/pom.xml .

# Run Maven to download dependencies
RUN mvn dependency:go-offline

# Copy the source code from the 'employee/src' folder to the '/app/src' in the image
COPY employee/src ./src

# Package the application using Maven
RUN mvn clean package

# Use a smaller image to run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file generated in the build stage
COPY --from=build /app/target/employee-backend.jar employee-backend.jar

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "employee-backend.jar"]
