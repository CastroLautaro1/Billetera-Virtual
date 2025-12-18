package com.cuenta_bancaria.user.application;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;
import com.cuenta_bancaria.user.domain.port.UserServicePort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepository;

    public UserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {

        // agregar validaciones
        user.setStatus(true);

        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El ID no coincide con ningún Usuario"));
    }

    @Override
    public User getByAlias(String alias) {
        return userRepository.findByAlias(alias)
                .orElseThrow(() -> new RuntimeException("El ID no coincide con ningún Usuario"));
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
    public User updateUser(Long id, User userUpdate) {
        User user = getById(id);

        user.builder()
                .firstname(userUpdate.getFirstname())
                .lastname((userUpdate.getLastname()))
                .alias(userUpdate.getAlias())
                .email(userUpdate.getEmail())
                .password(userUpdate.getPassword())
                .build();

        return userRepository.save(user);
    }
}
