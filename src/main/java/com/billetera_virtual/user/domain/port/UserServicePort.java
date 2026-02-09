package com.billetera_virtual.user.domain.port;

import com.billetera_virtual.user.domain.User;

import java.util.List;

public interface UserServicePort {

    List<User> getAll();
    User getById(Long id);

    User updateUser(Long id, User user);

    void logicallyDeleteById(Long id);


}
