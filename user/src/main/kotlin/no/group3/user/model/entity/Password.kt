package no.group3.user.model.entity

import javax.persistence.Entity

@Entity
class Password(

        var passwordHash: String,

        var salt: String
)