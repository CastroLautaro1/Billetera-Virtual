package com.billetera_virtual.user.infra.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(

        @NotBlank(message = "La contraseña actual es obligatoria")
        String password,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 8, message = "Debe tener al menos 8 caracteres")
        @Pattern(regexp = ".*[A-Z].*", message = "Debe contener al menos una mayúscula")
        @Pattern(regexp = ".*[0-9].*", message = "Debe contener al menos un número")
        String newPassword
) {}
