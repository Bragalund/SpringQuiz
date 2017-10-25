package no.group3.user.model.entity

import javax.persistence.Entity

@Entity
class Password(

        var PasswordHash: String,

        var Salt: String
)