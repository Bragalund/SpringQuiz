package no.group3.springQuiz.highscore.model.dto

import io.swagger.annotations.ApiModelProperty


/**
 * Created by johannes on 01.11.2017.
 */

data class ScoreDto(
        @ApiModelProperty("The category id")
        var id : Long? = null,

        @ApiModelProperty("username")
        var user : String? = null,

        @ApiModelProperty("The score")
        var score : Int? = null
)





