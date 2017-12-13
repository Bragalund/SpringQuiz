package no.group3.SpringQuiz.e2e

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import no.group3.SpringQuiz.e2e.data.AnswersDto
import no.group3.SpringQuiz.e2e.data.ScoreDto
import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers.equalTo
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by josoder on 12.12.17.
 * Test to make sure that the rabbitmq communication between quiz and highscore works
 */
class HighscoreQuizAmqpIT {
    companion object {
        val QUIZ_URL = "http://localhost:8083/api"
        val HIGHSCORE_URL = "http://localhost:8085"

        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)

        @ClassRule
        @JvmField
        val env = KDockerComposeContainer(File("amqp-compose.yml"))
                .withLocalCompose(true)

        private var counter = System.currentTimeMillis()

        @BeforeClass
        @JvmStatic
        fun initialize() {
            RestAssured.baseURI = "http://localhost"
            RestAssured.port = 80
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()


            Awaitility.await().atMost(200, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until({
                        RestAssured.given().get("$QUIZ_URL/health").then().body("status", equalTo("UP"))
                        RestAssured.given().get("$HIGHSCORE_URL/health").then()
                                .body("status", equalTo("UP"))

                        true
                    })
        }
    }

    private fun assertWithinTime(timeoutMS: Long, lambda: () -> Any) {
        val start = System.currentTimeMillis()

        var delta = 0L

        while (delta < timeoutMS) {
            try {
                lambda.invoke()
                return
            } catch (e: AssertionError) {
                Thread.sleep(100)
                delta = System.currentTimeMillis() - start
            } catch (e: Exception) {
                Thread.sleep(100)
                delta = System.currentTimeMillis() - start
            }
        }

        lambda.invoke()
    }

    @Test
    fun testAddNewHighScoreEntry(){
        given()
                .get("$HIGHSCORE_URL/highscore")
                .then()
                .body("size()", equalTo(0))

        val mockAnswers = AnswersDto(answers = arrayOf(1,1,1,1), username = "josoder")

        /**
         * This post will create a new message and publish it to rabbitmq.
         * The highscore is subscribing and will create a new entry in the highscore db when it receives it.
         * If an entry with the given username already exist it will only increment its score.
         */
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .body(mockAnswers)
                .post("$QUIZ_URL/quizzes/{id}/check")
                .then()
                .statusCode(201)

        assertWithinTime(3000, {
            given()
                    .get("$HIGHSCORE_URL/highscore")
                    .then()
                    .body("size()", equalTo(1))
        })
    }
}