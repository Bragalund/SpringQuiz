package no.group3.springQuiz.highscore

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
class HighscoreApplication


@EnableEurekaClient
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
                    .title("Api for highscore")
                    .description("pg6100 assignment")
                    .version("1.0")
                    .build()
        }
    }




fun main(args: Array<String>) {
    SpringApplication.run(HighscoreApplication::class.java, *args)
}
