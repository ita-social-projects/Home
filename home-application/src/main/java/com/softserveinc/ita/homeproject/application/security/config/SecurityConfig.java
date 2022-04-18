package com.softserveinc.ita.homeproject.application.security.config;

import java.util.ArrayList;
import java.util.LinkedList;

import com.softserveinc.ita.homeproject.application.security.filter.JWTProvider;
import com.softserveinc.ita.homeproject.application.security.filter.JWTTokenValidatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * SecurityConfig class configures security in application.
 * Class provides HttpBasic auth for the application.
 *
 * @author Ihor Svyrydenko
 */
@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTProvider jwtProvider;

    @Autowired
    public SecurityConfig(@Qualifier("homeUserDetailsService") UserDetailsService userDetailsService,
                          JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable()
            .cors().configurationSource(request ->
                getCorsConfiguration())
            .and()
            .addFilterBefore(new JWTTokenValidatorFilter(jwtProvider),
                BasicAuthenticationFilter.class);
    }

    private CorsConfiguration getCorsConfiguration() {
        var corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        return corsConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

