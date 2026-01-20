package com.cuenta_bancaria.account.infra.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AliasUpdateRequest(
        @NotBlank(message = "El alias es obligatorio")
        @Size(min = 4, max = 20, message = "El alias debe tener entre 4 y 20 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "El alias solo puede contener letras, números, puntos y guiones bajos")
        String alias
) {}


