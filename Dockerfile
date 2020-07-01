FROM openjdk:8-jdk-alpine

ADD ./demo-0.0.1-SNAPSHOT.jar spring-boot-demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-demo.jar"]
