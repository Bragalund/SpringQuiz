package no.group3.springQuiz.quiz.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.group3.springQuiz.quiz.model.dto.*
import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.entity.Quiz
import no.group3.springQuiz.quiz.model.repository.CategoryRepository
import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import no.group3.springQuiz.quiz.model.repository.QuizRepository
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal
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

    @Autowired
    lateinit var categoryRepository : CategoryRepository

    @Autowired
    private lateinit var  template: RabbitTemplate

    @Autowired
    private lateinit var fanout: FanoutExchange

    @ApiOperation("Retrieve all quizzes, apply '?category={categoryname}' to get quizzes from one specific category")
    @GetMapping
    fun get(@ApiParam("Category")
            @RequestParam(value="category", required = false)
            category: String?
    ): ResponseEntity<List<QuizDto>> {
        val list = if(category.isNullOrBlank()) {
            quizRepository.findAll()
        } else {
            quizRepository.findByCategory(category!!)
        }

        if(list!=null){
            return ResponseEntity.ok(QuizConverter.transform(list))
        }

        return ResponseEntity.ok().build()
    }

    @ApiOperation("Delete quiz with the given id")
    @DeleteMapping(path = arrayOf("/{id}"))
    fun deleteQuestion(@ApiParam("id of the quiz to delete")
                       @PathVariable("id")
                       pId: String?): ResponseEntity<Any> {
        val id: Long
        try {
            id = pId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if(!quizRepository.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        val q = quizRepository.findOne(id)

        quizRepository.delete(id)
        return ResponseEntity.status(204).build()
    }

    @ApiOperation("Create a quiz")
    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(code = 201, message = "The id of created quiz")
    fun createQuiz(
            @ApiParam("Name of the quiz to create.")
            @RequestBody
            dto: QuizDto): ResponseEntity<Long> {
        // don't want the id from the client
        if (dto.quizId != null || dto.category==null) {
            return ResponseEntity.status(400).build()
        }

        val cat = categoryRepository.findByName(dto.category!!)

        if(cat==null && dto.category != "mixed"){
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
           quiz = quizRepository.save(Quiz(questions = qList, difficulty = dto.difficulty, category = dto.category))
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



    /**
     * This one is a bit tricky, it will create a new message added to the amqp
     */
    @ApiOperation("Check answer from the playing user, and return the score derived from this session")
    @PostMapping(path = arrayOf("/{id}/check"), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(code = 201, message = "answers are submitted")
    fun checkAnswers(@ApiParam("the id of the quiz to submit answers to")
                     @PathVariable("id")
                     pId: String?,
                     @RequestBody
                     @ApiParam("json representation of the answers")
                     dto: AnswersDto): ResponseEntity<ScoreDto> {
        val id: Long
        try {
            id = pId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }

        val quiz = quizRepository.findOne(id)
        var correctAnswers = 0

        if(quiz==null){
            return ResponseEntity.status(403).build()
        }

        if(!dto.answers!!.isNotEmpty() || quiz.questions!=null) {
            if(quiz.questions!!.size != dto.answers!!.size){
                return ResponseEntity.status(400).build()
            }
            var i = 0
            dto!!.answers!!.forEach {
                if(quiz.questions!![i].correctAnswers == it){
                    correctAnswers++
                }

                i++
            }
        }

        val scoreDto = ScoreDto(score = correctAnswers*quiz.difficulty!!, username = dto.username)

        template.convertAndSend(fanout.name, "", scoreDto)

        return ResponseEntity.status(201).body(scoreDto)
    }
}