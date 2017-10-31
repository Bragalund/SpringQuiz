package no.group3.springQuiz.quiz.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.group3.springQuiz.quiz.model.dto.QuestionConverter
import no.group3.springQuiz.quiz.model.dto.QuestionDto
import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.repository.CategoryRepository
import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

/**
 * Created by josoder on 27.10.17.
 */
@Api(value = "/questions", description = "API for questions.")
@RequestMapping(
        path = arrayOf("/questions"),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
)
@RestController
@Validated
class QuestionController{
    @Autowired
    lateinit var questionRepository : QuestionRepository

    @Autowired
    lateinit var categoryRepository : CategoryRepository

    @ApiOperation("Retrieve all questions")
    @GetMapping
    fun get(): ResponseEntity<List<QuestionDto>> {
        return ResponseEntity.ok(QuestionConverter.transform(questionRepository.findAll()))
    }



    @ApiOperation("Create a question")
    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(code = 201, message = "The id of created question")
    fun createCategory(
            @ApiParam("Json representation of the question to create")
            @RequestBody
            dto: QuestionDto): ResponseEntity<Long> {
        if (dto.id != null) {
            // 400, client made an error
            return ResponseEntity.status(400).build()
        }

        if (dto.questionText == null || dto.answers == null || dto.correctAnswer == null)  {
            return ResponseEntity.status(400).build()
        }

        /*
        val category = categoryRepository.findOne(dto.subCategoryId!!.toLong())
        if (category == null) {
            return ResponseEntity.status(400).build()
        }
        */

        val question: Question?
        try {
            question = questionRepository.save(Question(questionText =  dto.questionText!!, answers = dto.answers,
                    correctAnswers = dto.correctAnswer))
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }


        questionRepository.save(question)

        return ResponseEntity.status(201).body(question.id)
    }
}