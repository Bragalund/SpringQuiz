package no.group3.springQuiz.quiz.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.group3.springQuiz.quiz.model.dto.QuestionConverter
import no.group3.springQuiz.quiz.model.dto.QuestionDto
import no.group3.springQuiz.quiz.model.dto.QuizConverter
import no.group3.springQuiz.quiz.model.dto.QuizDto
import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.entity.Quiz
import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import no.group3.springQuiz.quiz.model.repository.QuizRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

/**
 * Created by josoder on 03.11.17.
 */
@Api(value = "/quizzes", description = "API for quizzes.")
@RequestMapping(
        path = arrayOf("/quizzes"),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
)
@RestController
@Validated
class QuizController {
    @Autowired
    lateinit var questionRepository : QuestionRepository

    @Autowired
    lateinit var quizRepository : QuizRepository

    @ApiOperation("Retrieve all quizzes")
    @GetMapping
    fun get(): ResponseEntity<List<QuizDto>> {
        return ResponseEntity.ok(QuizConverter.transform(quizRepository.findAll()))
    }

    @ApiOperation("Create a quiz")
    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(code = 201, message = "The id of created quiz")
    fun createQuiz(
            @ApiParam("Name of the quiz to create.")
            @RequestBody
            dto: QuizDto): ResponseEntity<Long> {
        // don't want the id from the client
        if (dto.id != null) {
            return ResponseEntity.status(400).build()
        }

        // need to get the question from the db before creating the quiz entity
        val qList: MutableList<Question> = ArrayList()
        // make sure all questions exists(if any)
        if(dto.questions != null && !(dto.questions!!.isEmpty())){
            var valid = true

            dto.questions!!.forEach {
                if(!questionRepository.exists(it.id)) {
                    valid = false
                    return@forEach
                }
                qList.add(questionRepository.findOne(it.id))
            }
            if (!valid) return ResponseEntity.status(400).build()
        }

        var quiz : Quiz
        try {
           quiz = quizRepository.save(Quiz(questions = qList, difficulty = dto.difficulty))
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }

        return ResponseEntity.status(201).body(quiz.id)
    }

    @ApiOperation("Get a single quiz by id")
    @GetMapping(path = arrayOf("/{id}"))
    fun getById(@ApiParam("The id of the quiz")
                @PathVariable("id")
                pId: String?): ResponseEntity<QuizDto> {
        val id: Long
        try {
            id = pId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }

        val quiz = quizRepository.findOne(id) ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(QuizConverter.transform(quiz))
    }

    @ApiOperation("Get all questions of the quiz with the given id")
    @GetMapping(path = arrayOf("/{id}/questions"))
    fun getAllQuestionsById(@ApiParam("The id of the quiz")
                @PathVariable("id")
                pId: String?): ResponseEntity<List<QuestionDto>> {
        val id: Long
        try {
            id = pId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }

        val quiz = quizRepository.findOne(id) ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(QuestionConverter.transform(quiz.questions!!))
    }


}