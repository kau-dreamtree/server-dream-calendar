package org.standard.dreamcalendar.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.standard.dreamcalendar.domain.user.type.Role;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                .antMatchers("/**", "/user/**", "/schedule/**", "/swagger-ui.html", "/v2/**").permitAll()
                .antMatchers("/users/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout()
                .and()
                .build();

    }

}