package no.group3.SpringQuiz.user.model.dto

import io.swagger.annotations.ApiModelProperty
import no.group3.SpringQuiz.user.model.entity.User

data class UserDto(

        @ApiModelProperty("Id of user") //Provides swagger documentation
        var id: Long? = null,

        @ApiModelProperty("username of user")
        var userName: String? = null,

        @ApiModelProperty("First name of user")
        var firstName: String? = null,

        @ApiModelProperty("Last name of user")
        var lastName: String? = null,

        @ApiModelProperty("Email of user")
        var email: String? = null
)

