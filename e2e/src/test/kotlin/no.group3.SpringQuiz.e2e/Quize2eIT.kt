package no.group3.SpringQuiz.e2e

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.contains
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by josoder on 08.12.17.
 */
class Quize2eIT {
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


            await().atMost(200, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until({
                        // zuul and eureka is up when 200 is returned
                        // this will in itself act as a test proving both zuul and eureka works
                        RestAssured.given().get("$QUIZ_URL/health").then().body("status", equalTo("UP"))
                        RestAssured.given().get("$USER_URL/health").then().body("status", equalTo("UP"))
                        RestAssured.given().get("$HIGHSCORE_URL/health").then()
                                .body("status", equalTo("UP"))

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
    fun testLogin() {

        val id = createUniqueId()
        val pwd = "password"

        val cookies = registerUser(id, pwd)

        given().get("/user")
                .then()
                .statusCode(401)

        //note the difference in cookie name
        given().cookie("SESSION", cookies.session)
                .get("/user")
                .then()
                .statusCode(200)
                .body("username", equalTo(id))
                .body("roles", contains("ROLE_USER"))
    }

    @Test
    fun testQuiz() {
        val id = createUniqueId()
        val password = "secret"

        val cookies = registerUser(id, password)

        given().cookie("SESSION", cookies.session)
                .get("/quiz/api/quizzes")
                .then()
                .statusCode(200)

        given().cookie("SESSION", cookies.session)
                .get("/quiz/api/quizzes/")
    }
}