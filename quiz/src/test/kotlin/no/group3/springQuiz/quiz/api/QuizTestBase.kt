package no.group3.springQuiz.quiz.api

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import no.group3.springQuiz.quiz.QuizApplication
import no.group3.springQuiz.quiz.model.dto.CategoryDto
import no.group3.springQuiz.quiz.model.dto.QuestionDto
import no.group3.springQuiz.quiz.model.dto.QuizDto
import no.group3.springQuiz.quiz.security.WebSecurityConfig
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.test.context.ActiveProfiles


/**
 * Created by josoder on 31.10.17.
 * Test base with helper methods
 */
@RunWith(SpringRunner::class)
@SpringBootTest("eureka.client.enabled:false" ,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = arrayOf(QuizApplication::class))
@ActiveProfiles("test")
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

    // Helper methods.

    fun addCategory(name: String): Long {
        val catDto = CategoryDto(name = name)

        val catId = given().contentType(ContentType.JSON)
                .body(catDto)
                .post("/categories")
                .then()
                .statusCode(201)
                .extract().asString().toLong()

        return catId
    }

    fun addQuestion(catId : Long): Long {
        val qDto = QuestionDto(questionText = "whatever?",answers = listOf("1", "2", "3", "4"),
                correctAnswer = 1, category = catId)

        val qId = given().contentType(ContentType.JSON)
                .body(qDto)
                .post("/questions")
                .then()
                .statusCode(201)
                .extract().asString().toLong()

        return qId
    }

    fun addQuestion(questionText: String, catId : Long): QuestionDto {
        val qDto = QuestionDto(questionText = questionText,answers = listOf("1", "2", "3", "4"),
                correctAnswer = 1, category = catId)

        val qId = given().contentType(ContentType.JSON)
                .body(qDto)
                .post("/questions")
                .then()
                .statusCode(201)
                .extract().asString().toLong()
        qDto.id = qId
        return qDto
    }

    fun addQuiz(questions : List<QuestionDto>): QuizDto {
        val quizDto = QuizDto(questions = questions, difficulty = 1, category = "mixed")

        val qId = given().contentType(ContentType.JSON)
                .body(quizDto)
                .post("/quizzes")
                .then()
                .statusCode(201)
                .extract().asString().toLong()
        quizDto.id = qId
        return quizDto
    }

    // create cat and questions as well
    fun addQuiz(): Long {
        val cat = addCategory("test")
        val q1 = addQuestion(questionText = "testq1", catId = cat)
        val q2 = addQuestion(questionText = "testq2", catId = cat)
        val qList = listOf(q1, q2)
        val q = addQuiz(qList)

        return q.id!!
    }
}

@Configuration
@EnableWebSecurity
@Order(1)
class WebSecurityConfigLocalFake : WebSecurityConfig() {
    override fun configure(http: HttpSecurity) {
       http.httpBasic()
               .and()
               .authorizeRequests()
               .antMatchers("/**").permitAll()
               .and()
               .csrf().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
    }
}

