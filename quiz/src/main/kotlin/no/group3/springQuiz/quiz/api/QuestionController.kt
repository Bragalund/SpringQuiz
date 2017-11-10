package no.group3.springQuiz.quiz.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.group3.springQuiz.quiz.model.dto.PatchQuestionTextDto
import no.group3.springQuiz.quiz.model.dto.QuestionConverter
import no.group3.springQuiz.quiz.model.dto.QuestionDto
import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.repository.CategoryRepository
import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
        // the id should be auto generated
        if (dto.id != null) {
            // 400, client made an error
            return ResponseEntity.status(400).build()
        }

        if (dto.questionText == null || dto.answers == null || dto.correctAnswer == null || dto.category == null)  {
            return ResponseEntity.status(400).build()
        }

        val category = categoryRepository.findOne(dto.category!!)
        if (category == null) {
            return ResponseEntity.status(400).build()
        }

        val question: Question?
        try {
            question = questionRepository.save(Question(questionText =  dto.questionText!!, answers = dto.answers,
                    correctAnswers = dto.correctAnswer))
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }

        question.category = category
        questionRepository.save(question)
        category.questions.add(question)
        categoryRepository.save(category)

        return ResponseEntity.status(201).body(question.id)
    }

    @ApiOperation("Delete question with the given id")
    @DeleteMapping(path = arrayOf("/{id}"))
    fun deleteQuestion(@ApiParam("id of the question to delete")
               @PathVariable("id")
               pId: String?): ResponseEntity<Any> {
        val id: Long
        try {
            id = pId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if(!questionRepository.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        val q = questionRepository.findOne(id)

        questionRepository.delete(id)
        return ResponseEntity.status(204).build()
    }

    @ApiOperation("Get a single question by id")
    @GetMapping(path = arrayOf("/{id}"))
    fun getById(@ApiParam("The id of the question")
                @PathVariable("id")
                pId: String?): ResponseEntity<QuestionDto> {
        val id: Long
        try {
            id = pId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }

        val question = questionRepository.findOne(id) ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(QuestionConverter.transform(question))
    }


    @ApiOperation("Update an existing question(if it exists). Not allowed to update id.")
    @PutMapping(path = arrayOf("/{id}"), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun put(
            @ApiParam("The id of the question to update.")
            @PathVariable("id")
            pId: String?,
            @ApiParam("The new Question that will replace the old one.")
            @RequestBody
            dto: QuestionDto): ResponseEntity<Any> {
        val id: Long
        try {
            id = pId!!.toLong()
        } catch (e : Exception) {
            return ResponseEntity.status(404).build()
        }

        if (dto.id != id) {
            // client tried to change the id
            return ResponseEntity.status(409).build()
        }

        if (!questionRepository.exists(id)) {
            // not creating a new category if it does not exist
            return ResponseEntity.status(404).build()
        }

        try {
            val qToUpdate = questionRepository.findOne(id)

            qToUpdate.questionText = dto.questionText
            qToUpdate.category = categoryRepository.findOne(dto.category)
            qToUpdate.correctAnswers = dto.correctAnswer
            qToUpdate.answers = dto.answers

            questionRepository.save(qToUpdate)
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }

        return ResponseEntity.status(204).build()
    }

    @ApiOperation("Update the actual question text of a question given bt id")
    @PatchMapping(path = arrayOf("/{id}"), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun patch(
            @ApiParam("The id of the question to patch.")
            @PathVariable("id")
            pId: String?,
            @ApiParam("The new QuestionText that will replace the old one.")
            @RequestBody
            dto: PatchQuestionTextDto): ResponseEntity<Any>{
        val id : Long

        try{
            id = pId!!.toLong()
        }
        catch (e : Exception){
            return ResponseEntity.status(404).build()
        }

        if(!questionRepository.exists(id)){
            return ResponseEntity.status(404).build()
        }

        if(dto.updatedText.isNullOrBlank() || dto.updatedText!!.length > 500){
            return ResponseEntity.status(400).build()
        }

        if(!questionRepository.update(id, dto.updatedText!!)){
            return ResponseEntity.status(500).build()
        }

        return ResponseEntity.ok().build()
    }

    @ExceptionHandler(value = ConstraintViolationException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: ConstraintViolationException): String {
        val messages = StringBuilder()

        for (violation in ex.constraintViolations) {
            messages.append(violation.message + "\n")
        }

        return messages.toString()
    }
}