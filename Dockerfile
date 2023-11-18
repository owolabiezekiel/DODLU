FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY target/backend-core-service-1.0.jar backend-core-service-1.0.jar
EXPOSE 9001
CMD ["java","-jar","backend-core-service-1.0.jar"]