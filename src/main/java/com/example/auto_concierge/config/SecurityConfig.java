package com.example.auto_concierge.config;
import com.example.auto_concierge.security.JwtAuthTokenFilter;
import com.example.auto_concierge.security.UserDetailsServiceImpl;
import com.example.auto_concierge.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new CustomOAuth2UserService(); // Реализация пользовательского сервиса для обработки OAuth2 пользователей
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter(); // Фильтр для JWT аутентификации
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
      return new BCryptPasswordEncoder();
//        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance(); // пока без шифрования пароля в БД
    }

}