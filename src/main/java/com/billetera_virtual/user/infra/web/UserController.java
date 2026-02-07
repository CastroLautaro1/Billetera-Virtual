package com.billetera_virtual.user.infra.web;

import com.billetera_virtual.security.infra.model.UserPrincipal;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserServicePort;
import com.billetera_virtual.user.infra.web.dto.CreateUserRequest;
import com.billetera_virtual.user.infra.web.dto.UserProfileResponse;
import com.billetera_virtual.user.infra.web.mapper.UserMapperWeb;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServicePort userService;
    private final UserMapperWeb userMapperWeb;

    @Operation(
            summary = "Crear un usuario (Solo Admin)",
            description = "El Admin puede crear un usuario ingresando los campos: firstname, lastname, email y password. También se creara la cuenta correspondiente para el usuario creado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userMapperWeb.toDomain(request);
        User createdUser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @Operation(
            summary = "Obtener usuario por Id (Solo Admin)",
            description = "Se obtiene un usuario ingresando su Id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario existente obtenida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios con ese ID")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Obtener el perfil del usuario logueado",
            description = "El usuario logueado obtiene los datos de su perfil: firstaname, lastname y email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "DTO con los datos del usuario",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal UserPrincipal principal) {
       User user = userService.getById(principal.getId());
       UserProfileResponse profile = userMapperWeb.toProfileResponse(user);
       return ResponseEntity.ok(profile);
    }

    @Operation(
            summary = "Obtener todos los usuarios del sistema (Solo Admin)",
            description = "Se obtienen todos los usuarios del sistema, activos e inactivos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios existentes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/all")
    public ResponseEntity<List<User>> getAll() {
        List<User> allUsers = userService.getAll();
        return ResponseEntity.ok(allUsers);
    }

    @Operation(
            summary = "Eliminar un usuario por su Id (Solo Admin)",
            description = "Se realiza una baja lógica al usuario seleccionado por el Admin."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario dado de baja",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios con ese ID")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar datos del usuario ",
            description = "El usuario logueado puede actualizar los campos que desee "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario actualizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateUserRequest request
    ) {
        User user = userMapperWeb.toDomain(request);
        User userUpdated = userService.updateUser(principal.getId(), user);
        return ResponseEntity.ok(userUpdated);
    }

}
