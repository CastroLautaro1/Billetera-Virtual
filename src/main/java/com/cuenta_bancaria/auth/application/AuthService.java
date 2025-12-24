package com.cuenta_bancaria.auth.application;

import com.cuenta_bancaria.auth.infra.dto.LoginRequest;
import com.cuenta_bancaria.auth.infra.dto.RegisterRequest;
import com.cuenta_bancaria.exceptions.domain.EntityInactiveException;
import com.cuenta_bancaria.exceptions.domain.EntityNotFoundException;
import com.cuenta_bancaria.security.application.JwtService;
import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;
import com.cuenta_bancaria.user.infra.data.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryPort userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        // Validar si existe por email

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .alias(request.getAlias())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .status(true)
                .build();
        userRepository.save(user);

        // Faltaria comunicarse con el puerto de entrada de Account para crear la cuenta
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!user.isStatus()) {
            throw new EntityInactiveException("El usuario se encuenta deshabilitado");
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return jwtService.generateToken(userDetails);
    }


}
