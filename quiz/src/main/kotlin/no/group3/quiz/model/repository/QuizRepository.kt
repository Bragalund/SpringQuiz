package no.group3.springQuiz.quiz.model.repository

import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.entity.Quiz
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

/**
 * Created by josoder on 18.10.17.
 */
@Repository
interface QuizRepository : CrudRepository<Quiz, Long>, QuizRepositoryCustom {
}

@Transactional
interface QuizRepositoryCustom {
    fun addQuestion(subId: Long, questionId: Long) : Boolean
}

open class QuizRepositoryImpl : QuizRepositoryCustom {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun addQuestion(subId: Long, questionId: Long): Boolean {
        val quiz = em.find(Quiz::class.java, subId) ?: return false

        val question = em.find(Question::class.java, questionId) ?: return false

        quiz.questions!!.add(question)
        return true
    }
}