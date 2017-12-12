package no.group3.springQuiz.highscore.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.group3.springQuiz.highscore.model.dto.PatchScoreDto
import no.group3.springQuiz.highscore.model.dto.ScoreConverter
import no.group3.springQuiz.highscore.model.dto.ScoreDto
import no.group3.springQuiz.highscore.model.entity.Score
import no.group3.springQuiz.highscore.model.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException


@Api(value = "/highscore", description = "API for highscore.")
@RequestMapping(
        path = arrayOf("/highscore"),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
)
@RestController
@Validated
class ScoreApi{
    @Autowired
    lateinit var scoreRepository : ScoreRepository

    //Return all scores
    @ApiOperation("Returns a list over all scores")
    @GetMapping
    fun get() : ResponseEntity<List<ScoreDto>> {
        val scores = ScoreConverter.transform(scoreRepository.findAll())
        scores.sortedByDescending { scoreDto -> scoreDto.score }
        return ResponseEntity.ok(scores)
    }

/*    //Return all scores with highest score on top
    @ApiOperation("Returns a list over the top scores")
    @GetMapping
    fun getTopScores(
            @ApiParam("The score")
            @RequestParam("score", required = false)
            score: Int?
    ) : ResponseEntity<List<ScoreDto>> {
        return ResponseEntity.ok(ScoreConverter.transform(scoreRepository.findAllByScore(score!!)))
    }*/


    @ApiOperation("Create a score")
    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(code = 201, message = "The id of the created score")
    fun post(
            @ApiParam("Json representation of the score to create")
            @RequestBody
            dto: ScoreDto): ResponseEntity<Long>{
        if (dto.id != null) {
            return ResponseEntity.status(400).build()
        }

        if (dto.user.isNullOrEmpty())  {
            return ResponseEntity.status(400).build()
        }

        val score: Score?
        try {
            score = scoreRepository.save(Score(user =  dto.user!!, score = dto.score!!))
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }

        scoreRepository.save(score)

        return ResponseEntity.status(201).body(score.id)
    }


    //Delete a highscore
    @ApiOperation("Delete score with the given id")
    @DeleteMapping(path = arrayOf("/{id}"))
    @ApiResponse(code = 204, message = "Succesfully deleted score")
    fun delete(
            @ApiParam(" delete the score with given id")
            @PathVariable("id")
            scoreId: String?): ResponseEntity<Any> {
        val id: Long
        try {
            id = scoreId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if(!scoreRepository.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        val score = scoreRepository.findOne(id)
        scoreRepository.delete(id)
        return ResponseEntity.status(204).build()
    }

    //Put User
    @ApiOperation("Update score")
    @PutMapping(path = arrayOf("/{id}"))
    @ApiResponse(code = 204, message = "Succesfully updated score")
    fun put(
            @ApiParam("Id of the score to update")
            @PathVariable("id")
            scoreId: Long?,
            @ApiParam("The body of the new dto that replaces the old one")
            @RequestBody
            scoreDto: ScoreDto): ResponseEntity<Any> {
        val id: Long
        try {
            id = scoreId!!.toLong()
        } catch (exception: Exception) {
            return ResponseEntity.status(400).build()
        }

        if (!scoreRepository.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        if (scoreDto.user == null || scoreDto.score == null){
            return ResponseEntity.status(400).build()
        }

        try {
            scoreRepository.updateHighscore(id,scoreDto.user!!, scoreDto.score!!)
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }

        return ResponseEntity.status(204).build()
    }

    //Patch
    @ApiOperation("Update score in a highscore")
    @PatchMapping(path = arrayOf("/{id}"))
    fun patchScore(
            @ApiParam("Id of the score to update")
            @PathVariable("id")
            id: Long?,
            @ApiParam("")
            @RequestBody
            update: PatchScoreDto
    ) : ResponseEntity<Any>{
        if (id == null) {
            return ResponseEntity.status(400).build()
        }

        val scoreDto = scoreRepository.findOne(id)
            ?: return ResponseEntity.status(404).build()

        if (update.user == null || update.score == null){
            return ResponseEntity.status(400).build()
        }

        scoreDto.user = update.user!!
        scoreDto.score = update.score!!

        try {
            scoreRepository.updateHighscore(id, scoreDto.user!!, scoreDto.score!!)
        }

        catch (e: ConstraintViolationException){
            return ResponseEntity.status(400).build()
        }

        return ResponseEntity.status(204).build()
    }

}
