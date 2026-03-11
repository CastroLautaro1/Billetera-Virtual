package com.billetera_virtual.user.domain.port;

import com.billetera_virtual.user.domain.User;

import java.util.List;

public interface UserServicePort {

    List<User> getAll();
    User getById(Long id);
    User getUserDataByAccountId(Long accountId);

    User updateUser(Long id, User user);
    void updatePassword(Long id, String password, String newPassword);

    void logicallyDeleteById(Long id);


}
