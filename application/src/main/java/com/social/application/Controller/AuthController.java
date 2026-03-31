package com.social.application.Controller;
import com.social.application.DTOs.AuthResponseDTO;
import com.social.application.DTOs.LoginRequestDTO;
import com.social.application.Models.User;
import com.social.application.Services.AuthService;
import com.social.application.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public User currentUser(Authentication authentication) {
        return userService.fetchUserByEmail(authentication.getName());
    }
}
