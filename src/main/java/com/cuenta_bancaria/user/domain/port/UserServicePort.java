package com.cuenta_bancaria.user.domain.port;

import com.cuenta_bancaria.user.domain.User;

import java.util.List;

public interface UserServicePort {

    User createUser(User user);
    User getById(Long id);
    List<User> getAll();
    void logicallyDeleteById(Long id);
    User updateUser(Long id, User user);

}
