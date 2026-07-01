package lk.classmate.auth.service;

import lk.classmate.auth.dto.*;
import lk.classmate.auth.entity.User;
import lk.classmate.auth.repository.UserRepository;
import lk.classmate.auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(UserRepository repo, PasswordEncoder encoder, JwtUtil jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public AuthResponse register(RegisterRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new RuntimeException("Email already registered");
        }
        User u = new User();
        u.setName(req.name());
        u.setEmail(req.email());
        u.setPassword(encoder.encode(req.password()));
        u.setRole(req.role() == null ? "STUDENT" : req.role().toUpperCase());
        repo.save(u);
        String token = jwt.generateToken(u.getEmail(), u.getRole());
        return new AuthResponse(token, u.getName(), u.getRole());
    }

    public AuthResponse login(LoginRequest req) {
        User u = repo.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!encoder.matches(req.password(), u.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String token = jwt.generateToken(u.getEmail(), u.getRole());
        return new AuthResponse(token, u.getName(), u.getRole());
    }
}