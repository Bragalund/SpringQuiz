package no.group3.springQuiz.quiz.model.dto

import io.swagger.annotations.ApiModelProperty
import no.group3.springQuiz.quiz.model.entity.Category
import no.group3.springQuiz.quiz.model.entity.Question


/**
 * Created by josoder on 18.10.17.
 */
data class CategoryDto(
        @ApiModelProperty("The category id")
        var id : Long? = null,
        @ApiModelProperty("")
        var name : String = "",
        @ApiModelProperty("Questions that belongs to this category")
        var questions : List<QuestionDto> = emptyList())

class CategoryConverter {
    companion object {
        fun transform(entity: Category): CategoryDto {
            return CategoryDto(
                    id = entity.id!!,
                    name = entity.name!!,
                    questions = QuestionConverter.transform(entity.questions!!)
            )
        }

        fun transform(entities: Iterable<Category>): List<CategoryDto> {
            return entities.map { transform(it) }
        }
    }
}