package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.http.auth.AUTH
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class LargeUserTest : UserTestBase() {

    @Test
    fun createPatchAndDeleteUserTest(){
        val userId = createUser(AUTH_USERNAME_1)

        val patchDto = getUserDto(AUTH_USERNAME_1)
        patchDto.firstName="Arne"

        // Updates user with PATCH
        given().pathParam("id", userId)
                .auth()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(patchDto)
                .patch(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        val userDto = getUserDto(AUTH_USERNAME_1)
        userDto.id = userId.toLong()

        // Get user
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        // Updates user with PUT
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .put(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(204)

        // Deletes user
        given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .delete(USERS_PATH + "/{id}")
                .then().statusCode(204)
    }
}