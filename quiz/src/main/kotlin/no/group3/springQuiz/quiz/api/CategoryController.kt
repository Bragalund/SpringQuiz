package no.group3.springQuiz.quiz.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.group3.springQuiz.quiz.model.dto.CategoryConverter
import no.group3.springQuiz.quiz.model.dto.CategoryDto
import no.group3.springQuiz.quiz.model.entity.Category
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
 * Created by josoder on 01.11.17.
 */
@Api(value = "/categories", description = "API for categories.")
@RequestMapping(
        path = arrayOf("/categories"),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
)
@RestController
@Validated
class CategoryController{
    @Autowired
    lateinit var questionRepository : QuestionRepository

    @Autowired
    lateinit var categoryRepository : CategoryRepository

    @ApiOperation("Retrieve all categories")
    @GetMapping
    fun get(): ResponseEntity<List<CategoryDto>> {
        return ResponseEntity.ok(CategoryConverter.transform(categoryRepository.findAll()))
    }



    @ApiOperation("Create a category")
    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(code = 201, message = "The id of the created category")
    fun createCategory(
            @ApiParam("Json representation of the category to create")
            @RequestBody
            dto: CategoryDto): ResponseEntity<Long> {
        if (dto.id != null) {
            return ResponseEntity.status(400).build()
        }

        if (dto.name == null)  {
            return ResponseEntity.status(400).build()
        }

        val category: Category?
        try {
            category = categoryRepository.save(Category(name =  dto.name!!))
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }


        categoryRepository.save(category)

        return ResponseEntity.status(201).body(category.id)
    }

    @ApiOperation("Delete category with the given id")
    @DeleteMapping(path = arrayOf("/{id}"))
    fun deleteCategory(@ApiParam("id of the category to delete")
               @PathVariable("id")
               pId: String?): ResponseEntity<Any> {
        val id: Long
        try {
            id = pId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if(!categoryRepository.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        val cat = categoryRepository.findOne(id)


        categoryRepository.delete(id)
        return ResponseEntity.status(204).build()
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