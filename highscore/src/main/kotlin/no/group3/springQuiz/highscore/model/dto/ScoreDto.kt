package no.group3.springQuiz.highscore.model.dto

import io.swagger.annotations.ApiModelProperty
import no.group3.springQuiz.highscore.model.entity.Score

/**
 * Created by johannes on 01.11.2017.
 */

data class ScoreDto(
        @ApiModelProperty("The category id")
        var id : Long? = null,
        @ApiModelProperty("username")
        var user : String = "",
        @ApiModelProperty("The score")
        var score : Int)

class ScoreConverter{
    companion object {
        fun transform(entity: Score): ScoreDto {
            return ScoreDto(
                    id = entity.id!!,
                    user = entity.user!!,
                    score = entity.score!!)
        }

        fun transform(entities: Iterable<Score>): List<ScoreDto> {
            return entities.map { transform(it) }
        }
    }
}
