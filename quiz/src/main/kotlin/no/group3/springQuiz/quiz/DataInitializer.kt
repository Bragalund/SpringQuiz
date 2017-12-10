package no.group3.springQuiz.quiz

import no.group3.springQuiz.quiz.model.entity.Category
import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.entity.Quiz
import no.group3.springQuiz.quiz.model.repository.CategoryRepository
import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import no.group3.springQuiz.quiz.model.repository.QuizRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Created by josoder on 10.12.17.
 * Preload some question and quizzes
 */
@Component
@Profile("default-init", "development")
class DataInitializer(var questionRepository: QuestionRepository,
                      var quizRepository: QuizRepository,
                      var categoryRepository: CategoryRepository) : CommandLineRunner {


    override fun run(vararg p0: String?) {
        val cat = categoryRepository.save(Category(name = "Math"))
        var questions= ArrayList<Question>()

        val quiz = quizRepository.save(Quiz(difficulty = 1))
        questions.add(questionRepository.save(Question(questionText = "2+2", answers = listOf("1", "2", "4", "6"),
                correctAnswers = 3, category = cat)))
        questions.add(questionRepository.save(Question(questionText = "2+3", answers = listOf("5", "2", "4", "6"),
                correctAnswers = 1, category = cat)))
        questions.add(questionRepository.save(Question(questionText = "2+4", answers = listOf("5", "2", "4", "6"),
                correctAnswers = 4, category = cat)))
        questions.add(questionRepository.save(Question(questionText = "2+6", answers = listOf("5", "2", "8", "6"),
                correctAnswers = 3, category = cat)))
        questions.forEach({quiz.questions!!.add(it)})

        questions.forEach({cat.questions!!.add(it)})

        questions = ArrayList<Question>()

        questions.add(questionRepository.save(Question(questionText = "2+6*2", answers = listOf("16", "14", "15", "17"),
                correctAnswers = 2, category = cat)))
        questions.add(questionRepository.save(Question(questionText = "(2+6)*2",
                answers = listOf("16", "14", "15", "17"), correctAnswers = 1, category = cat)))
        questions.add(questionRepository.save(Question(questionText = "2*3^3",
                answers = listOf("36", "18", "18", "17"), correctAnswers = 2, category = cat)))
        questions.add(questionRepository.save(Question(questionText = "2+6*2*3^3",
                answers = listOf("110", "324", "126", "172"), correctAnswers = 1, category = cat)))
        val hardQuiz = quizRepository.save(Quiz(difficulty = 3))

        questions.forEach({
            cat.questions!!.add(it)
            hardQuiz.questions!!.add(it)
        })

        quizRepository.save(quiz)
        quizRepository.save(hardQuiz)
    }

}