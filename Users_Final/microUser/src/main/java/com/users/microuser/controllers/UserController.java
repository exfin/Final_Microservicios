package com.users.microuser.controllers;

import com.users.microuser.dtos.*;
import com.users.microuser.enums.Role;
import com.users.microuser.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;


    @PostMapping("/register")
    public ResponseEntity<Response<?>> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }


    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<?>> updateMyAccount(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateMyAccount(userDTO));
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/find-all")
    public ResponseEntity<Response<List<UserDTO>>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response<UserDTO>> updateUserById(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO
    ) {
        return ResponseEntity.ok(userService.updateUserById(id, userDTO));
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response<?>> deleteUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ResponseEntity.ok(userService.deleteUser(email));
    }


    @GetMapping("/find-by-role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response<List<UserDTO>>> findAllUsersByRole(@RequestParam Role role) {
        return ResponseEntity.ok(userService.findAllUsersByRole(role));
    }


    @PostMapping("/create-daemon")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response<?>> createUserWithRoleDaemon(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUserWithRoleDaemon(userDTO));
    }
}
