FROM amazoncorretto:21.0.11
WORKDIR /search-service
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]