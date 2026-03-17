package com.billetera_virtual.config;

import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.domain.port.AccountRepositoryPort;
import com.billetera_virtual.auth.application.AuthService;
import com.billetera_virtual.auth.infra.dto.RegisterRequest;
import com.billetera_virtual.security.infra.config.PasswordEncoderAdapter;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AuthService authService;
    private final UserRepositoryPort userRepository;
    private final AccountRepositoryPort accountRepository;
    private final PasswordEncoderAdapter passwordEncoder;
    @Value("${ADMIN_EMAIL}")
    private String adminEmail;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;


    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
        createTestUser();
    }

    public void createTestUser() {
        if(!userRepository.existsByEmail("lautaro@oran.com")) {
            authService.register(new RegisterRequest("Lautaro", "Castro", "lautaro@oran.com", "Oran2026!"));
            User lautaro = userRepository.findByEmail("lautaro@oran.com").orElseThrow();
            Account cuentaLautaro = accountRepository.getByIdUser(lautaro.getId()).orElseThrow();
            cuentaLautaro.setBalance(new BigDecimal(10000.00));
            accountRepository.save(cuentaLautaro);
            System.out.println("✅ Usuario de prueba creado exitosamente con saldo inicial.");
        }
        if(!userRepository.existsByEmail("zlatan@oran.com")) {
            authService.register(new RegisterRequest("Zlatan", "Ibrahimovic", "zlatan@oran.com", "Oran2026!"));
            User zlatan = userRepository.findByEmail("zlatan@oran.com").orElseThrow();
            Account cuentaZlatan = accountRepository.getByIdUser(zlatan.getId()).orElseThrow();
            cuentaZlatan.setBalance(new BigDecimal(10000.00));
            accountRepository.save(cuentaZlatan);
            System.out.println("✅ Usuario de prueba creado exitosamente con saldo inicial.");
        }

    }

    public void createAdminUser() {
        if(!userRepository.existsByEmail(adminEmail)) {
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
