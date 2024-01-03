FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . /app
RUN ./mvnw clean package
CMD ["java", "-jar", "target/EurekaServer-0.0.1-SNAPSHOT.jar"]