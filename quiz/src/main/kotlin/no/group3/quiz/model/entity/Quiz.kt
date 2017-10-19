package no.group3.springQuiz.quiz.model.entity
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.*

/**
 * Created by josoder on 18.10.17.
 */
@Entity
class Quiz(
        @get:Id @get:GeneratedValue
        var id : Long? = null,
        @get: OneToMany
        var questions: MutableList<Question>? = null
        )