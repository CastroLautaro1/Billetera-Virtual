package com.billetera_virtual.user.infra.data.adapters;

import com.billetera_virtual.user.infra.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE UserEntity u SET u.status = false WHERE u.id = :id")
    void logicallyDeleteById(@Param("id") Long id);
}
