FROM openjdk:21-jdk

ARG JAR_FILE=target/*.jar
COPY $JAR_FILE app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker",  "-jar", "app.jar"]
