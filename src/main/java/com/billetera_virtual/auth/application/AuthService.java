package com.billetera_virtual.auth.application;

import com.billetera_virtual.auth.domain.*;
import com.billetera_virtual.auth.infra.dto.LoginRequest;
import com.billetera_virtual.auth.infra.dto.RegisterRequest;
import com.billetera_virtual.exceptions.domain.EntityAlreadyExistsException;
import com.billetera_virtual.exceptions.domain.EntityInactiveException;
import com.billetera_virtual.exceptions.domain.EntityNotFoundException;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryPort userRepository;
    private final CreateAccountExternalPort accountExternal;
    private final IdentityServicePort identityService;
    private final TokenProviderPort tokenProvider;
    private final AuthPasswordEncoderPort authPasswordEncoder;

    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EntityAlreadyExistsException("El email ingresado ya esta registrado");
        }

        User user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(authPasswordEncoder.encodePass(request.password()))
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

        identityService.authenticate(request.email(), request.password());

        return tokenProvider.generateToken(request.email());
    }

}
