package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class CreateUserTest : UserTestBase(){

    @Test
    fun createUserTest(){
        RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_1))
                .post(USERS_PATH)
                .then()
                .statusCode(201)
    }

    @Test
    fun createTwoUsersWithDifferentNameTest(){
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
    fun createUserWithSameUsernameShouldFailTest(){
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


    //Creates user and tries and checks if user exists
    @Test
    fun createAndGetByIdTest() {

        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = RestAssured.given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .body("id", CoreMatchers.equalTo(userId.toInt()))
                .body("firstName", CoreMatchers.equalTo(userDto.firstName))
                .body("lastName", CoreMatchers.equalTo(userDto.lastName))
                .body("email", CoreMatchers.equalTo(userDto.email))
    }
}