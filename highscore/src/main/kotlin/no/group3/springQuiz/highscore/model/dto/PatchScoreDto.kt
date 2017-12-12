package no.group3.springQuiz.highscore.model.dto

import io.swagger.annotations.ApiModelProperty

data class PatchScoreDto(
        @ApiModelProperty("username to patch")
        var user : String? = null,

        @ApiModelProperty("The new score")
        var score : Int? = null
)
