FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . /app
RUN ./mvnw clean package
CMD ["java", "-jar", "target/ReportService-0.0.1-SNAPSHOT.jar"]