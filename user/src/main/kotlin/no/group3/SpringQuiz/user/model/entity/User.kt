package no.group3.SpringQuiz.user.model.entity

import org.hibernate.validator.constraints.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name="userdetails")
class User(

        @get: Id @get: GeneratedValue
        var userId: Long? = null,

        @get: NotEmpty @get: NotNull @get: Column(unique = true)
        var userName: String? = null,

        @get: NotEmpty @get: NotNull
        var firstName: String? = null,
        var lastName: String? = null,

        @get: NotEmpty @get: NotNull @get: Email
        var email: String? = null
)