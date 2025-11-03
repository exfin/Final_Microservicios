package com.users.microuser.controllers;

import com.users.microuser.dtos.*;
import com.users.microuser.enums.Role;
import com.users.microuser.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    // 🟢 PUBLICO: Registro de usuarios
    @PostMapping("/register")
    public ResponseEntity<Response<?>> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // 🟢 PUBLICO: Login de usuario
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    // 🔒 CUALQUIER USUARIO AUTENTICADO: Obtener su ID
    @GetMapping("/me/id")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<Long>> getCurrentUserId() {
        Long id = userService.currentUserId();
        return ResponseEntity.ok(Response.<Long>builder()
                .statusCode(200)
                .message("ID de usuario actual obtenido correctamente")
                .data(id)
                .build());
    }

    // 🔒 CUALQUIER USUARIO AUTENTICADO: Actualizar su propia cuenta
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<?>> updateMyAccount(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateMyAccount(userDTO));
    }

    // 🔒 SOLO SUPER_ADMIN: Listar todos los usuarios
    @GetMapping("/all")
    public ResponseEntity<Response<List<UserDTO>>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // 🔒 SOLO SUPER_ADMIN: Actualizar usuario por ID
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<UserDTO>> updateUserById(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO
    ) {
        return ResponseEntity.ok(userService.updateUserById(id, userDTO));
    }

    // 🔒 SOLO SUPER_ADMIN: Eliminar usuario por email
    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.deleteUser(email));
    }

    // 🔒 SOLO SUPER_ADMIN: Buscar usuarios por rol
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<List<UserDTO>>> findAllUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.findAllUsersByRole(role));
    }

    // 🔒 SOLO SUPER_ADMIN: Crear usuario con rol DAEMON
    @PostMapping("/create-daemon")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<?>> createUserWithRoleDaemon(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUserWithRoleDaemon(userDTO));
    }
}
