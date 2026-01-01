package com.cuenta_bancaria.auth.application;

import com.cuenta_bancaria.auth.domain.CreateAccountExternalPort;
import com.cuenta_bancaria.auth.infra.dto.LoginRequest;
import com.cuenta_bancaria.auth.infra.dto.RegisterRequest;
import com.cuenta_bancaria.exceptions.domain.EntityAlreadyExistsException;
import com.cuenta_bancaria.exceptions.domain.EntityInactiveException;
import com.cuenta_bancaria.exceptions.domain.EntityNotFoundException;
import com.cuenta_bancaria.security.application.JwtService;
import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryPort userRepository;
    private final CreateAccountExternalPort accountExternal;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        // hacer un metodo para usar exists en lugar de find
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EntityAlreadyExistsException("El email ingresado ya esta registrado");
        }

        if (userRepository.existsByAlias(request.alias())) {
            throw new EntityAlreadyExistsException("El alias ingresado esta en uso");
        }

        User user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .alias(request.alias())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(User.Role.USER)
                .status(true)
                .build();
        User userSaved = userRepository.save(user);

        accountExternal.createAccountByUserId(userSaved.getId());
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!user.isStatus()) {
            throw new EntityInactiveException("El usuario se encuenta deshabilitado");
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return jwtService.generateToken(userDetails);
    }


}
