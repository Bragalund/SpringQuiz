package no.group3.springQuiz.highscore.api

import io.swagger.annotations.Api
import org.springframework.http.MediaType
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

}
