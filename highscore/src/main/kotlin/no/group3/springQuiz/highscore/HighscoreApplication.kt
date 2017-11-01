package no.group3.springQuiz.highscore

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class HighscoreApplication

fun main(args: Array<String>) {
    SpringApplication.run(HighscoreApplication::class.java, *args)
}
