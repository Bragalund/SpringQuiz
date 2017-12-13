package no.group3.springQuiz.highscore.model.dto

import io.swagger.annotations.ApiModelProperty

data class PatchScoreDto(
        @ApiModelProperty("The new score")
        var score : Int? = null
)