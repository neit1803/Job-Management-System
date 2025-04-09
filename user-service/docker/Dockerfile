FROM openjdk:17-jdk-slim
COPY target/user-service-0.0.1-SNAPSHOT.jar employeeservice.jar
ENTRYPOINT ["java","-jar","/employeeservice.jar"]