package com.cuenta_bancaria.auth.infra.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String firstname,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String lastname,

//        @NotBlank(message = "El alias es obligatorio")
//        @Size(min = 4, max = 20, message = "El alias debe tener entre 4 y 20 caracteres")
//        @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "El alias solo puede contener letras, números, puntos y guiones bajos")
//        String alias,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es valido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        @Pattern(regexp = ".*[A-Z].*", message = "La contraseña debe contener al menos una mayúscula")
        @Pattern(regexp = ".*[0-9].*", message = "La contraseña debe contener al menos un número")
        String password
) {}

