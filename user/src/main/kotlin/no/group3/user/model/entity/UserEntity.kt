package no.group3.user.model.entity

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class UserEntity(

        @get: Id @get: GeneratedValue
        var UserId: String,

        @get: NotEmpty
        var UserName: String,

        var FirstName: String,
        var SurName: String,

        @get: Email
        var Email: String
)