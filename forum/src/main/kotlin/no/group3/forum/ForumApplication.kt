package no.group3.forum

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ForumApplication

fun main(args: Array<String>) {
    SpringApplication.run(ForumApplication::class.java, *args)
}
