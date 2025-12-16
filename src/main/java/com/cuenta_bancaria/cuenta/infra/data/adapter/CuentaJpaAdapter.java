package com.cuenta_bancaria.cuenta.infra.data.adapter;

import com.cuenta_bancaria.cuenta.domain.Cuenta;
import com.cuenta_bancaria.cuenta.domain.port.CuentaRepositoryPort;
import com.cuenta_bancaria.cuenta.infra.data.entity.CuentaEntity;
import com.cuenta_bancaria.cuenta.infra.data.mapper.CuentaMapper;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CuentaJpaAdapter implements CuentaRepositoryPort {

    private CuentaJpaRepository jpaRepository;

    public CuentaJpaAdapter(CuentaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cuenta save(Cuenta c) {
        CuentaEntity entity = CuentaMapper.toEntity(c);
        CuentaEntity savedEntity = jpaRepository.save(entity);
        return CuentaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Cuenta> getById(Long id) {
        return jpaRepository.findById(id)
                .map(CuentaMapper::toDomain);
    }

    @Override
    public Optional<Cuenta> getByIdUser(Long idUser) {
        return jpaRepository.findByIdUser(idUser)
                .map(CuentaMapper::toDomain);
    }

    @Override
    public List<Cuenta> findAll() {
        List<CuentaEntity> entities = jpaRepository.findAll();

        return entities.stream()
                        .map(CuentaMapper::toDomain)
                        .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void logicallyDeleteById(Long id) {
        jpaRepository.logicallyDeleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByIdUser(Long idUser) {
        return jpaRepository.existsByIdUser(idUser);
    }
}
