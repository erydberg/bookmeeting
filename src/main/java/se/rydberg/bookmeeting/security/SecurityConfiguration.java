package se.rydberg.bookmeeting.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/bookmeeting/**").permitAll()
                .antMatchers("/utv").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/styles/**").permitAll()
                //normal users - well not now-perhaps public?
                //to change to admin-user
                .antMatchers("/admin/**").permitAll()

                //admin users
                .antMatchers("/h2-console/**").permitAll()
                .and()
                .authorizeRequests()
                    .antMatchers("/menu/**").hasRole("USER")
                  //  .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()

                .and()
                .csrf()
                .disable() // bara för att komma åt h2-console
                .headers()
                .frameOptions()
                .disable()
                .and()
                .httpBasic()
                .and()
                .logout()
                .and()
                .rememberMe();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
}
