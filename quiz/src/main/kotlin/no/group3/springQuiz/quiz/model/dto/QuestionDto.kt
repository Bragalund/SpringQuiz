package no.group3.springQuiz.quiz.model.dto

import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import no.group3.springQuiz.quiz.model.entity.Question

/**
 * Created by josoder on 18.10.17.
 */
data class QuestionDto(
        @ApiModelProperty("Question id")
        var id : Long? = null,
        @ApiModelProperty("List of possible answers")
        var answers : List<String>? = null,
        @ApiModelProperty("Index containing the correct answer (0-3)")
        var correctAnswer : Int? = null,
        @ApiModelProperty("The actual question in text")
        var questionText : String? = null,
        @ApiModelProperty("The category(id) this question belongs to")
        var category : Long? = null)

class QuestionConverter {
    companion object {
        fun transform(entity: Question): QuestionDto {
            return QuestionDto(
                    id = entity.id,
                    category = entity.category!!.id,
                    answers = entity.answers,
                    questionText = entity.questionText
            )
        }

        fun transform(entities: Iterable<Question>): List<QuestionDto> {
            return entities.map { transform(it) }
        }
    }
}