package com.cuenta_bancaria.cuenta.infra.data.adapter;

import com.cuenta_bancaria.cuenta.domain.Cuenta;
import com.cuenta_bancaria.cuenta.infra.data.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CuentaJpaRepository extends JpaRepository<CuentaEntity, Long> {
    boolean existsByIdUser(Long idUser);
    Optional<CuentaEntity> findByIdUser(Long idUser);
    @Modifying
    @Query("update CuentaEntity c set c.estado = 'INHABILITADA'  where c.id = :id")
    void logicallyDeleteById(@Param("id") Long id);


}
