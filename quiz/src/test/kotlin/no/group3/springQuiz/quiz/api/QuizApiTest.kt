package no.group3.springQuiz.quiz.api

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import no.group3.springQuiz.quiz.model.dto.QuestionDto
import org.junit.Test
import org.hamcrest.CoreMatchers.equalTo

/**
 * Created by josoder on 31.10.17.
 */
class QuizApiTest: QuizTestBase() {
    companion object {
        val QUESTION_PATH = "questions"
    }

    @Test
    fun testCreateAndGet(){
        val questionText = "stupid question"
        val answers = listOf("1", "2", "3", "4")
        val correct = 0
        val dto = QuestionDto(questionText = questionText, answers = answers, correctAnswer =  correct)


        // should not contain any question before..
        given().get(QUESTION_PATH)
                .then().statusCode(200).body("size()", equalTo(0))

        // Post new question
        val id = given().contentType(ContentType.JSON)
                .body(dto)
                .post(QUESTION_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        // check that it gets returned
        given().get(QUESTION_PATH)
                .then().statusCode(200).body("size()", equalTo(1))

    }
}