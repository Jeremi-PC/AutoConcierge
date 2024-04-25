package com.example.auto_concierge.validator;

import com.example.auto_concierge.dto.user.UserRegistrationFormData;
import com.example.auto_concierge.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator; // обратите внимание, что из org.springframework.validation

@Component
public class UserValidator implements Validator {

    private final UserSecurityService userSecurityService;

    @Autowired
    public UserValidator(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationFormData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationFormData data = (UserRegistrationFormData) target;
        String email = data.getEmail();
        String login = email.substring(0, email.indexOf("@"));
        try {
            userSecurityService.loadUserByUsername(login); // основываться на исключении - плохая практика. Лучше сделать метод, который возвращает Optional
        } catch (Exception e) {
            return;
        }
        errors.rejectValue("email", "There is a user with login " + login + ". Choose another email for registration");
    }
}
