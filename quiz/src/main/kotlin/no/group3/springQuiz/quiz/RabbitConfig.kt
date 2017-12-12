package no.group3.springQuiz.quiz

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter


/**
 * Created by josoder on 12.12.17.
 */
@Configuration
class RabbitConfiguration {
    @Bean
    fun jsonMessageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("no.group3.springQuiz")
    }
}

