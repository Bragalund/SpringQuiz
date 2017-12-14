package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class PatchUserTest : UserTestBase() {


    @Test
    fun patchUserTest() {
        // Creates UserDTO instance
        val userDto = getUserDto(UserTestBase.AUTH_USERNAME_1)

        userDto.firstName = "Kristian"
        userDto.lastName = "Klungenberg"

        //Saves UserDto in DB and get userId
        val userId = RestAssured.given().contentType(ContentType.JSON)
                .auth()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .body(userDto)
                .post(UserTestBase.USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        userDto.id = userId.toLong()

        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .body("id", CoreMatchers.equalTo(userId.toInt()))
                .body("firstName", CoreMatchers.equalTo("Kristian"))

        //changes firstname, lastname and email
        userDto.firstName = "Arne"
        userDto.email = "ArneSinMail@mail.com"

        // patches firstname, lastname and email
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .patch(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .body("id", CoreMatchers.equalTo(userId.toInt()))
                .body("firstName", CoreMatchers.equalTo("Arne"))
                .body("lastName", CoreMatchers.equalTo("Klungenberg"))
    }

    @Test
    fun patchWithDto() {
        val userId = createUser(AUTH_USERNAME_1)

        val patchDto = getUserDto(AUTH_USERNAME_1)

        patchDto.email = "Geir@mail.com"

        given().pathParam("id", userId)
                .auth()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(patchDto)
                .patch(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
    }
}