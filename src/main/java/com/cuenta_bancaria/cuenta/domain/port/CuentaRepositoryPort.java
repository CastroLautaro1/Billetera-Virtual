package com.cuenta_bancaria.cuenta.domain.port;

import com.cuenta_bancaria.cuenta.domain.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepositoryPort {

    Cuenta save(Cuenta c);
    Optional<Cuenta> getById(Long id);
    List<Cuenta> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
