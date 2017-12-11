package no.group3.springQuiz.quiz.model.entity

import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * Created by josoder on 18.10.17.
 */
@Entity
class Category(
        @get: Id @get: GeneratedValue
        var id : Long? = null,
        @get: NotNull @get: NotEmpty
        var name : String? = null,
        @get: OneToMany(mappedBy = "category")
        var questions : MutableList<Question> = ArrayList()
)