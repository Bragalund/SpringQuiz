package no.group3.springQuiz.highscore.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.group3.springQuiz.highscore.HighscoreApplication
import no.group3.springQuiz.highscore.model.dto.ScoreDto
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(HighscoreApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ScoreTestBase{


    @LocalServerPort
    protected var port = 0

    @Before
    @After
    fun clean() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = ""
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    fun addScore(user: String, score: Int): Long {
        val scoreDto = ScoreDto(user=user, score=score)
        val scoreId = RestAssured.given().contentType(ContentType.JSON)
                .body(scoreDto)
                .post("/highscore")
                .then()
                .statusCode(201)
                .extract().asString().toLong()

        return scoreId
    }

    fun addScoreDto(user: String, score: Int) : ScoreDto {
        val scoreDto = ScoreDto(user = user, score = score)

        val id = RestAssured.given().contentType(ContentType.JSON)
                .body(scoreDto)
                .post("/highscore")
                .then()
                .statusCode(201)
                .extract().asString().toLong()

        scoreDto.id= id

        return scoreDto
    }

}