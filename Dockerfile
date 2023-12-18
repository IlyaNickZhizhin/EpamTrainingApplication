FROM maven:3.8.4-openjdk-17-slim AS BUILDER
ARG VERSION=1.0-SNAPSHOT
WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src/

RUN mvn clean package
COPY target/EpamTraningApplication-${VERSION}.jar /build/application.jar

FROM openjdk:17.0.2-slim
WORKDIR /app/

COPY --from=BUILDER /build/application.jar /app/
CMD java -jar /app/application.jar