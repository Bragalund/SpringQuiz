package no.group3.user.security

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


// This just allows all (for development)
@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                .anyRequest().denyAll()
                .and()
                .csrf().disable()
    }

}
/*

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    */
/*
     * This is where we define all the access rules,
     * ie who is authorized to access what
     *//*

    override fun configure(http: HttpSecurity) {

        http.authorizeRequests()
                */
/*
                    these rules are matched one at a time, in their order.
                    this is important to keep in mind if different URL templates
                    can match the same URLs
                 *//*

                .antMatchers("/users").permitAll()
                .antMatchers("/users/{id}").permitAll()
                */
/*
                    whitelisting: deny everything by default,
                    unless it was explicitly allowed in the rules
                    above.
                 *//*

                .anyRequest().denyAll()
                .and()
                */
/*
                    there are many different ways to define
                    how login is done.
                    So here we need to configure it.
                    We start from looking at "Basic" HTTP,
                    which is the simplest form of authentication
                  *//*

                .httpBasic()
    }


    */
/*
        Here we configure how users are authenticated.
        For simplicity here we just create some pre-defined users.
     *//*

    override fun configure(auth: AuthenticationManagerBuilder) {

        auth.inMemoryAuthentication()
                .withUser("foo").password("123456").roles("USER").and()
                .withUser("admin").password("bar").roles("ADMIN", "USER")
    }
}*/
