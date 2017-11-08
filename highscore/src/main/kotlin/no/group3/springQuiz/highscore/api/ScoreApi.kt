package no.group3.springQuiz.highscore.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.group3.springQuiz.highscore.model.dto.ScoreConverter
import no.group3.springQuiz.highscore.model.dto.ScoreDto
import no.group3.springQuiz.highscore.model.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    @ApiOperation("Returns a list over the top scores")
    @GetMapping(path = arrayOf("/highscores"))
    fun get() : ResponseEntity<List<ScoreDto>> {
        return ResponseEntity.ok(ScoreConverter.transform(scoreRepository.findAll()))
    }


    //Add a highscore

    //Delete a highscore

    //Delete all highscores

    //
}
