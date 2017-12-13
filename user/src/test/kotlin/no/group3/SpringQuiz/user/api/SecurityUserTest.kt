package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class SecurityUserTest : UserTestBase(){


    @Test
    fun forbiddenToChangeAnotherUserTest(){
        // Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = RestAssured.given().contentType(ContentType.JSON)
                .body(userDto)
                .post(USERS_PATH)
                .then()
                .statusCode(201)
                .extract().asString()

        userDto.id=userId.toLong()


        // Creates another UserDTO instance
        val anotherUserDto = getUserDto(AUTH_USERNAME_2)

        //Saves another UserDto in DB and get userId
        val anotherUserId = RestAssured.given().contentType(ContentType.JSON)
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
        RestAssured.given().pathParam("id", userId)
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
    fun cantPatchWithoutAuthorizationTest(){
        // Creates UserDTO instance
        val userDto = getUserDto(AUTH_USERNAME_1)

        //Saves UserDto in DB and get userId
        val userId = RestAssured.given().contentType(ContentType.JSON)
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
        RestAssured.given().pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(userDto)
                .patch(USERS_PATH +"/{id}")
                .then()
                .statusCode(401)
    }

    @Test
    fun deleteWithMalformedIdInURL(){
        RestAssured.given().pathParam("id", "ABC")
                .auth()
                .basic(AUTH_USERNAME_1, AUTH_PASSWORD_1)
                .get(USERS_PATH + "/{id}")
                .then()
                .statusCode(403)
    }

}