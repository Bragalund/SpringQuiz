package no.group3.springQuiz.quiz.model.repository

import no.group3.springQuiz.quiz.model.entity.Question
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Created by josoder on 18.10.17.
 */
@Repository
interface QuestionRepository : CrudRepository<Question, Long> {
}