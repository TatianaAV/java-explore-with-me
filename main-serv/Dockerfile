FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar main-serv.jar
ENTRYPOINT ["java","-jar","/main-serv.jar"]