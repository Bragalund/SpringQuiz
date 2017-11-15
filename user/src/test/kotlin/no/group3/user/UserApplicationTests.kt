package no.group3.user

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import no.group3.user.model.dto.UserDto
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class UserApplicationTests : UserTestBase() {

    companion object {
        val USERS_PATH = "/users"
    }


    //Checks if application starts
    @Test
    fun contextLoads() {
    }

    //Creates user and tries and checks if user exists
    @Test
    fun testCreateAndGetById() {

        val userId = given().contentType(ContentType.JSON)
                .body(getUserDto())
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        given().pathParam("id", userId)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId.toInt()))
                .body("firstName", equalTo(getUserDto().firstName))
                .body("lastName", equalTo(getUserDto().lastName))
                .body("email", equalTo(getUserDto().email))
        //.body("password", equalTo(hashedPassword))
    }

    @Test
    fun deleteUser() {

        // Creates user
        val userId = createUser()

        //Checks that user exists
        given().pathParam("id", userId)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        // Deletes user
        given().pathParam("id", userId)
                .delete(USERS_PATH + "/{id}")
                .then().statusCode(204)

        // Checks that user does not exist
        given().pathParam("id", userId)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(404)
    }

    @Test
    fun updateUser() {
        // Creates UserDTO instance
        val userDto = getUserDto()

        //Saves UserDto in DB and get userId
        val userId = given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        //Checks that user exists
        given().pathParam("id", userId)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        // Sets id to userDto-instance
        //userDto.id = userId.toLong()


        // Changes firstname of userdto
        userDto.firstName = "Jonas"

        // Updates user
        given().pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(userDto)
                .put(USERS_PATH + "/{id}")
                .then()
                .statusCode(204)

    }

    fun createUser(): String {
        return given().contentType(ContentType.JSON)
                .body(getUserDto())
                .post(USERS_PATH)
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
        val password = "SomePassword"
        //val hashedPassword = BCrypt.hashpw(userDto.password, BCrypt.gensalt(10))
        return UserDto(id, username, firstname, lastname, email, password)
    }


}
