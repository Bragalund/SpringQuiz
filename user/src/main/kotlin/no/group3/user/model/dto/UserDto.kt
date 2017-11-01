package no.group3.user.model.dto

import io.swagger.annotations.ApiModelProperty

data class UserDto(
        @ApiModelProperty("Id of user") //Provides swagger documentation
        var id: String? = null,

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