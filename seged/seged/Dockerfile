# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven descriptor first (leverage layer caching)
COPY pom.xml ./
# If you have modules, copy their poms as needed:
# COPY seged/pom.xml seged/pom.xml

# Download dependencies (will cache unless pom.xml changes)
RUN mvn -q -e -B -DskipTests dependency:go-offline

# Copy the rest of the source
COPY . .

# Build the Spring Boot jar
RUN mvn -q -e -B -DskipTests clean package

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Create nonroot user
RUN useradd -r -s /bin/false spring && mkdir -p /app && chown spring:spring /app

# Copy the built jar from the build stage
# Adjust the path if your module name is different. This works for single-module projects located at this Dockerfile context.
COPY --from=build /app/target/*.jar app.jar

# Expose standard Spring port
EXPOSE 8080

# Mongo/Secret are passed via environment in docker-compose
ENV JAVA_OPTS=""

USER spring
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]