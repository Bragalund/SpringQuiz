package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.group3.SpringQuiz.user.UserApplication
import no.group3.SpringQuiz.user.model.dto.UserDto
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest("eureka.client.enabled:false" ,classes = arrayOf(UserApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class UserTestBase{


    @LocalServerPort
    protected var port = 0

    @Before
    @After
    fun setUpAndBreakdown() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    fun createUser(): String {
        return RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto())
                .post(UserApplicationTests.USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()
    }

    fun getUserDto(): UserDto {
        val id = null
        val username = "SomeUserName"
        val firstname = "SomeFirstName"
        val lastname = "SomeLastName"
        val email = "MyMail@SomeMail.com"
        return UserDto(id, username, firstname, lastname, email)
    }

}

