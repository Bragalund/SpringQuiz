package no.group3.springQuiz.quiz.model

import no.group3.springQuiz.quiz.model.entity.Category
import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import org.junit.Assert
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
class QuestionJPATest {
    @Autowired
    lateinit var questionRepository : QuestionRepository

    private fun createQuestion(name: String){
        var cat = Category(name = name)
    }

    @Test
    fun testCreateQuestion(){
        var question = Question(questionText = "what?" ,answers = listOf("1", "2", "3", "4"), correctAnswers = 1)

        val savedQuestion = questionRepository.save(question)
        println("id : " + savedQuestion.id)
        Assert.assertNotNull(questionRepository.findOne(savedQuestion.id))
    }
}