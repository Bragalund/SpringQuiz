package no.group3.springQuiz.quiz.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by josoder on 13.10.17.
 */
@RestController
class HelloWorld {
    @GetMapping("/hello")
    fun greet() : String {
        return "Hello world"
    }
}
