package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean after each test
class ApplicationUserTest : UserTestBase() {

    @Test
    fun contextLoads() {
    }

}
