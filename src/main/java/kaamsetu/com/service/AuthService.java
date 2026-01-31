package kaamsetu.com.service;

import kaamsetu.com.DTO.AuthRequest;
import kaamsetu.com.DTO.AuthResponse;
import kaamsetu.com.DTO.RegisterRequest;
import kaamsetu.com.model.Role;
import kaamsetu.com.model.User;
import kaamsetu.com.repository.UserRepository;
import kaamsetu.com.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String registerUser(RegisterRequest r) {
        if(userRepository.existsByEmail(r.getEmail())) {
            throw new IllegalArgumentException("Email Already Taken");
        }

        User u = User.builder()
                .name(r.getName())
                .email(r.getEmail())
                .password(passwordEncoder.encode(r.getPassword()))
                .role(Role.valueOf(r.getRole().toUpperCase())) // Handle case sensitivity
                .college(r.getCollege())
                .year(r.getYear())
                .skills(r.getSkills())
                .build();

        userRepository.save(u);
        return "Registered Successfully";
    }

    public Optional<AuthResponse> loginUser(AuthRequest a) {
        return userRepository.findByEmail(a.getEmail())
                .filter(u -> passwordEncoder.matches(a.getPassword(), u.getPassword()))
                .map(u -> {
                    String token = jwtService.generateToken(u.getEmail());
                    return new AuthResponse(token, u.getName(),u.getRole().toString());
                });
    }
}