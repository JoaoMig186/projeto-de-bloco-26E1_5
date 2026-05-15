FROM amazoncorretto:21.0.11

WORKDIR /discovery-server

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]