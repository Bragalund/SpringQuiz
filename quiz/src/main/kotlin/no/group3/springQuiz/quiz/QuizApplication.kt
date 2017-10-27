package no.group3.springQuiz.quiz

import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class QuizApplication{
    @Bean
    fun init(questionRepository: QuestionRepository) = CommandLineRunner {

    }
}

fun main(args: Array<String>) {
    SpringApplication.run(QuizApplication::class.java, *args)
}
