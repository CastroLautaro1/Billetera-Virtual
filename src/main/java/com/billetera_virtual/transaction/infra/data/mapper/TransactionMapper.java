package com.billetera_virtual.transaction.infra.data.mapper;

import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.infra.data.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toDomain(TransactionEntity entity);

    TransactionEntity toEntity(Transaction domain);

}
