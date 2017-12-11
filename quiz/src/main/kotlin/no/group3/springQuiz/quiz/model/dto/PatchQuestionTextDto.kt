package no.group3.springQuiz.quiz.model.dto

import io.swagger.annotations.ApiModelProperty

/**
 * Created by josoder on 08.11.17.
 */
data class PatchQuestionTextDto(
        @ApiModelProperty("The new updated text")
        var updatedText : String? = null
)