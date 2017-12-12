package no.group3.SpringQuiz.user.api

import no.group3.SpringQuiz.user.security.WebSecurityConfig
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@Order(1)
open class WebSecurityConfigLocal : WebSecurityConfig() {
    override fun configure(http: HttpSecurity) {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .csrf().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
    }
}