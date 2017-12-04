FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/quiz-0.0.1-SNAPSHOT.jar quiz.jar
ENTRYPOINT exec java -jar quiz.jar /quiz.jar