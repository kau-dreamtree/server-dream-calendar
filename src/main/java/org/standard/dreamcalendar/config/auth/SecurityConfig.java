package org.standard.dreamcalendar.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.standard.dreamcalendar.domain.user.Role;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/user/**").permitAll()
                .anyRequest().hasRole(Role.USER.name())
                .and()
                .logout()
                .logoutSuccessUrl("/user/auth/logout")
                .and()
                .build();

    }

}
