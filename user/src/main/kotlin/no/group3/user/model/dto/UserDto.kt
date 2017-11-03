package no.group3.user.model.dto

import io.swagger.annotations.ApiModelProperty
import no.group3.user.model.entity.User

data class UserDto(

        @ApiModelProperty("Id of user") //Provides swagger documentation
        var id: Long? = null,

        @ApiModelProperty("username of user")
        var userName: String,

        @ApiModelProperty("First name of user")
        var firstName: String,

        @ApiModelProperty("Last name of user")
        var lastName: String,

        @ApiModelProperty("Email of user")
        var email: String,

        @ApiModelProperty("Password of user")
        var password: String
)

class UserConverter{
        companion object {
                fun transform(entity: User): UserDto{
                        return UserDto(
                                id = entity.userId,
                                userName = entity.userName        ,
                                firstName = entity.firstName,
                                lastName = entity.lastName,
                                email = entity.email,
                                password = entity.passwordHash
                        )
                }

                fun transform(entities: Iterable<User>): List<UserDto> {
                        return entities.map { transform(it) }
                }
        }
}