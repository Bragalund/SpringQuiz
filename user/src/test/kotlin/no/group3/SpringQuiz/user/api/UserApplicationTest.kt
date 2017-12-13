package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class UserApplicationTest : UserTestBase() {

    companion object {
        val USERS_PATH = "/user"
        val AUTH_USERNAME_1 = "user1"
        val AUTH_PASSWORD_1 = "password1"
        val AUTH_USERNAME_2 = "user2"
        val AUTH_PASSWORD_2 = "password2"
    }


    @Test
    fun contextLoads() {
    }

    @Test
    fun createUserTest(){
        given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(201)
    }

    //Creates user and tries and checks if user exists
    @Test
    fun createAndGetByIdTest() {
// Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId.toInt()))
                .body("firstName", equalTo(userDto.firstName))
                .body("lastName", equalTo(userDto.lastName))
                .body("email", equalTo(userDto.email))
    }

    @Test
    fun deleteUserTest() {

        // Creates user
        val userId = createUser(AUTH_USERNAME_1)

        //Checks that user exists
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        // Deletes user
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .delete(USERS_PATH + "/{id}")
                .then().statusCode(204)

        // Checks that user does not exist
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(403)
    }

    @Test
    fun updateUserTest() {
        // Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = given()
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        //Checks that user exists
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        // Sets id to userDto-instance
        userDto.id = userId.toLong()


        // Changes firstname of userdto
        userDto.firstName = "Jonas"

        // Updates user
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .put(USERS_PATH + "/{id}")
                .then()
                .statusCode(204)
    }

    @Test
    fun patchUserTest(){
        // Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = given().contentType(ContentType.JSON)
                .auth()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        userDto.id=userId.toLong()

        //changes firstname, lastname and email
        userDto.firstName="Arne"
        userDto.lastName="Klungenberg"
        userDto.email="ArneSinMail@mail.com"

        // patches firstname, lastname and email
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .patch(USERS_PATH +"/{id}")
                .then()
                .statusCode(204)
    }

    @Test
    fun forbiddenToChangeAnotherUserTest(){
        // Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        userDto.id=userId.toLong()


        // Creates another UserDTO instance
        val anotherUserDto = getUserDto(AUTH_USERNAME_2)

        //Saves another UserDto in DB and get userId
        val anotherUserId = given().contentType(ContentType.JSON)
                .body(anotherUserDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        anotherUserDto.id=anotherUserId.toLong()

        //changes firstname, lastname and email
        userDto.firstName="Arne"
        userDto.lastName="Klungenberg"
        userDto.email="ArneSinMail@mail.com"

        // patches firstname, lastname and email
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_2, AUTH_PASSWORD_2)
                .contentType(ContentType.JSON)
                .body(userDto)
                .patch(USERS_PATH +"/{id}")
                .then()
                .statusCode(403)
    }

    @Test
    fun createUserWithSameUsernameShouldFailTest(){
        given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(201)

        given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(409)
    }

    @Test
    fun createTwoUsersWithDifferentNameTest(){
        given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(201)

        given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_2))
                .post(USERS_PATH)
                .then()
                .statusCode(201)
    }

    @Test
    fun cantPutToExistingUsernameTest(){
        // Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        // creates additional user
        given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_2))
                .post(USERS_PATH)
                .then()
                .statusCode(201)

        // Changes firstname of userdto
        userDto.firstName = AUTH_USERNAME_2

        // Updates user
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .put(USERS_PATH + "/{id}")
                .then()
                .statusCode(400)

    }

    @Test
    fun cantPatchWithoutAuthorizationTest(){
        // Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = given().contentType(ContentType.JSON)
                .auth()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        userDto.id=userId.toLong()

        //changes firstname, lastname and email
        userDto.firstName="Arne"
        userDto.lastName="Klungenberg"
        userDto.email="ArneSinMail@mail.com"

        // patches firstname, lastname and email
        given().pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(userDto)
                .patch(USERS_PATH +"/{id}")
                .then()
                .statusCode(401)
    }
}
