package no.group3.SpringQuiz.user.model.dto

import io.swagger.annotations.ApiModelProperty

data class PatchDto(

        @ApiModelProperty("First name of user")
        var firstName: String? = null,

        @ApiModelProperty("Last name of user")
        var lastName: String? = null,

        @ApiModelProperty("Email of user")
        var email: String? = null
        )