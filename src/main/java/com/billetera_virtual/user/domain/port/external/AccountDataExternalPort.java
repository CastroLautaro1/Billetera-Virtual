package com.billetera_virtual.user.domain.port.external;

public interface AccountDataExternalPort {

    // Me comunico con el módulo de Account para obtener el user_id de la cuenta que quiero buscar
    Long getUserIdByAccountId(Long accountId);

}
