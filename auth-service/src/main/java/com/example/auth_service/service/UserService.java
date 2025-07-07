package com.example.auth_service.service;

import com.example.auth_service.dto.AuthResponse;
import com.example.auth_service.dto.ErrorResponse;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.repository.UserRepository;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.auth_service.model.User;
import com.example.auth_service.dto.AuthRequest;
import com.example.auth_service.security.JwtService;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public ResponseEntity<?> register(RegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Username Already exists"));
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);

        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        var token = jwtService.generateToken(userDetails);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AuthResponse(token));
    }

    public AuthResponse authenticate(AuthRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        var token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);

    }

    public Map<String, Object> verify(String token){
        String username = jwtService.extractUsername(token);

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        boolean valid = jwtService.isTokenValid(token, userDetailsService.loadUserByUsername(username));

        return Map.of(
                "username", username,
                "valid", valid,
                "role", user.getRole()
        );
    }

}
