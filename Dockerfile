FROM openjdk:17-jdk-alpine
MAINTAINER scaler.com
COPY target/cachingsample-0.0.1-SNAPSHOT.jar docker-cachingsample.jar
ENTRYPOINT ["java","-jar","/docker-cachingsample.jar"]