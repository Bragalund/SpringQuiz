package no.group3.springQuiz.highscore.api

import io.restassured.RestAssured
import io.restassured.RestAssured.get
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import no.group3.springQuiz.highscore.model.dto.ScoreDto
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by johannes on 08.11.2017.
 */

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ScoreTest {
    companion object {
        val HIGHSCORE_PATH = "/highscore"
    }

    @Before
    @After
    fun clean() {
        RestAssured.baseURI = "http://localhost:8080"
    }

    private fun createScore(user: String, score: Int){
        given().contentType(ContentType.JSON)
                .body(ScoreDto(null, user, score))
                .post(HIGHSCORE_PATH)
                .then()
                .statusCode(201)
    }

    private fun createSomeScores(){
        createScore("Kjell", 5)
        createScore("Nils", 8)
        createScore("Per", 7)
        createScore("Arne", 9)
    }


    @Test
    fun testGetAllScore(){
        get(HIGHSCORE_PATH).then().body("size()", equalTo(0))

        val dto = ScoreDto(user="Svein", score = 6)

        given().contentType(ContentType.JSON)
                .body(dto)
                .post(HIGHSCORE_PATH)
                .then()
                .statusCode(201)

        get(HIGHSCORE_PATH).then().body("size()", equalTo(1))
    }

}