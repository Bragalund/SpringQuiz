package no.group3.springQuiz.quiz.model.dto

import io.swagger.annotations.ApiModelProperty

/**
 * Created by josoder on 11.12.17.
 */
data class AnswersDto(
        @ApiModelProperty("answers submitted by a playing user")
        var answers: Array<Int>? = null,
        @ApiModelProperty("The username of the playing user")
        var username:String? = null)