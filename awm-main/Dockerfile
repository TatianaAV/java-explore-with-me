FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar awm-main.jar
ENTRYPOINT ["java","-jar","/awm-main.jar"]