package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class DeleteUserTest :UserTestBase(){


    @Test
    fun deleteUserTest() {

        // Creates user
        val userId = createUser(AUTH_USERNAME_1)

        //Checks that user exists
        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(200)

        // Deletes user
        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .delete(USERS_PATH + "/{id}")
                .then().statusCode(204)

        // Checks that user does not exist
        RestAssured.given().pathParam("id", userId)
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(403)
    }

    @Test
    fun deleteNotExistingUserTest(){
        // Checks that user does not exist
        RestAssured.given().pathParam("id", "123")
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(403)
    }


}