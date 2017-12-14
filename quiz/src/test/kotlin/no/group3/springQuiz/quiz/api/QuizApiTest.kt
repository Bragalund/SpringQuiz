package no.group3.springQuiz.quiz.api

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import no.group3.springQuiz.quiz.model.dto.AnswersDto
import no.group3.springQuiz.quiz.model.dto.PatchQuestionTextDto
import no.group3.springQuiz.quiz.model.dto.QuestionDto
import org.junit.Test
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.ClassRule
import org.springframework.boot.test.util.EnvironmentTestUtils
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer

/**
 * Created by josoder on 31.10.17.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
@ContextConfiguration(initializers = arrayOf(QuizApiTest.Companion.Initializer::class))
class QuizApiTest : QuizTestBase() {
    companion object {
        val QUESTION_PATH = "/questions"
        val CATEGORY_PATH = "/categories"
        val QUIZ_PATH = "/quizzes"


        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
        /*
            This will start a RabbitMQ server using Docker.
            This is "similar" to start the following from command-line:
            docker run -p 5672:5672 rabbitmq:3
            However, here, although the port is exposed, it is mapped to a
            random, free one.
         */
        @ClassRule
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3")
                .withExposedPorts(5672)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                EnvironmentTestUtils.addEnvironment(
                        "testcontainers",
                        configurableApplicationContext.environment,
                        "spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
                        "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672)
                )
            }
        }
    }



@Test
fun testCreateAndGetQuestions() {
    val questionText = "stupid question"
    val answers = listOf("1", "2", "3", "4")
    val correct = 0
    val dto = QuestionDto(questionText = questionText, answers = answers, correctAnswer = correct)

    // should not contain any question/categories before this post.. size should be 0
    given().get(QUESTION_PATH)
            .then()
            .statusCode(200)
            .body("size()", equalTo(0))

    given().get(CATEGORY_PATH)
            .then().statusCode(200).body("size()", equalTo(0))

    // add new category
    val catId = addCategory("category")

    // size should now be 1
    given().get(CATEGORY_PATH)
            .then().statusCode(200).body("size()", equalTo(1))

    // add new question
    val qId = addQuestion(catId)

    given().get(QUESTION_PATH)
            .then().statusCode(200).body("size()", equalTo(1))
}

@Test
fun deleteQuestions() {
    val catId = addCategory("cat")

    val qId = addQuestion(catId)

    given().get(QUESTION_PATH)
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))

    // delete question
    given().pathParam("id", qId)
            .delete("$QUESTION_PATH/{id}")
            .then()
            .statusCode(204)

    given().get(QUESTION_PATH)
            .then()
            .statusCode(200)
            .body("size()", equalTo(0))


    // delete category
    given().pathParam("id", catId)
            .delete("$CATEGORY_PATH/{id}")
            .then()
            .statusCode(204)

    given().get(CATEGORY_PATH)
            .then()
            .statusCode(200)
            .body("size()", equalTo(0))
}


@Test
fun getQuestionById() {
    val catId = addCategory("cat")
    val qId = addQuestion(catId)

    given().pathParam("id", catId)
            .get("$QUESTION_PATH/{id}")
            .then()
            .statusCode(200)
            .body("id", equalTo(qId.toInt())) // Unfortunately this cast is needed. Id is recognized
    // as an int (1L != 1)..
}

@Test
fun updateQuestionWithPut() {
    val before = "before"
    val catId = addCategory("cat")
    val qDto = addQuestion(questionText = before, catId = catId)

    given().pathParam("id", qDto.id)
            .get("$QUESTION_PATH/{id}")
            .then()
            .statusCode(200)
            .body("questionText", equalTo(before))


    val after = "after"
    qDto.questionText = after

    given().contentType(ContentType.JSON)
            .pathParam("id", qDto.id)
            .body(qDto)
            .put("$QUESTION_PATH/{id}")
            .then()
            .statusCode(204)

    given().pathParam("id", qDto.id)
            .get("$QUESTION_PATH/{id}")
            .then()
            .statusCode(200)
            .body("questionText", equalTo(after))
}

@Test
fun patchQuestion() {
    val catId = addCategory("test")

    val originalText = "original"

    val question = addQuestion(questionText = originalText, catId = catId)

    given().pathParam("id", question.id)
            .get("$QUESTION_PATH/{id}")
            .then()
            .statusCode(200)
            .body("questionText", equalTo(originalText))

    val updatedText = "patched"
    val patchDto = PatchQuestionTextDto(updatedText)

    given().contentType(ContentType.JSON)
            .pathParam("id", question.id)
            .body(patchDto)
            .patch("$QUESTION_PATH/{id}")
            .then()
            .statusCode(200)

    given().pathParam("id", question.id)
            .get("$QUESTION_PATH/{id}")
            .then()
            .statusCode(200)
            .body("questionText", equalTo(updatedText))
}

@Test
fun createAndGetQuizzes() {
    given().get(QUIZ_PATH)
            .then()
            .statusCode(200)
            .body("size()", equalTo(0))

    // Create the questions that will populate the quiz.
    val cat = addCategory("test")
    val q1 = addQuestion(questionText = "testq1", catId = cat)
    val q2 = addQuestion(questionText = "testq2", catId = cat)
    val q3 = addQuestion(questionText = "testq3", catId = cat)
    val q4 = addQuestion(questionText = "testq4", catId = cat)
    val qList = listOf(q1, q2)
    val qList2 = listOf(q3, q4)
    val quizDto = addQuiz(questions = qList)
    val quizDto2 = addQuiz(questions = qList2)

    given().get(QUIZ_PATH)
            .then()
            .statusCode(200)
            .body("size()", equalTo(2))


    // get the quiz by id
    given().pathParam("id", quizDto.quizId)
            .get("$QUIZ_PATH/{id}")
            .then()
            .statusCode(200)
            .body("quizId", equalTo(quizDto.quizId!!.toInt()))

    given().pathParam("id", quizDto2.quizId)
            .get("$QUIZ_PATH/{id}")
            .then()
            .statusCode(200)
            .body("quizId", equalTo(quizDto2.quizId!!.toInt()))
}

@Test
fun getAllQuestionsFromQuiz() {
    val quizId = addQuiz()

    given().pathParam("id", quizId)
            .get("$QUIZ_PATH/{id}/questions")
            .then()
            .statusCode(200)
            .body("size()", equalTo(2))
}

@Test
fun deleteQuiz() {
    val quizId = addQuiz()

    given().pathParam("id", quizId)
            .get("$QUIZ_PATH/{id}")
            .then()
            .statusCode(200)
            .body("quizId", equalTo(quizId.toInt()))

    given().pathParam("id", quizId)
            .delete("$QUIZ_PATH/{id}")
            .then()
            .statusCode(204)
}

@Test
fun checkAnswer() {
    val quizId = addQuiz()

    val dto = AnswersDto(answers = arrayOf(2, 1), username = "sven")
    // 2 is the wrong answer but 1 is correct. (see QuizTestBase to verify)
    given().pathParam("id", quizId)
            .contentType(ContentType.JSON)
            .body(dto)
            .post("$QUIZ_PATH/{id}/check")
            .then()
            .statusCode(201)
            .body("score", equalTo(1))
}

@Test
fun getQuizByCategory() {
    // (set to category 'mixed' in testbase)
    val quizId = addQuiz()

    given().get("$QUIZ_PATH?category=mixed")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))

    given().get("$QUIZ_PATH?category=nonexisting")
            .then()
            .statusCode(200)
            .body("size()", equalTo(0))

}

}