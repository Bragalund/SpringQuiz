package no.group3.springQuiz.quiz.model.entity
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min

/**
 * Created by josoder on 18.10.17.
 */
@Entity
class Quiz(
        @get:Id @get:GeneratedValue
        var id : Long? = null,
        @get: OneToMany
        var questions: MutableList<Question>? = ArrayList(),
        @get: Min(1) @get: Max(3)
        var difficulty : Int? = null,
        var category: String? = null
        )