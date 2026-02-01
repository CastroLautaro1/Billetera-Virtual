package com.billetera_virtual.account.domain.port.external;

import com.billetera_virtual.account.domain.dto.UserDataDTO;

public interface UserDataPort {
    UserDataDTO getUserDataById(Long id);
}
