package com.cuenta_bancaria.user.infra.data.adapters;

import com.cuenta_bancaria.user.infra.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
