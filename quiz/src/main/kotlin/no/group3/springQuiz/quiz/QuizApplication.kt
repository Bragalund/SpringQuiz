package no.group3.springQuiz.quiz

import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.repository.QuestionRepository
import org.aspectj.weaver.patterns.TypePatternQuestions
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@SpringBootApplication
class QuizApplication{

    /*
    @Bean
    fun init(questionRepository: QuestionRepository) = CommandLineRunner {
        questionRepository.save(Question(questionText = "who am I?", answers = listOf("?", "?", "js", "?"),
                correctAnswers = 2))
    }*/
}

@EnableSwagger2
@Configuration
class config{
    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build()
    }


    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("API for quiz")
                .description("pg6100 assignment")
                .version("1.0")
                .build()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(QuizApplication::class.java, *args)
}
