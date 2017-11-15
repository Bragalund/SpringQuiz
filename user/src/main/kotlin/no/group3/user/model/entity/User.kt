package no.group3.user.model.entity

import no.group3.user.model.validation.CustomEmail
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
class User(

        @get: Id @get: GeneratedValue
        var userId: Long? = null,

        @get: NotEmpty @get: NotNull
        var userName: String,

        @get: NotEmpty @get: NotNull
        var firstName: String,
        var lastName: String,

        @get: NotEmpty @get: NotNull
        var email: String,

        @get: NotEmpty @get: NotNull
        var passwordHash: String
)