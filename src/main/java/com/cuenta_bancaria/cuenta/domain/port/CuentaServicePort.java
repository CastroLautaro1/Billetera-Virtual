package com.cuenta_bancaria.cuenta.domain.port;

import com.cuenta_bancaria.cuenta.domain.Cuenta;

import java.util.List;

public interface CuentaServicePort {
    Cuenta crearCuenta(Cuenta c);
    Cuenta getCuentaById(Long id);
    Cuenta getCuentaByIdUser(Long idUser);
    List<Cuenta> getAll();
    void eliminarCuenta(Long id);
    Cuenta actualizarCuenta(Cuenta c);

}
