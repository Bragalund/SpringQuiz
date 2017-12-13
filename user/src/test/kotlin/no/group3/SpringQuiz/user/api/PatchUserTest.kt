package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class PatchUserTest : UserTestBase() {


    @Test
    fun patchUserTest() {
        // Creates UserDTO instance
        val userDto = getUserDto(UserTestBase.AUTH_USERNAME_1)

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

        //changes firstname, lastname and email
        userDto.firstName = "Arne"
        userDto.lastName = "Klungenberg"
        userDto.email = "ArneSinMail@mail.com"

        // patches firstname, lastname and email
        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .patch(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(204)
    }
}