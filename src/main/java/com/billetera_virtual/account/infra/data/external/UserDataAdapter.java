package com.billetera_virtual.account.infra.data.external;

import com.billetera_virtual.account.domain.dto.UserDataDTO;
import com.billetera_virtual.account.domain.port.external.UserDataPort;
import com.billetera_virtual.exceptions.domain.EntityNotFoundException;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import com.billetera_virtual.user.domain.port.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDataAdapter implements UserDataPort {

    private final UserRepositoryPort userRepository;

    @Override
    public UserDataDTO getUserDataById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDataDTO(user.getFirstname(), user.getLastname()))
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con ID: " + id));
    }
}
