package no.group3.springquiz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;
import java.lang.reflect.Method;


/**
 * Created by josoder on 05.12.17.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private PasswordEncoder passwordEncoder;
    private DataSource dataSource;


    public WebSecurityConfig(PasswordEncoder passwordEncoder, DataSource dataSource){
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }


    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.httpBasic()
                .and()
                .logout()
                .and()
                .authorizeRequests()
                .antMatchers("/user").authenticated()
                // quiz-service rules
                .antMatchers(HttpMethod.GET, "/quiz/**").permitAll()
                .antMatchers(HttpMethod.POST ,"/quiz/**").authenticated()
                .antMatchers(HttpMethod.PUT , "/quiz/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/quiz/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/quiz/**").authenticated()
                // highscore-service rules
                .antMatchers(HttpMethod.GET, "/highscore/**").permitAll()
                .antMatchers(HttpMethod.POST ,"/highscore/**").authenticated()
                .antMatchers(HttpMethod.PUT , "/highscore/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/highscore/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/highscore/**").authenticated()
                // auth api rules
                .antMatchers("/register").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/user").authenticated()
                // user-service rules
                .antMatchers(HttpMethod.GET, "/user/details/**").permitAll()
                .antMatchers("/user/details/**").authenticated()
                .anyRequest().denyAll()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
                .authoritiesByUsernameQuery("SELECT x.username, y.roles FROM users x, user_entity_roles y WHERE " +
                        "x.username=? and y.user_entity_username=x.username")
                .passwordEncoder(passwordEncoder);
    }
}
