package com.billetera_virtual.user.domain.port;

import com.billetera_virtual.user.domain.User;

import java.util.List;

public interface UserServicePort {

    User createUser(User user);
    User getById(Long id);
    List<User> getAll();
    void logicallyDeleteById(Long id);
    User updateUser(Long id, User user);

}
