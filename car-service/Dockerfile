FROM amazoncorretto:21.0.11
WORKDIR /car-service
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8086