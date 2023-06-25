# Base image with Maven for building the Kafka Streams application
FROM maven:3.8.4-openjdk-11 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Kafka Streams application source code to the container
COPY . .

# Build the Kafka Streams application
RUN mvn clean package -DskipTests

# Base image with Java for running the Kafka Streams application
FROM openjdk:11-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Kafka Streams application JAR file from the builder stage to the container
COPY --from=builder /app/target/kafka-streams-app.jar .

# Set the entrypoint command to run the Kafka Streams application
CMD ["java", "-jar", "kafka-streams-app.jar"]
