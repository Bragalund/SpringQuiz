package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.group3.SpringQuiz.user.UserApplication
import no.group3.SpringQuiz.user.model.dto.PatchDto
import no.group3.SpringQuiz.user.model.dto.UserDto
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.print.attribute.standard.JobOriginatingUserName

@RunWith(SpringRunner::class)
@SpringBootTest("eureka.client.enabled:false",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = arrayOf(UserApplication::class))
abstract class UserTestBase{

    companion object {
        val USERS_PATH = "/user"
        val AUTH_USERNAME_1 = "user1"
        val AUTH_PASSWORD_1 = "password1"
        val AUTH_USERNAME_2 = "user2"
        val AUTH_PASSWORD_2 = "password2"
    }

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


    // helper methods

    fun createUser(chosenUserName: String): String {
        return RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto(chosenUserName))
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()
    }


    fun getPatchDto(): PatchDto{
        val firstname = "Lars"
        val lastname = "Larsen"
        val email = "larsen@mail.com"
        return PatchDto(firstname, lastname, email)
    }

    fun getUserDto(chosenUserName: String): UserDto {
        val id = null
        val username = chosenUserName
        val firstname = "SomeFirstName"
        val lastname = "SomeLastName"
        val email = "MyMail@SomeMail.com"
        return UserDto(id, username, firstname, lastname, email)
    }

}

