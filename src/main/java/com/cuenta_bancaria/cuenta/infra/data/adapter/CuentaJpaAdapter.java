package com.cuenta_bancaria.cuenta.infra.data.adapter;

import com.cuenta_bancaria.cuenta.infra.data.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaJpaAdapter extends JpaRepository<CuentaEntity, Long> {
    boolean existsByIdUser(Long idUser);
    Optional<CuentaEntity> findByIdUser(Long idUser);
}
