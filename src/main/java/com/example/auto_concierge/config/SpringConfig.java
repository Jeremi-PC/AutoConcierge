package com.example.auto_concierge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Random;

@Configuration
@ComponentScan("com.example.auto_concierge")
@EnableWebMvc
public class SpringConfig {}
