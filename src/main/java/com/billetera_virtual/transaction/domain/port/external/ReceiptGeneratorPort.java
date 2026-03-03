package com.billetera_virtual.transaction.domain.port.external;

import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.dto.TransactionAccountInfo;

public interface ReceiptGeneratorPort {

    byte [] generateReceipt(Transaction tx, TransactionAccountInfo origin, TransactionAccountInfo destination);
}
