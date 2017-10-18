package no.group3.springQuiz.quiz.model.dto

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
{

}