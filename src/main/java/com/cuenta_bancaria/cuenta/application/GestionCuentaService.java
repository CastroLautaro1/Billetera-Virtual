package com.cuenta_bancaria.cuenta.application;

import com.cuenta_bancaria.cuenta.domain.Cuenta;
import com.cuenta_bancaria.cuenta.domain.port.CuentaRepositoryPort;
import com.cuenta_bancaria.cuenta.domain.port.CuentaServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestionCuentaService implements CuentaServicePort{

    private CuentaRepositoryPort cuentaRepository;

    public GestionCuentaService(CuentaRepositoryPort cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Cuenta crearCuenta(Cuenta c) {

        // agregar validacion que verifique si el idUser existe

        if(cuentaRepository.existsByIdUser(c.getIdUser())) {
            throw new RuntimeException("El ID de Usuario ya esta asociado a una cuenta, no puede tener otra");
        }

        if(c.getMonto() < 0) {
            throw new IllegalArgumentException("El monto de la cuenta no puede ser inferior a 0");
        }

        c.setEstado(Cuenta.Estado.ACTIVA);

        return cuentaRepository.save(c);
    }

    @Override
    public Cuenta getCuentaById(Long id) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.getById(id);
        Cuenta cuenta;

        if (cuentaOpt.isPresent()) {
            cuenta = cuentaOpt.get();
        }
        else {
            throw new RuntimeException("El ID ingresado no coincide con ninguna cuenta");
        }

        return cuenta;
    }

    @Override
    public Cuenta getCuentaByIdUser(Long idUser) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.getByIdUser(idUser);
        Cuenta cuenta;

        if (cuentaOpt.isPresent()) {
            cuenta = cuentaOpt.get();
        }
        else {
            throw new RuntimeException("El ID ingresado no coincide con ninguna cuenta");
        }

        return cuenta;
    }

    @Override
    public List<Cuenta> getAll() {
        return cuentaRepository.findAll();
    }

    @Override
    public void eliminarCuenta(Long id) {
        boolean exists = cuentaRepository.existsById(id);

        if(exists) {
            cuentaRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("El ID ingresado no coincide con ninguna cuenta");
        }
    }

    @Override
    public Cuenta actualizarCuenta(Cuenta c) {

        // hacer validacion que verifique que el ID pertenece al Usuario logueado

        if(c.getMonto() < 0) {
            throw new IllegalArgumentException("El monto de la cuenta no puede ser inferior a 0");
        }

        return cuentaRepository.save(c);
    }
}
