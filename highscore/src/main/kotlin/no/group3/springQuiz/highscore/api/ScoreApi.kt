package no.group3.springQuiz.highscore.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.group3.springQuiz.highscore.model.dto.ScoreConverter
import no.group3.springQuiz.highscore.model.dto.ScoreDto
import no.group3.springQuiz.highscore.model.entity.Score
import no.group3.springQuiz.highscore.model.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

/**
 * Created by johannes on 01.11.2017.
 */

@Api(value = "/highscore", description = "API for highscore.")
@RequestMapping(
        path = arrayOf("/highscore"),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
)
@RestController
class ScoreApi{
    @Autowired
    lateinit var scoreRepository : ScoreRepository

    //Return all scores
    @ApiOperation("Returns a list over all scores")
    @GetMapping
    fun get() : ResponseEntity<List<ScoreDto>> {
        return ResponseEntity.ok(ScoreConverter.transform(scoreRepository.findAll()))
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

        if (dto.user == null)  {
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

    //Delete all highscores


    //
}
