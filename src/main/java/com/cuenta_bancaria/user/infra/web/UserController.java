package com.cuenta_bancaria.user.infra.web;

import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.domain.port.UserServicePort;
import com.cuenta_bancaria.user.infra.web.dto.CreateUserRequest;
import com.cuenta_bancaria.user.infra.web.mapper.UserMapperWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userMapperWeb.toDomain(request);
        User createdUser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/alias/{alias}")
    public ResponseEntity<User> getByAlias(@PathVariable String alias) {
        User user = userService.getByAlias(alias);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        List<User> allUsers = userService.getAll();
        return ResponseEntity.ok(allUsers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody CreateUserRequest request) {
        User user = userMapperWeb.toDomain(request);
        User userUpdated = userService.updateUser(id, user);
        return ResponseEntity.ok(userUpdated);
    }



}
