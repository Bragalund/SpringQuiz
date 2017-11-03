package no.group3.springQuiz.quiz.model.repository

import no.group3.springQuiz.quiz.model.entity.Category
import no.group3.springQuiz.quiz.model.entity.Question
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

/**
 * Created by josoder on 18.10.17.
 */
@Repository
interface QuestionRepository : CrudRepository<Question, Long>, QuestionRepositoryCustom {
}

@Transactional
interface QuestionRepositoryCustom {
    // Used
    fun update(id:Long,
               name: String): Boolean
}


open class QuestionRepositoryImpl : QuestionRepositoryCustom {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun update(id: Long,
                        newText: String): Boolean {

        var question = em.find(Question::class.java, id) ?: return false
        question.questionText = newText
        return true
    }

}
