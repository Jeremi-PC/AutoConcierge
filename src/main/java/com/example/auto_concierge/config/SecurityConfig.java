package com.example.auto_concierge.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http, AuthenticationManager manager) throws Exception {
        return http
                .csrf().disable() // временно отключаем защиту от подделки запросов
                // настройка правил аутентификации
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/auth/login", "/auth/registration", "/error").permitAll() // какие url открыть
                                // соответствие ролей эндпоинтамв
                                .anyRequest().authenticated()) // остальные открыть только для аутентифицированных пользователей
                // Настройка своей формы аутентификации
                .formLogin(form -> form
                        .defaultSuccessUrl("/view/profile", true) // перенаправляем сюда в случае успешной аутентификации
                        .failureUrl("/auth/login?error") // если аутентификация не удалась, выдаём ту же страницу с параметром error
                )
                .logout(logout -> logout.logoutUrl("/auth/logout").logoutSuccessUrl("/auth/login")) // действия для выхода
                .authenticationManager(manager) // используем менеджер вместо отдельного провайдера
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("u")
                .password("p")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance(); // пока без шифрования пароля в БД
    }

}