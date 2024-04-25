package com.example.auto_concierge.controller;


import com.example.auto_concierge.dto.user.UserRegistrationFormData;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.mapper.UserMapper;
import com.example.auto_concierge.repository.UserRepository;
import com.example.auto_concierge.service.UserService;
import com.example.auto_concierge.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path = "auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder, UserValidator userValidator, UserService userService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("registrationForm") UserRegistrationFormData userFormData) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String completeRegistration(
            @ModelAttribute("registrationForm") UserRegistrationFormData userFormData,
            BindingResult result
    ) {
        userValidator.validate(userFormData, result);
        if (result.hasErrors()) return "auth/registration";

        User user = UserMapper.INSTANCE.registrationFormDataToUser(userFormData);
        userService.createUser(user);

        return "redirect:/login";
    }
//    @PostMapping("/process_login")
//    public ResponseEntity<String> processLogin(@RequestParam String username, @RequestParam String password) {
//        Optional<User> user = userRepository.findByUsername(username);
//        return user.map(u -> {
//            if (!passwordEncoder.matches(password, u.getPassword())) {
//                try {
//                    throw new IncorrectAuthenticationException("Incorrect password");
//                } catch (IncorrectAuthenticationException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
//            Authentication authentication = authenticationManager.authenticate(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            if (u.getRole() == Role.ADMIN) {
//                return ResponseEntity.status(HttpStatus.OK).body("redirect:/admin/home");
//            } else if (u.getRole() == Role.CLIENT) {
//                return ResponseEntity.status(HttpStatus.OK).body("redirect:/user/home");
//            } else if (u.getRole() == Role.SERVICE_CENTER) {
//                    return ResponseEntity.status(HttpStatus.OK).body("redirect:/service_center/home");
//            } else if (u.getRole() == Role.PART_SUPPLIER) {
//                return ResponseEntity.status(HttpStatus.OK).body("redirect:/part_supplier/home");
//            }else {
//                throw new RuntimeException("Unknown user role");
//            }
//        }).orElseThrow(() -> new NotFoundException("User not found with username: " + username));
//    }
}
