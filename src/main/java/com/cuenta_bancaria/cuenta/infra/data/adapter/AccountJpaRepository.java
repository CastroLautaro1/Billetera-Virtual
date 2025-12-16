package com.cuenta_bancaria.cuenta.infra.data.adapter;

import com.cuenta_bancaria.cuenta.infra.data.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByIdUser(Long idUser);
    Optional<AccountEntity> findByIdUser(Long idUser);
    @Modifying
    @Query("update AccountEntity a set a.status = false  where a.id = :id")
    void logicallyDeleteById(@Param("id") Long id);


}
