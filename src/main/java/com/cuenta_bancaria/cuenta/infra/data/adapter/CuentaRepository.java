package com.cuenta_bancaria.cuenta.infra.data.adapter;

import com.cuenta_bancaria.cuenta.domain.Cuenta;
import com.cuenta_bancaria.cuenta.domain.port.CuentaRepositoryPort;
import com.cuenta_bancaria.cuenta.infra.data.entity.CuentaEntity;
import com.cuenta_bancaria.cuenta.infra.data.mapper.CuentaMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cuenta_bancaria.cuenta.infra.data.mapper.CuentaMapper.toEntity;

public class CuentaRepository implements CuentaRepositoryPort {

    private CuentaJpaAdapter cuentaAdapter;

    @Override
    public Cuenta save(Cuenta c) {
        CuentaEntity entity = CuentaMapper.toEntity(c);
        CuentaEntity savedEntity = cuentaAdapter.save(entity);
        return CuentaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Cuenta> getById(Long id) {
        return cuentaAdapter.findById(id)
                .map(CuentaMapper::toDomain);
    }

    @Override
    public Optional<Cuenta> getByIdUser(Long idUser) {
        return cuentaAdapter.findByIdUser(idUser)
                .map(CuentaMapper::toDomain);
    }

    @Override
    public List<Cuenta> findAll() {
        List<CuentaEntity> entities = cuentaAdapter.findAll();

        return entities.stream()
                        .map(CuentaMapper::toDomain)
                        .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        cuentaAdapter.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return cuentaAdapter.existsById(id);
    }

    @Override
    public boolean existsByIdUser(Long idUser) {
        return cuentaAdapter.existsByIdUser(idUser);
    }
}
