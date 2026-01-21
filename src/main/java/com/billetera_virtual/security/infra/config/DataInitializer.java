package com.billetera_virtual.security.infra.config;

import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderAdapter passwordEncoder;
    @Value("${ADMIN_EMAIL}")
    private String adminEmail;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .firstname("System")
                    .lastname("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(User.Role.ADMIN)
                    .status(true)
                    .build();

            userRepository.save(admin);
            System.out.println("Usuario ADMIN inicializado en la base de datos.");
        } else {
            System.out.println("El usuario ADMIN ya existe, no se crea uno nuevo.");
        }
    }
}
