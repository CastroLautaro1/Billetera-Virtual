package com.cuenta_bancaria.transaction.infra.web.mapper;

import com.cuenta_bancaria.transaction.domain.Transaction;
import com.cuenta_bancaria.transaction.infra.web.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TransactionMapperWeb {
    
    Transaction toDomain(TransactionDTO request);
}
