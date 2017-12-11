package no.group3.springQuiz.highscore.model.dto

import no.group3.springQuiz.highscore.model.entity.Score

/**
 * Created by johannes on 11.12.2017.
 */
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