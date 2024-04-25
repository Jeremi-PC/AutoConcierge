package com.example.auto_concierge.security;

import com.example.auto_concierge.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

@Component
// Нет необходимости создавать свой AuthenticationProvider, т.к. есть такой же готовый. См. SecurityConfig
public class AuthProviderImpl implements AuthenticationProvider {

    private final UserSecurityService userSecurityService;

    @Autowired
    public AuthProviderImpl(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails details = userSecurityService.loadUserByUsername(username);

        if (!details.isCredentialsNonExpired()) throw new BadCredentialsException("Password expired");
        if (!Objects.equals(password, details.getPassword())) throw new BadCredentialsException("Incorrect password");
        return new UsernamePasswordAuthenticationToken(details, password, new ArrayList<>());
        // в последне коллекции нужно указывать список прав пользователя
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}


