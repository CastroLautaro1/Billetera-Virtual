package com.billetera_virtual.user.infra.web;

import com.billetera_virtual.security.infra.model.UserPrincipal;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserServicePort;
import com.billetera_virtual.user.infra.web.dto.CreateUserRequest;
import com.billetera_virtual.user.infra.web.dto.UserProfileResponse;
import com.billetera_virtual.user.infra.web.mapper.UserMapperWeb;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal UserPrincipal principal) {
       User user = userService.getById(principal.getId());
       UserProfileResponse profile = userMapperWeb.toProfileResponse(user);
       return ResponseEntity.ok(profile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/all")
    public ResponseEntity<List<User>> getAll() {
        List<User> allUsers = userService.getAll();
        return ResponseEntity.ok(allUsers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

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
