package br.com.ronan.springboot.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ronan.springboot.service.SpringbootUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SpringbootUserDetailsService springbootUserDetailsService;

    /**
     * BasicAuthenticatorFilter 
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginPageGeneratingFilter 
     * DefaultLogoutPageGeneratingFilter
     * FilterSecurityInteceptor
     * Authentication => Authorization
     * @param http
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                .authorizeRequests().antMatchers("/animes/admin/**").hasRole("ADMIN")
                .antMatchers("/animes/**").hasRole("USER")
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated().and().formLogin().and().httpBasic()
                .and().formLogin()
                .and().httpBasic(); 
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoded {}", passwordEncoder.encode("senha"));
        auth.inMemoryAuthentication()
                .withUser("ronan2").password(passwordEncoder.encode("senha")).roles("ADMIN", "USER")
                .and()
                .withUser("user2").password(passwordEncoder.encode("senha")).roles("USER");
        auth.userDetailsService(springbootUserDetailsService).passwordEncoder(passwordEncoder);
    }
}
