package no.group3.springQuiz.quiz

import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import org.aspectj.weaver.patterns.TypePatternQuestions
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class QuizApplication{

    /*
    @Bean
    fun init(questionRepository: QuestionRepository) = CommandLineRunner {
        questionRepository.save(Question(questionText = "who am I?", answers = listOf("?", "?", "js", "?"),
                correctAnswers = 2))
    }*/
}

fun main(args: Array<String>) {
    SpringApplication.run(QuizApplication::class.java, *args)
}
