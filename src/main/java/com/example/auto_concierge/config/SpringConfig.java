package com.example.auto_concierge.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Random;

@Configuration
@ComponentScan("com.example.auto_concierge")
@EnableWebMvc
public class SpringConfig {


    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public Faker faker() {
        return new Faker();
    }


}
