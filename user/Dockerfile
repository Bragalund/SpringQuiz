FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/user-0.0.1-SNAPSHOT.jar user.jar
ENTRYPOINT exec java -jar user.jar /user.jar