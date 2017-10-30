package no.group3.user.model.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Password(

        @get: Id @get: GeneratedValue
        var passwordId: String,

        var passwordHash: String,

        var salt: String
)