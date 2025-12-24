package com.cuenta_bancaria.user.infra.data.adapters;

import com.cuenta_bancaria.user.infra.data.entity.UserEntity;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByAlias(String alias);
    Optional<UserEntity> findByEmail(String email);

    boolean existsByAlias(String alias);

    @Modifying
    @Query("UPDATE UserEntity u SET u.status = false WHERE u.id = :id")
    void logicallyDeleteById(@Param("id") Long id);
}
