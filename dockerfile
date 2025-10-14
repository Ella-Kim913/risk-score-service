# Stage 1: Build Stage (Uses a full JDK image to compile and package)
FROM eclipse-temurin:21-jdk-jammy AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven Wrapper files (mvnw and .mvn directory)
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Grant execution rights to the wrapper script
RUN chmod +x mvnw

# Download dependencies
# CHANGE: Use the wrapper script: ./mvnw
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the Spring Boot executable JAR
# CHANGE: Use the wrapper script: ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Production Stage (No changes needed here)
FROM eclipse-temurin:21-jre-jammy AS final

ARG JAR_FILE=target/risk-score-service-0.0.1-SNAPSHOT.jar

COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]