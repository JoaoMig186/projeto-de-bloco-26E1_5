FROM amazoncorretto:21.0.11
WORKDIR /api-gateway
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]