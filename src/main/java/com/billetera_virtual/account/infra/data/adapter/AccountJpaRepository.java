package com.billetera_virtual.account.infra.data.adapter;

import com.billetera_virtual.account.infra.data.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByUserId(Long userId);
    boolean existsByAlias(String alias);
    Optional<AccountEntity> findByUserId(Long userId);
    Optional<AccountEntity> findByAlias(String alias);
    Optional<AccountEntity> findByCvu(String cvu);
    @Modifying
    @Query("update AccountEntity a set a.status = false where a.id = :id")
    void logicallyDeleteById(@Param("id") Long id);


}
