package kaamsetu.com.controller;

import kaamsetu.com.DTO.AuthRequest;
import kaamsetu.com.DTO.RegisterRequest;
import kaamsetu.com.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Sirf AuthService ki dependency rakhein
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest r) {
        try {
            String result = authService.registerUser(r);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest a) {
        return authService.loginUser(a)
                .map(message -> ResponseEntity.ok(message))
                .orElse(ResponseEntity.status(401).body("Invalid Email or password"));
    }
}