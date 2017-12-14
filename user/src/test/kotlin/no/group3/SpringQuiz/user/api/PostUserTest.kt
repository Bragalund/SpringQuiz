package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.auth.AUTH
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class PostUserTest : UserTestBase() {

    @Test
    fun createUserTest() {
        RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(201)
    }

    @Test
    fun createTwoUsersWithDifferentNameTest() {
        RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(201)

        RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_2))
                .post(USERS_PATH)
                .then()
                .statusCode(201)
    }

    @Test
    fun createUserWithSameUsernameShouldFailTest() {
        RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(201)

        RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(409)
    }

    @Test
    fun createUserWhereIdIsSetTest() {
        var userDto = getUserDto(AUTH_USERNAME_1)

        userDto.id = 3

        RestAssured.given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(400)
    }

    @Test
    fun createUserWhereFieldAreEmpty(){
        var userDto = getUserDto(AUTH_USERNAME_1)

        userDto.firstName = null

        RestAssured.given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(400)
    }

    @Test
    fun createUserWithMalformedEmail(){
        var userDto = getUserDto(AUTH_USERNAME_1)

        userDto.email = "------abc123------"

        RestAssured.given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(400)
    }


}