package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.group3.SpringQuiz.user.model.dto.PatchDto
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class LargeUserTest : UserTestBase() {

    @Test
    fun createPatchAndDeleteUserTest(){
        val userId = createUser(AUTH_USERNAME_1)

        val patchDto = getPatchDto()

        // Updates user with PATCH
        RestAssured.given().pathParam("id", userId)
                .auth()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(patchDto)
                .patch(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(204)

        val userDto = getUserDto(AUTH_USERNAME_1)
        userDto.id = userId.toLong()

        // Get user
        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        // Updates user with PUT
        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(UserTestBase.AUTH_USERNAME_1, UserTestBase.AUTH_PASSWORD_1)
                .contentType(ContentType.JSON)
                .body(userDto)
                .put(UserTestBase.USERS_PATH + "/{id}")
                .then()
                .statusCode(204)

        // Deletes user
        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .delete(USERS_PATH + "/{id}")
                .then().statusCode(204)
    }
}