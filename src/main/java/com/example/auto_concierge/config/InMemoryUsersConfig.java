package com.example.auto_concierge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class InMemoryUsersConfig {
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails defaultAdmin = User.builder()
                .username("admin")
                .password("1234")
                .roles("ADMIN")
                .build();
        UserDetails defaultClient = User.builder()
                .username("client")
                .password("1234")
                .roles("CLIENT")
                .build();
        return new InMemoryUserDetailsManager(defaultAdmin, defaultClient);
    }


}
