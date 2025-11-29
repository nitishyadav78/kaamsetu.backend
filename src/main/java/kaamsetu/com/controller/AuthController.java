package kaamsetu.com.controller;

import kaamsetu.com.DTO.AuthRequest;
import kaamsetu.com.DTO.RegisterRequest;
import kaamsetu.com.model.Role;
import kaamsetu.com.model.User;
import kaamsetu.com.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest r)
    {
        if(userRepository.existsByEmail(r.getEmail()))
        {
            return ResponseEntity.badRequest().body("Email Already Taken");
        }

        User u = User.builder()
                .name(r.getName())
                .email(r.getEmail())
                .password(passwordEncoder.encode(r.getPassword()))
                .role(Role.valueOf(r.getRole()))
                .college(r.getCollege())
                .year(r.getYear())
                .skills(r.getSkills())
                .build();

                userRepository.save(u);

                return ResponseEntity.ok("Registered Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest a)
    {
        return userRepository.findByEmail(a.getEmail())
                .filter(u -> passwordEncoder.matches(a.getPassword(), u.getPassword()))
                .map(u -> {
                    return ResponseEntity.ok("Login SuccessFull for user :" + u.getName());
                })
                .orElse(ResponseEntity.status(401)
                        .body("Invalid Email or password"));
    }

}
