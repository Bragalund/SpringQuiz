package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class PutUserTest : UserTestBase() {


    @Test
    fun putUserTest() {
        // Creates UserDTO instance
        val userDto = getUserDto(UserTestBase.AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = RestAssured.given()
                .auth()
                .preemptive()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .post(UserTestBase.USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        //Checks that user exists
        RestAssured.given().pathParam("id", userId)
                .auth()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .get(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        // Sets id to userDto-instance
        userDto.id = userId.toLong()


        // Changes firstname of userdto
        userDto.firstName = "Jonas"

        // Updates user
        RestAssured.given().pathParam("id", userId)
                .auth()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .put(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(204)
    }

    @Test
    fun cantPutToExistingUsernameTest(){
        // Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        // creates additional user
        RestAssured.given().contentType(ContentType.JSON)
                .body(getUserDto(AUTH_USERNAME_2))
                .post(USERS_PATH)
                .then()
                .statusCode(201)

        // Changes firstname of userdto
        userDto.firstName = AUTH_USERNAME_2

        // Updates user
        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .put(USERS_PATH + "/{id}")
                .then()
                .statusCode(400)

    }


}
