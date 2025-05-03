# ---- STAGE 1: Build ----
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project and build it
COPY . .
RUN mvn clean package -DskipTests

# ---- STAGE 2: Run ----
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
