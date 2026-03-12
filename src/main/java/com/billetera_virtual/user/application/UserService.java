package com.billetera_virtual.user.application;

import com.billetera_virtual.exceptions.domain.EntityAlreadyExistsException;
import com.billetera_virtual.exceptions.domain.EntityNotFoundException;
import com.billetera_virtual.exceptions.domain.InvalidRequestException;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import com.billetera_virtual.user.domain.port.UserServicePort;
import com.billetera_virtual.user.domain.port.external.AccountDataExternalPort;
import com.billetera_virtual.user.domain.port.external.PasswordEncoderPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final AccountDataExternalPort accountDataExternalPort;

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
    public User getUserDataByAccountId(Long accountId) {
        Long user_id = accountDataExternalPort.getUserIdByAccountId(accountId);

        return getById(user_id);
    }

    @Override
    public void logicallyDeleteById(Long id) {
        userRepository.logicallyDeleteById(id);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User userUpdate) {
        User existingUser = getById(id);

        if(userRepository.existsByEmail(userUpdate.getEmail()) &&
                !existingUser.getEmail().equals(userUpdate.getEmail())) {
          throw new EntityAlreadyExistsException("El email ingresado ya esta en uso");
        }

        existingUser.setFirstname(userUpdate.getFirstname());
        existingUser.setLastname(userUpdate.getLastname());
        existingUser.setEmail(userUpdate.getEmail());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void updatePassword(Long id, String password, String newPassword) {
        User existingUser = getById(id);

        if(!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new InvalidRequestException("La contraseña actual es incorrecta");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(existingUser);
    }
}
