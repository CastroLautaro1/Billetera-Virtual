package com.billetera_virtual.user.application;

import com.billetera_virtual.exceptions.domain.EntityAlreadyExistsException;
import com.billetera_virtual.exceptions.domain.EntityNotFoundException;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.external.AccountCreatorPort;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import com.billetera_virtual.user.domain.port.UserServicePort;
import com.billetera_virtual.user.domain.port.external.PasswordEncoderPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepository;
    private final AccountCreatorPort accountCreator;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    @Transactional
    public User createUser(User user) {

        // agregar validaciones
        if (userRepository.existsByEmail(user.getPassword())) {
            throw new EntityAlreadyExistsException("El email ingresado ya esta registrado");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        user.setStatus(true);

        User savedUser = userRepository.save(user);

        accountCreator.createInitialAccount(savedUser.getId());

        return savedUser;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El ID no coincide con ningún Usuario"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }


    @Override
    public void logicallyDeleteById(Long id) {
        userRepository.logicallyDeleteById(id);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User userUpdate) {
        User existingUser = getById(id);

        existingUser.setFirstname(userUpdate.getFirstname());
        existingUser.setLastname(userUpdate.getLastname());
        existingUser.setEmail(userUpdate.getEmail());

        if (userUpdate.getPassword() != null && !userUpdate.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        }

        return userRepository.save(existingUser);
    }


}
