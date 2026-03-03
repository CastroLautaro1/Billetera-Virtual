package com.billetera_virtual.transaction.domain.port.external;

import com.billetera_virtual.transaction.domain.dto.TransactionAccountInfo;

import java.math.BigDecimal;

public interface AccountExternalPort {

    // Con esos parametros el modulo Account se puede encargar de realizar la transaccion
    BigDecimal makeTransaction(Long originId, Long counterpartyId, BigDecimal amount);

    // Obtengo el ID de la cuenta para guardarlo en la transaccion
    Long getAccountIdByUserId(Long userId);

    // Obtengo el ID de la contraparte usando el alias o CVU
    Long getCounterpartyAccountIdByDestination(String destination);

    // Obtengo la información de una cuenta mediante su Id (nombre completo + alias y cvu)
    TransactionAccountInfo getAccountDataById(Long accountId);


}
