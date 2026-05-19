FROM amazoncorretto:21.0.11
WORKDIR /user-service
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]