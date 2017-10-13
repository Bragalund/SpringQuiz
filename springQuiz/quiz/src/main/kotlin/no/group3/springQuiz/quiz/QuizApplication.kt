package no.group3.springQuiz.quiz

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class QuizApplication

fun main(args: Array<String>) {
    SpringApplication.run(QuizApplication::class.java, *args)
}
