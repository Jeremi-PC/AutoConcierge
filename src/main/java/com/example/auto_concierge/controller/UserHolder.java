package com.example.auto_concierge.controller;

import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.security.UserWithCredentials;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    public User getUserFromPrincipal(){
        // Обращение к контексту безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // получим бин текущего потока, т.е. пользователя
        return ((UserWithCredentials)authentication.getPrincipal()).user();
    }
}