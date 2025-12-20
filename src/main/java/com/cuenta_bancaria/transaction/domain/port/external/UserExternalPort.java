package com.cuenta_bancaria.transaction.domain.port.external;

import com.cuenta_bancaria.user.domain.User;

public interface UserExternalPort {

    Long getUserIdByAlias(String alias);

}
