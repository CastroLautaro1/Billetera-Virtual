package com.billetera_virtual.transaction.infra.web.mapper;

import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.infra.web.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TransactionMapperWeb {

    Transaction toDomain(TransactionDTO request);
}
