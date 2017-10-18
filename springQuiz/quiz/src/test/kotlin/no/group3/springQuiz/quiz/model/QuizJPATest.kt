package no.group3.springQuiz.quiz.model

import no.group3.springQuiz.quiz.model.entity.Quiz
import no.group3.springQuiz.quiz.model.repository.QuizRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by josoder on 18.10.17.
 */
@RunWith(SpringRunner::class)
@DataJpaTest
class QuizJPATest {
    @Autowired
    lateinit var quizRepository : QuizRepository

    @Test
    fun createQuiz() {
        var quiz : Quiz = Quiz()
        val savedQuiz = quizRepository.save(quiz)
    }
}