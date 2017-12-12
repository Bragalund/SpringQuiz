FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/highscore-0.0.1-SNAPSHOT.jar highscore.jar
ENTRYPOINT exec java -jar highscore.jar /highscore.jar