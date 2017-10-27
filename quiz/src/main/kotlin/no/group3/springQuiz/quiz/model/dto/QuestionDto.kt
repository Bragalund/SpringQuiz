package no.group3.springQuiz.quiz.model.dto

import no.group3.springQuiz.quiz.model.entity.Question

/**
 * Created by josoder on 18.10.17.
 */
data class QuestionDto(
        // TODO: swagger annotations
        var id : Long? = null,
        var answers : List<String>? = null,
        var correctAnswer : Int? = null,
        var questionText : String? = null,
        var category : CategoryDto? = null)

class QuestionConverter {
    companion object {
        fun transform(entity: Question): QuestionDto {
            return QuestionDto(
                    id = entity.id,
                    category = null,
                    answers = entity.answers,
                    questionText = entity.questionText
            )
        }

        fun transform(entities: Iterable<Question>): List<QuestionDto> {
            return entities.map { transform(it) }
        }
    }
}