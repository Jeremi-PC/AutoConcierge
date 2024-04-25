package com.example.auto_concierge.dto.user;

import com.example.auto_concierge.entity.user.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public final class UserRegistrationFormData {
    @NotBlank(message = "First name must not be blank")
    @Size(max = 100, message = "First name length cannot exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 100, message = "Last name length cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email address is not valid")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(max = 100, message = "Password length cannot exceed 100 characters")
    private String password;

    @NotNull(message = "Role must not be null")
    private Role role;
}
