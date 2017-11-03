package no.group3.user

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import junit.framework.Assert.assertEquals
import no.group3.user.model.dto.UserDto
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class UserApplicationTests: UserTestBase() {

companion object {
    val USERS_PATH= "/users"
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
        assertEquals(userName, userDto.userName)
        //val hashedPassword = BCrypt.hashpw(userDto.password, BCrypt.gensalt(10))

        val userId = given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        given().pathParam("id", userId)
                .get(USERS_PATH+"/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId.toInt()))
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
