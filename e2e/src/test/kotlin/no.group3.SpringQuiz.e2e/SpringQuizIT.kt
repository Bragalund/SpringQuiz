package no.group3.SpringQuiz.e2e

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import no.group3.SpringQuiz.e2e.data.AnswersDto
import no.group3.SpringQuiz.e2e.data.UserDto
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.contains
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

/**
 * Created by josoder on 08.12.17.
 */
class SpringQuizIT {
    companion object {
        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)

        val QUIZ_URL = "http://localhost/quiz/api"
        val USER_URL = "http://localhost/user/details/api"
        val HIGHSCORE_URL = "http://localhost/highscore/api"

        @ClassRule
        @JvmField
        val env = KDockerComposeContainer(File("../docker-compose.yml"))
                .withLocalCompose(true)

        private var counter = System.currentTimeMillis()

        @BeforeClass
        @JvmStatic
        fun initialize() {
            RestAssured.baseURI = "http://localhost"
            RestAssured.port = 80
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()


            await().atMost(500, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until({
                        // zuul and eureka is up when 200 is returned
                        // this will in itself act as a test proving both zuul and eureka works
                        RestAssured.given().get("$QUIZ_URL/health").then().body("status", equalTo("UP"))
                        RestAssured.given().get("$USER_URL/health").then().body("status", equalTo("UP"))
                        RestAssured.given().get("$HIGHSCORE_URL/health").then()
                                .body("status", equalTo("UP"))
                        // need to make sure the data is created before running this tests
                        RestAssured.given().get("$QUIZ_URL/quizzes").then().body("size()", equalTo(2))

                        true
                    })
        }
    }


    @Test
    fun testUnauthorizedAccess() {

        given().get("/user")
                .then()
                .statusCode(401)
    }

    class NeededCookies(val session:String, val csrf: String)

    private fun registerUser(id: String, password: String): NeededCookies {

        val xsrfToken = given().contentType(ContentType.URLENC)
                .formParam("the_user", id)
                .formParam("the_password", password)
                .post("/register")
                .then()
                .statusCode(403)
                .extract().cookie("XSRF-TOKEN")

        val session=  given().contentType(ContentType.URLENC)
                .formParam("the_user", id)
                .formParam("the_password", password)
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookie("XSRF-TOKEN", xsrfToken)
                .post("/register")
                .then()
                .statusCode(201)
                .extract().cookie("SESSION")

        return NeededCookies(session, xsrfToken)
    }

    private fun createUniqueId(): String {
        counter++
        return "foo_$counter"
    }

    @Test
    fun testSecurity() {

        val id = createUniqueId()
        val pwd = "password"

        val cookies = registerUser(id, pwd)

        given().get("/user")
                .then()
                .statusCode(401)

        val answer = AnswersDto(arrayOf(1,2,3,4), username = "not-authed")

        given()
                .pathParam("id", 1)
                .body(answer)
                .post("$QUIZ_URL/quizzes/{id}/check")
                .then()
                .statusCode(403)



        given().cookie("SESSION", cookies.session)
                .get("/user")
                .then()
                .statusCode(200)
                .body("username", equalTo(id))
                .body("roles", contains("ROLE_USER"))
    }

    @Test
    fun testUser(){
        val id = createUniqueId()
        val password = "secret"
        val cookies = registerUser(id, password)

        // Test that cookie is valid
        given().cookie("SESSION", cookies.session)
                .get("/user")
                .then()
                .statusCode(200)

        val username = "SomeUsername"
        val firstname = "SomeFirstName"
        val lastname = "SomeLastName"
        val email = "MyMail@SomeMail.com"

        val userDtoBody = UserDto(null, username, firstname, lastname, email)

//        val userBody = "{\n" +
//                "    \"id\": 1,\n" +
//                "    \"userName\": \"user1\",\n" +
//                "    \"firstName\": \"SomeFirstName\",\n" +
//                "    \"lastName\": \"SomeLastName\",\n" +
//                "    \"email\": \"MyMail@SomeMail.com\"\n" +
//                "}"

        // creates user
        val userId =  given().contentType(ContentType.JSON)
                .cookie("SESSION", cookies.session)
                .cookie("XSRF-TOKEN", cookies.csrf)
                .header("X-XSRF-TOKEN", cookies.csrf)
                .body(userDtoBody)
                .post("$USER_URL/user")
                .then()
                .statusCode(201)
                .extract().asString()

        userDtoBody.userId=userId.toLong()

//        // updates user with put
//        given().cookie("SESSION", cookies.session)
//                .cookie("XSRF-TOKEN", cookies.csrf)
//                .header("X-XSRF-TOKEN", cookies.csrf)
//                .pathParam("id", userId)
//                .put("$USER_URL/user/{id}")
//                .then()
//                .statusCode(204)
//
//        // updates user with patch
//        given().cookie("SESSION", cookies.session)
//                .cookie("XSRF-TOKEN", cookies.csrf)
//                .header("X-XSRF-TOKEN", cookies.csrf)
//                .pathParam("id", userId)
//                .put("$USER_URL/user/{id}")
//                .then()
//                .statusCode(204)

        // deletes user
        given().pathParam("id", userId)
                .contentType(ContentType.JSON)
                .cookie("SESSION", cookies.session)
                .cookie("XSRF-TOKEN", cookies.csrf)
                .header("X-XSRF-TOKEN", cookies.csrf)
                .delete("$USER_URL/user/{id}")
                .then()
                .statusCode(204)
    }

    @Test
    fun testGame() {
        val id = createUniqueId()
        val password = "secret"

        val cookies = registerUser(id, password)

        given().cookie("SESSION", cookies.session)
                .get("/user")
                .then()
                .statusCode(200)

        // get quizzes from category,
        // in GUI would have the user choose quiz based on category
        given().cookie("SESSION", cookies.session)
                .get("$QUIZ_URL/quizzes?category=Math")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))

        val quizIds = given().cookie("SESSION", cookies.session)
                .get("$QUIZ_URL/quizzes?category=Math")
                .then()
                .statusCode(200)
                .extract().path<List<Long>>("quizId")

        println("uniqueoutput: 1231231231231 id :$quizIds")


        // in GUI the user would select one correct answer for each question.
        val answers: Array<Int> = arrayOf(1,1,1,1)

        // The id would be extracted in the gui to be able to display the username
        // a logged in users role and username is available at /user
        val responsId = given().cookie("SESSION", cookies.session)
                .get("/user")
                .then()
                .statusCode(200)
                .extract().path<String>("username")
        assertTrue(id == responsId)


        // To submit the answers the quiz module has an endpoint /quizzes/{id}/check
        // It takes the answer and the logged in users id and calculates it score.
        // It will send the score as response and also create a new amqp message and publish it to rabbitmq

        // check that highscore is empty before submitting the answers
        given().cookie("SESSION", cookies.session)
                .get("$HIGHSCORE_URL/highscore")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))


        // the dto that is handled by the endpoint /quizzes/{id}/check
        val answersDto = AnswersDto(answers = answers, username = responsId)

        given().cookie("SESSION", cookies.session)
                .cookie("XSRF-TOKEN", cookies.csrf)
                .header("X-XSRF-TOKEN", cookies.csrf)
                .contentType(ContentType.JSON)
                .pathParam("id", quizIds[0])
                .body(answersDto)
                .post("$QUIZ_URL/quizzes/{id}/check")
                .then()
                .statusCode(201)
                .body("username", equalTo(responsId))

        // Wait for 3000ms since it will take a moment for the message to be received by the highscore module, with
        // rabbitmq
        assertWithinTime(3000, {
        given().cookie("SESSION", cookies.session)
                .get("$HIGHSCORE_URL/highscore")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
        })

    }

    private fun assertWithinTime(timeoutMS: Long, lambda: () -> Any) {
        val start = System.currentTimeMillis()

        var delta = 0L

        while (delta < timeoutMS) {
            try {
                lambda.invoke()
                return
            } catch (e: AssertionError) {
                Thread.sleep(100)
                delta = System.currentTimeMillis() - start
            } catch (e: Exception) {
                Thread.sleep(100)
                delta = System.currentTimeMillis() - start
            }
        }

        lambda.invoke()
    }

}