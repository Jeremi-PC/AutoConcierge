package com.example.auto_concierge.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

private final InMemoryUsersConfig inMemoryUsersConfig;

    public SecurityConfig(InMemoryUsersConfig inMemoryUsersConfig) {
        this.inMemoryUsersConfig = inMemoryUsersConfig;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable) // временно отключаем защиту от подделки запросов
                // настройка правил аутентификации
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/auth/login", "/auth/registration", "/error").permitAll() // какие url открыть
                                .requestMatchers("/api/**").hasRole("ADMIN")
                                .anyRequest().authenticated()) // остальные открыть только для аутентифицированных пользователей
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout.logoutUrl("/auth/logout").logoutSuccessUrl("/auth/login")) // действия для выхода
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance(); // пока без шифрования пароля в БД
    }

}