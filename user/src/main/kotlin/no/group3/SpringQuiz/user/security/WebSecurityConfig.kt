package no.group3.SpringQuiz.user.security

import no.group3.SpringQuiz.user.model.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
override fun configure(http: HttpSecurity) {

        // To use PUT on user-resources and create a resource is not idempotent because of security-issues

        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/health").permitAll()
                .antMatchers( "/user/**").permitAll()
                .antMatchers( "/swagger-ui.html/**")
                .permitAll()
                .antMatchers( "/swagger**")
                .permitAll()
                //.access("hasRole('USER') and @userSecurity.checkUserNameInDBWithCookie(authentication, #id)")
                // .anyRequest().denyAll()
                .and()
                .csrf().disable()

    }


    @Bean
    fun userSecurity(): UserSecurity {
        return UserSecurity()
    }

}

class UserSecurity {

    @Autowired
    private lateinit var userRepository: UserRepository

//    fun checkThatIdIsLong(id: String): Boolean {
//        try {
//            id.toLong() //Cast String(userId) to Long
//            return true
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return false
//
//    }


//fun checkThatUserExist(id: Long): Boolean {
//    return userRepository.exists(id)
//}

fun checkUserNameInDBWithCookie(authentication: Authentication, id: String): Boolean {
    val idAsLong: Long
    try {
        idAsLong= id.toLong() //Cast String(userId) to Long
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }


    val cookieUsername = (authentication.principal as UserDetails).username
    val existingUsername: String
    try {
        existingUsername = userRepository.findOne(idAsLong).userName!!
    } catch (e: NullPointerException) {
        e.printStackTrace()
        return false
    }
    return cookieUsername.equals(existingUsername)
}
}
