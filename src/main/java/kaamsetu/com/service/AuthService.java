package kaamsetu.com.service;

import kaamsetu.com.DTO.AuthRequest;
import kaamsetu.com.DTO.RegisterRequest;
import kaamsetu.com.model.Role;
import kaamsetu.com.model.User;
import kaamsetu.com.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public Optional<String> loginUser(AuthRequest a) {
        return userRepository.findByEmail(a.getEmail())
                .filter(u -> passwordEncoder.matches(a.getPassword(), u.getPassword()))
                .map(u -> "Login SuccessFull for user: " + u.getName());
    }
}