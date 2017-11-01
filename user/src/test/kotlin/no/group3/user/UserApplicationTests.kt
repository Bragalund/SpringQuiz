package no.group3.user

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import no.group3.user.model.dto.UserDto
import no.group3.user.model.entity.User
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.context.WebApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.test.context.ContextConfiguration


@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(UserApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = arrayOf(UserApplication::class))
class UserApplicationTests {

    @Autowired
    private val context: WebApplicationContext? = null

    @LocalServerPort
    protected var port = 0

    @Before
    fun setUp() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/users"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        RestAssuredMockMvc.webAppContextSetup(context)
    }

    @After
    fun breakDown() {
        RestAssuredMockMvc.reset()
    }


    //Checks if application starts
    @Test
    fun contextLoads() {
    }

    //Creates user and tries and checks if user exists
    @Test
    fun testCreateAndGetById() {
        val id = null
        val userName = "SomeUserName"
        val firstName = "SomeFirstName"
        val lastName = "SomeLastName"
        val email = "MyMail@SomeMail.com"
        val password = "SomePassword"
        val userDto = UserDto(id, userName, firstName,lastName, email, password)
        //val hashedPassword = BCrypt.hashpw(userDto.password, BCrypt.gensalt(10))

        val userId = given().contentType(ContentType.JSON)
                .body(userDto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        given().pathParam("id", userId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("firstname", equalTo(userDto.firstName))
                .body("lastname", equalTo(userDto.lastName))
                .body("MyMail@SomeMail.com", equalTo(userDto.email))
                //.body("password", equalTo(hashedPassword))
    }

    fun getUserDto(): UserDto{
        val id = null
        val username = "SomeUserName"
        val firstname = "SomeFirstName"
        val lastname = "SomeLastName"
        val email = "MyMail@SomeMail.com"
        val password = "SomePassword"
        return UserDto(id, username, firstname,lastname, email, password)
    }

}
