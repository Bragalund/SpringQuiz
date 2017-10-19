package no.group3.springQuiz.quiz.model.entity

import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by josoder on 18.10.17.
 */
@Entity
class Question(
        @get: Id @get: GeneratedValue
        var id : Long? = null,
        @get : NotEmpty
        var questionText : String? = null,
        @get: ElementCollection
        var answers : List<String>? = null,
        var correctAnswers : Int? = null,
        @get: ManyToOne
        var category : Category? = null
)