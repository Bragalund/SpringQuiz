package no.group3.springQuiz.highscore.model.entity

import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
class Score(
        @get: Id @get: GeneratedValue
        var id : Long? = null,
        @get: NotNull
        // cant use user in postgres
        @get: Column(name= "username")
        var user : String? = null,
        @get: NotNull
        var score : Int? = null
)


