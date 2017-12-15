package no.group3.SpringQuiz.user.api

import no.group3.SpringQuiz.user.security.UserSecurity
import no.group3.SpringQuiz.user.security.WebSecurityConfig
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity


@EnableWebSecurity
@Order(1)
class WebSecurityConfigLocal : WebSecurityConfig() {

    override fun configure(http: HttpSecurity) {
      http.httpBasic()
    .and()
    .authorizeRequests()
    .antMatchers(HttpMethod.GET, "/health").permitAll()
    .antMatchers(HttpMethod.POST, "/user").permitAll()
    .antMatchers( "/user/{id}/**")
    .access("hasRole('USER') and @userSecurity.checkUserNameInDBWithCookie(authentication, #id)")
    .anyRequest().denyAll()
    .and()
    .csrf().disable()
    }

    @Bean
    override fun userSecurity(): UserSecurity {
        return UserSecurity()
    }



    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!
                .inMemoryAuthentication()
                .withUser("user1").password("password1").roles("USER")
                .and()
                .withUser("user2").password("password2").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("ADMIN", "USER")
    }
}