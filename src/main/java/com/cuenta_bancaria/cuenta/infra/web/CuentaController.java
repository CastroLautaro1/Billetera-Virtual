package com.cuenta_bancaria.cuenta.infra.web;

import com.cuenta_bancaria.cuenta.domain.Cuenta;
import com.cuenta_bancaria.cuenta.domain.port.CuentaServicePort;
import com.cuenta_bancaria.cuenta.infra.web.dto.CuentaRequest;
import com.cuenta_bancaria.cuenta.infra.web.dto.CuentaUpdateRequest;
import com.cuenta_bancaria.cuenta.infra.web.mapper.CuentaMapperWeb;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    private final CuentaServicePort cuentaService;

    public CuentaController(CuentaServicePort cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody CuentaRequest c) {
        Cuenta cuentaDomain = CuentaMapperWeb.toDomain(c);
        Cuenta cuenta = cuentaService.crearCuenta(cuentaDomain);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cuenta.getId())
                .toUri();
        return ResponseEntity.created(location).body(cuenta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getById(@PathVariable Long id) {
        Cuenta cuenta = cuentaService.getCuentaById(id);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cuenta>> getAll() {
        List<Cuenta> cuenta = cuentaService.getAll();
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/usuario/{idUser}")
    public ResponseEntity<Cuenta> getByIdUser(@PathVariable Long idUser) {
        Cuenta cuenta = cuentaService.getCuentaByIdUser(idUser);
        return ResponseEntity.ok(cuenta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable  Long id) {
        cuentaService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> updateCuenta(
            @RequestBody CuentaUpdateRequest c,
            @PathVariable Long id) {
        Cuenta cuenta = cuentaService.actualizarCuenta(id, c.getMonto());
        return ResponseEntity.ok(cuenta);
    }

}
