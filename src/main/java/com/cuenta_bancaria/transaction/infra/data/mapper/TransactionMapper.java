package com.cuenta_bancaria.transaction.infra.data.mapper;

import com.cuenta_bancaria.transaction.domain.Transaction;
import com.cuenta_bancaria.transaction.infra.data.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toDomain(TransactionEntity entity);

    TransactionEntity toEntity(Transaction domain);

}
