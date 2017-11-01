package no.group3.springQuiz.highscore.model.entity

import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by johannes on 01.11.2017.
 */

@Entity
class Score(
        @get: Id @get: GeneratedValue
        var id : Long? = null,
        @get: NotNull @get: NotEmpty
        var name : String? = null,
        @get: NotNull @get: NotEmpty
        var score : Long? = null
)


