package no.group3.springQuiz.quiz.model.dto

import io.swagger.annotations.ApiModelProperty
import no.group3.springQuiz.quiz.model.entity.Quiz
import javax.persistence.Id

/**
 * Created by josoder on 18.10.17.
 */
data class QuizDto(
        @ApiModelProperty("The quiz id")
        var id : Long? = null,
        @ApiModelProperty("List of questions in this quiz")
        var questions: List<QuestionDto>? = null,
        @ApiModelProperty("Number between 1-3, representing difficulty of this quiz")
        var difficulty : Int? = null,
        @ApiModelProperty("The name of the category this quiz belongs to")
        var category: String? = null
)

class QuizConverter {
    companion object {
        fun transform(entity: Quiz): QuizDto {
            return QuizDto(
                    id = entity.id,
                    questions = QuestionConverter.transform(entity.questions!!),
                    difficulty = entity.difficulty,
                    category = entity.category
                    )
        }

        fun transform(entities: Iterable<Quiz>): List<QuizDto> {
            return entities.map { transform(it) }
        }
    }
}