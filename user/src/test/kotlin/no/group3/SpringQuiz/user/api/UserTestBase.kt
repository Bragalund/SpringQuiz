package no.group3.SpringQuiz.user.api

import io.restassured.RestAssured
import no.group3.SpringQuiz.user.UserApplication
import no.group3.SpringQuiz.user.security.WebSecurityConfig
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest("eureka.client.enabled:false" ,classes = arrayOf(UserApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class UserTestBase{


    @LocalServerPort
    protected var port = 0

    @Before
    @After
    fun setUpAndBreakdown() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/apiV1"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

}

