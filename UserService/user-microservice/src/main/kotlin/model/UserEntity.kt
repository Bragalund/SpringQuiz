package model

import javax.persistence.Entity

@Entity
class UserEntity(

        var userid: String,

        var username: String
)