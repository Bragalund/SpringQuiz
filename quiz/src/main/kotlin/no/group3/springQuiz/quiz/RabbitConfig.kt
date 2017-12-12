package no.group3.springQuiz.quiz

import org.springframework.amqp.core.FanoutExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by josoder on 12.12.17.
 */
@Configuration
class RabbitConfiguration {

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("no.group3.springQuiz.quiz")
    }
}