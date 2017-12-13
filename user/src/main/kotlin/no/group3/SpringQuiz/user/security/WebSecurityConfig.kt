package no.group3.SpringQuiz.user.security

import no.group3.SpringQuiz.user.model.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

@Configuration
@EnableWebSecurity
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.httpBasic()
                .and()
                .authorizeRequests()
                //.antMatchers(HttpMethod.GET, "/user/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                .antMatchers("/user/{id}/**")
                .access("hasRole('USER') and @userSecurity.checkThatIdIsLong(#id) and @userSecurity.checkThatUserExist(#id) and @userSecurity.checkUserName(authentication, #id)")
                .anyRequest().denyAll()
                .and()
                .csrf().disable()
    }


    @Bean
    open fun userSecurity(): UserSecurity {
        return UserSecurity()
    }

}

class UserSecurity {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun checkThatIdIsLong(id: String): Boolean {
        try {
            id.toLong() //Cast String(userId) to Long
            return true
        } catch (exception: Exception) {
            return false
        }

    }


fun checkThatUserExist(id: Long): Boolean {
    return userRepository.exists(id)
}

fun checkUserName(authentication: Authentication, id: Long): Boolean {
    val cookieUsername = (authentication.principal as UserDetails).username
    var existingUsername = ""
    try {
        existingUsername = userRepository.findOne(id).userName!!
    } catch (error: NullPointerException) {
        error.stackTrace
        return false
    }
    return cookieUsername == existingUsername
}
}
