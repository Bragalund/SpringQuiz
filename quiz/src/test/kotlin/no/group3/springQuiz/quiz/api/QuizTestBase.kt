package no.group3.springQuiz.quiz.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.group3.springQuiz.quiz.QuizApplication
import no.group3.springQuiz.quiz.model.dto.QuestionDto
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by josoder on 31.10.17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = arrayOf(QuizApplication::class))
abstract class QuizTestBase {
    @LocalServerPort
    protected var port = 0


    @Before
    @After
    fun clean() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()


    }

    fun addQuestion(subId: String) {
        val qDto = QuestionDto(questionText = "whatever?",answers = listOf("1", "2"),
                correctAnswer = 1)

        RestAssured.given().contentType(ContentType.JSON)
                .body(qDto)
                .post("/questions")
                .then()
                .statusCode(201)
    }

}